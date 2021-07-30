package com.example.noticesubscribe.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.noticesubscribe.*
import com.example.noticesubscribe.databinding.FragmentSubscribeBinding

class SubscribeFragment : Fragment() {
    //스크랩 공지
    //mbinding을 통해 네비게이션 바를 이용해서 이동할 수 있는 fragment를 만듦
    private var mBinding : FragmentSubscribeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentSubscribeBinding.inflate(inflater,container,false)

        mBinding = binding

        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
    //스크랩한 공지들이 표시될 recyclerlist 넣기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_subscribe)
//        val notice_list = arrayListOf<notices>(
//            notices("title221", "2020_07_01", "100", "http.."),
//            notices("title222", "2020_07_02", "200", "http..")
//        )


        val innerDb = activity?.let {
            Room.databaseBuilder(
                it.applicationContext,
                AppDatabase::class.java, "notice"
            ).allowMainThreadQueries().build()
        }

        var notice_list = ArrayList<Notice>()
        innerDb?.noticeDao()?.getAll()?.forEach { notice_list.add(it) }

        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = scrapAdapter(view.context, notice_list)
    }
}