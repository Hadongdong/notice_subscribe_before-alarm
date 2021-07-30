package com.example.noticesubscribe.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noticesubscribe.*
import com.example.noticesubscribe.databinding.FragmentSearchBinding
import com.google.api.ResourceDescriptor

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class SearchFragment : Fragment() {
    //공지검색
    //mbinding을 통해 네비게이션 바를 이용해서 이동할 수 있는 fragment를 만듦
    private var mBinding: FragmentSearchBinding? = null
    val db = FirebaseFirestore.getInstance()
    var mDocuments: List<DocumentSnapshot>? = null
    //val notice_list = arrayListOf<Notice>()
    //val noticeadapter=NoticeAdapter(requireContext(), notice_list)
    val historylist = arrayListOf<Searchhistory>()
    val historyadapter = HistoryAdapter(historylist)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSearchBinding.inflate(inflater, container, false)

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_searchnotice)
        val notice_list = arrayListOf<Notice>()
//      val historylist = arrayListOf<Searchhistory>()
//        val noticeadapter = NoticeAdapter(view.context, notice_list)
//       val historyadapter = HistoryAdapter(historylist)
        val recyclerView1 = view.findViewById<RecyclerView>(R.id.rv_searchhistory)
        var searchOption = "title"
        val searchOption2 = "history"
        //스피너 (제목,키워드) 생성
        mBinding?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (mBinding?.spinner?.getItemAtPosition(position)) {
                    "제목" -> {
                        searchOption = "title"
                    }
                    "키워드" -> {
                        searchOption = "keyword"
                    }
                }
            }
        }

        //검색버튼 클릭시
        mBinding?.button?.setOnClickListener {
            //검색 옵션에 따라 검색
            (mBinding?.rvSearchnotice?.adapter as NoticeAdapter).search2(mBinding?.searchWord?.text.toString(), searchOption)
            //searchword에서 문자열을 가져와 hashMap으로 만듦
            val input = mBinding?.searchWord
            val data = hashMapOf("history" to input?.text?.toString())
            //검색어 기록
            (mBinding?.rvSearchhistory?.adapter as HistoryAdapter).search3(data, searchOption2)
        }
        //검색기록의 검색어 클릭시 해당 공지사항 나열.
        mBinding?.rvSearchhistory?.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
//      recyclerView1.setHasFixedSize(true)
        mBinding?.rvSearchhistory?.adapter =historyadapter
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = NoticeAdapter(view.context, notice_list)
        historyadapter.itemClick = object : HistoryAdapter.ItemClick {
            override fun onClick(view: View, pos: Int) {
                val history: TextView = view.findViewById(R.id.historyword)
                val searchOption = "title"
                (mBinding?.rvSearchnotice?.adapter as NoticeAdapter).move(history.text.toString(), searchOption)

            }
        }

        (mBinding?.rvSearchhistory?.adapter as HistoryAdapter).getDataFromFirestore()
        //검색어 ,클릭삭제 관련부분
        historyadapter.deleteClick = object:HistoryAdapter.ItemClick {
            override fun onClick(v: View, pos: Int) {

                when(v.id){
                        R.id.btn_delete -> itemDelete(mDocuments!!.get(pos))
                }
            }
       }
    }
    //검색기록 추가
    fun HistoryAdapter.search3(word: HashMap<String, String?>, option: String) {
        db.collection("History")
            .add(word)
            .addOnSuccessListener {
                // 성공할 경우
                db.collection("History")//작업할 컬렉션
                    .get() // 문서 가져오기
                    .addOnSuccessListener { result ->
                        historyList.clear()
                        //성공 경우
                        for (document in result) {
                            val item2 = Searchhistory(document[option] as String)
                            historyList.add(item2)
                        }
                        notifyDataSetChanged()//리사이클러뷰 갱신
                    }
                    .addOnFailureListener { exception ->
                        //실패 경우
                        Log.w("MainActivity", "Error getting documents: $exception")
                    }
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("MainActivity", "Error getting documents: $exception")
            }
    }

    //파이어베이스에서 모든검색 결과 가져오기
    fun NoticeAdapter.search2(searchWord: String, option: String) {
        db.collection("total")   // 작업할 컬렉션
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                noticeList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (document.getString(option)!!.contains(searchWord)) {
                        val item = Notice(document["title"] as String, document["date"] as String, document["visited"] as String, document["link"] as String)
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
    //기존 검색 기록 가져오기
//    fun HistoryAdapter.load2() {
//        db.collection("History")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val item = document.toObject(Searchhistory::class.java)
//                    historyList.add(item)
//                }
//                notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Log.w("MainActivity", "Error getting documents: $exception")
//            }
//    }

    //검색기록의 검색어 클릭통해 공지사항보기 함수(homefragment의 search함수와 비교해서 다른 점 있으면 말씀해주십시오!! 분명히 같은데...)
    fun NoticeAdapter.move(word: String, option: String) {
        db.collection("total")   // 작업할 컬렉션2
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                noticeList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (document.getString(option)!!.contains(word)) {
                        val item = Notice(document["title"] as String, document["date"] as String, document["visited"] as String, document["link"] as String)
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
//이전 검색어 삭제 관련부분:https://stackoverflow.com/questions/64370610/android-kotlin-how-can-i-delete-the-data-from-firebase
    fun HistoryAdapter.getDataFromFirestore() {
        db.collection("History")
            .addSnapshotListener{ snapshot, exception ->
                if (exception != null) {
                }
                else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            historyList.clear()
                            mDocuments = snapshot.documents
                            val documents = snapshot.documents
                            for (document in documents) {
                                val item = Searchhistory(document["history"] as String)
                                historyList.add(item)
                            }
                                notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
    fun itemDelete(doc:DocumentSnapshot){
        db.collection("History").document(doc.id)
            .delete()
    }

    }




















