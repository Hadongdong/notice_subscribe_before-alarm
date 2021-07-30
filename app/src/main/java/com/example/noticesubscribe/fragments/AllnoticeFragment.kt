package com.example.noticesubscribe.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noticesubscribe.Keyword
import com.example.noticesubscribe.Notice
import com.example.noticesubscribe.NoticeAdapter
import com.example.noticesubscribe.R
import com.example.noticesubscribe.databinding.FragmentAllnoticeBinding
import com.google.firebase.firestore.FirebaseFirestore


class AllnoticeFragment : Fragment() {
    //전체공지
    //mbinding을 통해 네비게이션 바를 이용해서 이동할 수 있는 fragment를 만듦
    private var mBinding : FragmentAllnoticeBinding? = null
    val db=FirebaseFirestore.getInstance()
//    val noticeList = arrayListOf<Notice>()
//    val noticeadapter=NoticeAdapter(requireContext(), noticeList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentAllnoticeBinding.inflate(inflater,container,false)

        mBinding = binding

        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
    //전체 공지들이 표시될 recyclerlist 넣기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_allnotice)
        val notice_list = arrayListOf<Notice>()
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = NoticeAdapter(view.context, notice_list)
        (mBinding?.rvAllnotice?.adapter as NoticeAdapter).load()//전체 공지사항 보이기
    }
    //전체공지사항 보이기
    fun NoticeAdapter.load(){
        db.collection("total")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = document.toObject(Notice::class.java)
                    noticeList.add(item)
                }
                notifyDataSetChanged()
            }.addOnFailureListener {exception->
                Log.w("MainActivity", "Error getting documents: $exception")
            }
    }
}
