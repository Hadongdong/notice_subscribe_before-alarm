package com.example.noticesubscribe

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room


class NoticeAdapter( val parentContext: Context,val noticeList: ArrayList<Notice>) : RecyclerView.Adapter<NoticeAdapter.CustomViewHolder>()
    //공지들이 표시되는 recyclerview를 사용하기 위해 만든 adpater
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoticeAdapter.CustomViewHolder {
        //view는 표시되는 화면을 의미
        //button은 은행잎모양 스크랩 버튼 의미
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
//        val button :Button = view.findViewById(R.id.bt_keep_button)
//        button?.setOnClickListener {
//            button?.isSelected = button?.isSelected != true
//        }

        return CustomViewHolder(view) // 이 return값을 이용해서 layout을 누르면 해당 link로 이동하게 하면 될 것 같습니다!
    }

    override fun onBindViewHolder(holder: NoticeAdapter.CustomViewHolder, position: Int) {

        holder.title.text = noticeList.get(position).title
        holder.date.text = noticeList.get(position).date
        holder.visited.text = noticeList.get(position).visited
        holder.link.text = noticeList.get(position).link


        val innerDb = Room.databaseBuilder(
            parentContext.applicationContext,
            AppDatabase::class.java, "notice"
        ).allowMainThreadQueries().build()

        holder.scrap.setOnClickListener {
            innerDb.noticeDao().insert(noticeList.get(position))
            holder.scrap.isSelected = holder.scrap.isSelected != true
        }
        //버튼인식
        if(itemClick!=null){
            holder?.itemView.setOnClickListener{v->
                itemClick?.onClick(v,position)
            }
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.titleView) // 제목
        val date = itemView.findViewById<TextView>(R.id.dateView) // 날짜
        val visited = itemView.findViewById<TextView>(R.id.visitedView) // 조회수
        val link = itemView.findViewById<TextView>(R.id.linkView) // 링크
        val scrap = itemView.findViewById<Button>(R.id.bt_keep_button)

    }


    interface OnItemClickListener // 버튼 클릭을 감지하기 위한 interface
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: OnItemClickListener? = null
}