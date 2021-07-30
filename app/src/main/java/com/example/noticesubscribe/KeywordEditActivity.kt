package com.example.noticesubscribe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noticesubscribe.databinding.ActivityKeywordEditBinding
import com.example.noticesubscribe.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class KeywordEditActivity : AppCompatActivity() {
    private var mainbinding: ActivityKeywordEditBinding? = null
    private val binding get() = mainbinding!!
    val db= FirebaseFirestore.getInstance()//Firestore 인스턴스 선언
    val keyList = arrayListOf<Keyword>()//첫번째 리스트 아이템 배열(구독키워드)
    val keyadapter = KeyWordAdapter(keyList)//첫번째 리사이클러뷰 어댑터 부르기(구독키워드)
    var mDocuments: List<DocumentSnapshot>? = null
//    출처: https://duckssi.tistory.com/42 [홍드로이드의 야매코딩]
    override fun onDestroy() { // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mainbinding = null
        super.onDestroy()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainbinding = ActivityKeywordEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //구독한 키워드 보이기
//        db.collection("Contacts")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val item = document.toObject(Keyword::class.java)
//                    keyList.add(item)
//                }
//                keyadapter.notifyDataSetChanged()
//            }.addOnFailureListener { exception ->
//                Log.w("MainActivity", "Error getting documents: $exception")
//            }
        //(mainbinding?.rvKeyword?.adapter as KeyWordAdapter).getDataFromFirestore()
        mainbinding?.btnAddkeyword?.setOnClickListener {
            val input = binding.keywordinput
            // EditText에서 문자열을 가져와 hashMap으로 만듦
            val data2 = hashMapOf("key" to input.text.toString())
            // Contacts 컬렉션에 data를 자동 이름으로 저장
            db.collection("Contacts")
                .add(data2)
                .addOnSuccessListener {
                    // 성공할 경우
                    Toast.makeText(this, "키워드가 추가되었습니다", Toast.LENGTH_SHORT).show()
                    db.collection("Contacts")//작업할 컬렉션
                        .get() // 문서 가져오기
                        .addOnSuccessListener { result ->
                            keyList.clear()
                            //성공 경우
                            for (document in result) {
                                val item = Keyword(document["key"] as String)
                                keyList.add(item)
                            }
                            keyadapter.notifyDataSetChanged()//리사이클러뷰 갱신
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
        val gridLayoutManager = GridLayoutManager(applicationContext, 4)
        mainbinding?.rvKeyword?.layoutManager = gridLayoutManager
        mainbinding?.rvKeyword?.adapter = keyadapter
        //(mainbinding?.rvKeyword?.adapter as KeyWordAdapter).getDataFromFirestore()
        //이제 키워드 초록 에딧텍스트에 넣어줘야함 선언해줘야한다(백엔드 작업시 불필요한 부분)
        // binding.keywordedittext.text = binding.keywordinput.text

        (mainbinding?.rvKeyword?.adapter as KeyWordAdapter).getDataFromFirestore()
        keyadapter.itemClick = object : KeyWordAdapter.ItemClick {
            override fun onClick(view: View, pos: Int) {

                when(view.id){
                    R.id.btn_delete2->itemDelete(mDocuments!!.get(pos))
                }
            }

        }

   }

    //
    // 키워드 삭제 관련부분 (구현실패) 참고사이트: https://stackoverflow.com/questions/64370610/android-kotlin-how-can-i-delete-the-data-from-firebase
    fun KeyWordAdapter.getDataFromFirestore() {
        db.collection("Contacts")
            .addSnapshotListener{ snapshot, exception ->
                if (exception != null) {
                }
                else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            keyList.clear()
                            mDocuments = snapshot.documents
                            val documents = snapshot.documents
                            for (document in documents) {
                                val item = Keyword(document["key"] as String)
                                keyList.add(item)
                            }
                            notifyDataSetChanged()
                        }
                    }
                }
            }
    }
    fun itemDelete(doc: DocumentSnapshot){
        db.collection("Contacts").document(doc.id)
            .delete()
            }
   }


//        출처: https://duckssi.tistory.com/42 [홍드로이드의 야매코딩]}


