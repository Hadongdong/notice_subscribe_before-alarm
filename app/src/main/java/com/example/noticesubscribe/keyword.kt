package com.example.noticesubscribe

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Keyword(
    @PrimaryKey var key: String = "",
    @PrimaryKey var timestamp : Long? = null
)