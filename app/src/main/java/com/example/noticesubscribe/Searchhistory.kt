package com.example.noticesubscribe

import androidx.room.PrimaryKey

data class Searchhistory(
    @PrimaryKey var history: String = ""
)
