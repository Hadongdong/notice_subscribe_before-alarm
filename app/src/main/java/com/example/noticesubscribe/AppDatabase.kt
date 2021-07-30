package com.example.noticesubscribe

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notice::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao
}