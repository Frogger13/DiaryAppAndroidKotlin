package com.androidapp.diaryapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(listArray:ArrayList<ListItem>, context: Context): RecyclerView.Adapter<MyAdapter.ViewHolder>(){
    var listArrayR = listArray
    var contextR = context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val tvTime= view.findViewById<TextView>(R.id.tvTime)
        val tvText = view.findViewById<TextView>(R.id.tvText)

        fun bind(listItem: ListItem,context: Context){
            tvTime.text = listItem.time
            tvText.text = listItem.text
            itemView.setOnClickListener(){
                var intent = Intent(context, AboutTask::class.java).apply {
                    putExtra("Time", tvTime.text.toString())
                    putExtra("Name", tvText.text.toString())
                }
                context.startActivity(intent)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(contextR)
        return ViewHolder(inflater.inflate(R.layout.day_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var listItem = listArrayR.get(position)
        holder.bind(listItem, contextR)
    }

    override fun getItemCount(): Int {
        return listArrayR.size
    }

    fun updateAdapter(listArray: List<ListItem>){
        listArrayR.clear()
        listArrayR.addAll(listArray)
        notifyDataSetChanged()
    }
}