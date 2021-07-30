package com.example.noticesubscribe

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.example.noticesubscribe.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    val db= FirebaseFirestore.getInstance()
    var notice_list = ArrayList<Notice>()
    val keyList = arrayListOf<Keyword>()//첫번째 리스트 아이템 배열(구독키워드)
    val keyadapter = KeyWordAdapter(keyList)//첫번째 리사이클러뷰 어댑터 부르기(구독키워드)

    private lateinit var mBinding: ActivityMainBinding

    //네비게이션바를 통해 실행됨
    //drawable에 파일이 굉장히 많아요! 그 중 백엔드에서 그나마 쓸만한 파일은 keep_button.xml(버튼 디자인)만 있을 듯합니다
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {}

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        //네비게이션들을 담는 호스트
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션컨트롤러
        val navController = navHostFragment.navController

        //바텀 네비게이션뷰와 네비게이션을 묶어준다
        NavigationUI.setupWithNavController(mBinding.myBottomNav,navController)





    }
}

