package com.example.noticesubscribe.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noticesubscribe.databinding.FragmentHomeBinding
import android.content.Intent
import android.util.Log
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noticesubscribe.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    //홈
    //mbinding을 통해 네비게이션 바를 이용해서 이동할 수 있는 fragment를 만듦
    private var mBinding: FragmentHomeBinding? = null
    val db=FirebaseFirestore.getInstance()
    //var notice_list = ArrayList<Notice>()
    val keyList = arrayListOf<Keyword>()//첫번째 리스트 아이템 배열(구독키워드)
    val keyadapter = KeyWordAdapter(keyList)//첫번째 리사이클러뷰 어댑터 부르기(구독키워드)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        mBinding = binding

        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

    //검색 내용에 해당하는 공지들이 표시될 recyclerlist 넣기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keywordeditbutton = view.findViewById<Button>(R.id.bt_editKeyword)
        keywordeditbutton.setOnClickListener {
            val intent = Intent(getActivity(), KeywordEditActivity::class.java)
            startActivity(intent)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_homenotice)
        val notice_list = arrayListOf<Notice>()
        //현재의 구독키워드 보여줌
        db.collection("Contacts")
           // .orderBy()
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = document.toObject(Keyword::class.java)
                    keyList.add(item)
                }
                keyadapter.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: $exception")
            }
        val gridLayoutManager = GridLayoutManager(view.context, 4)
        mBinding?.rvKeyword?.layoutManager = gridLayoutManager
        mBinding?.rvKeyword?.adapter = keyadapter
        keyadapter.itemClick = object : KeyWordAdapter.ItemClick {
            override fun onClick(view: View, pos: Int) {
                val key: TextView = view.findViewById(R.id.keywordbox)
                val searchOption = "title"//현재는 Notice에 키워드가 포함되어 있지 않아서 제목을 통해서 관련공지사항을 수집함. 추후에 키워드가 생겨나면 "keyword" 로 대체
                (mBinding?.rvHomenotice?.adapter as NoticeAdapter).search(key.text.toString(), searchOption)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = NoticeAdapter(view.context, notice_list)
    }

    //키워드 클릭시 관련공지사항 나옴
    fun NoticeAdapter.search(key:String,option: String){
        db.collection("total")   // 작업할 컬렉션
            .orderBy("date", Query.Direction.DESCENDING)
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                noticeList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (document.getString(option)!!.contains(key)) {
                        val item =  Notice(document["title"] as String, document["date"] as String,document["visited"] as String,document["link"] as String)
                        noticeList.add(item)
                    }
                }
                    notifyDataSetChanged()  // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("MainActivity", "Error getting documents: $exception")
            }
}
}