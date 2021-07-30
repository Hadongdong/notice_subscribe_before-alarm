package com.example.noticesubscribe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//공지 recyclerview에 들어가는 값들(제목, 날짜, 조회수, 링크)


//class notices(val title: String, val date: String, val visited : String, val link : String) {
//}

@Entity
data class Notice(
    @PrimaryKey var title: String = "",
    @ColumnInfo(name="date") var date: String = "",
    @ColumnInfo(name="visited") var visited: String = "",
    @ColumnInfo(name="link") var link: String = ""
)