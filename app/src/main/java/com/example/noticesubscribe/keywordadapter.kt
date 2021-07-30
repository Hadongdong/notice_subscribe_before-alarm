package com.example.noticesubscribe
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class KeyWordAdapter (val keyList:ArrayList<Keyword>): RecyclerView.Adapter<KeyWordAdapter.ViewHolder>() {
    interface ItemClick{
        fun onClick(view: View, pos:Int)
    }
    var itemClick:ItemClick?=null


    //ViewHolder객체 만듦
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyWordAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.key_item, parent, false)
        return ViewHolder(view)
    }


    //생성된 뷰홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: KeyWordAdapter.ViewHolder, position: Int) {
        holder.key.text = keyList[position].key
        if (itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
        //키워드 삭제 관련부분 https://stackoverflow.com/questions/64370610/android-kotlin-how-can-i-delete-the-data-from-firebase
        holder.delete.setOnClickListener {v:View->
            keyList.removeAt(position)
            notifyItemRemoved(position)
            itemClick?.onClick(v,position)
       }
    }

    override fun getItemCount(): Int {
        return keyList.size
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val key: TextView =itemView.findViewById(R.id.keywordbox)
        val delete:ImageView=itemView.findViewById(R.id.btn_delete2)
    }

}
