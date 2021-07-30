package com.example.noticesubscribe.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.noticesubscribe.R
import com.example.noticesubscribe.databinding.FragmentAlarmBinding
import com.example.noticesubscribe.databinding.FragmentSearchBinding

class AlarmFragment : Fragment() {
    //알림설정
    //mbinding을 통해 네비게이션 바를 이용해서 이동할 수 있는 fragment를 만듦
    private var mBinding : FragmentAlarmBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentAlarmBinding.inflate(inflater,container,false)

        mBinding = binding
        R.id.bt_editKeyword
        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}