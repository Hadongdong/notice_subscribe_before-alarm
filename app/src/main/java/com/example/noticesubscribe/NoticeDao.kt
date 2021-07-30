package com.example.noticesubscribe

import androidx.room.*

@Dao
interface NoticeDao{
    @Query("SELECT * FROM notice")
    fun getAll(): List<Notice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notice: Notice)

    @Delete
    fun delete(notice: Notice)
}