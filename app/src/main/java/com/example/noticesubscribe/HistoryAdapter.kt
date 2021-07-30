package com.example.noticesubscribe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter (val historyList:ArrayList<Searchhistory>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    //클릭 인터페이스 구성
    interface ItemClick{
        fun onClick(view: View, pos:Int)
    }
    var itemClick:ItemClick?=null
    var deleteClick:ItemClick?=null

    //ViewHolder객체 만듦
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    //생성된 뷰홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.history.text = historyList[position].history
        if (itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
        //검색어 삭제 관련부분 https://stackoverflow.com/questions/64370610/android-kotlin-how-can-i-delete-the-data-from-firebase
        holder.delete.setOnClickListener {v:View->
            historyList.removeAt(position)
            notifyItemRemoved(position)
            deleteClick?.onClick(v,position)
        }

    }


    override fun getItemCount(): Int {
        return historyList.size
    }


    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val history: TextView =itemView.findViewById(R.id.historyword)
        val delete: ImageView =itemView.findViewById(R.id.btn_delete)
    }

}

