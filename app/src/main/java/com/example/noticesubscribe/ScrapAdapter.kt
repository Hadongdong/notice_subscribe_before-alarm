package com.example.noticesubscribe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class scrapAdapter(val parentContext: Context, val scrapList: ArrayList<Notice>) : RecyclerView.Adapter<scrapAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): scrapAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        //parentContext = parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: scrapAdapter.CustomViewHolder, position: Int) {
        holder.title.text = scrapList.get(position).title
        holder.date.text = scrapList.get(position).date
        holder.visited.text = scrapList.get(position).visited

        val innerDb = Room.databaseBuilder(
            parentContext.applicationContext,
            AppDatabase::class.java, "notice"
        ).allowMainThreadQueries().build()

        holder.btn_scrap.setOnClickListener {
            innerDb.noticeDao().delete(scrapList.get(position))
            scrapList.remove(scrapList.get(position))
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {
        return scrapList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.titleView) // 제목
        val date = itemView.findViewById<TextView>(R.id.dateView) // 날짜
        val visited = itemView.findViewById<TextView>(R.id.visitedView) // 조회수
        val link = itemView.findViewById<TextView>(R.id.linkView) // 링크
        val btn_scrap = itemView.findViewById<Button>(R.id.bt_keep_button)


    }


}