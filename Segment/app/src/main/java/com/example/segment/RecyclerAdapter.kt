package com.example.segment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class RecyclerAdapter(val context: Context, val boardList: MutableList<message>, val clientNum : Int) : RecyclerView.Adapter<RecyclerAdapter.BaseViewHolder<*>>() {

    var items : MutableList<message> = boardList
    abstract class BaseViewHolder<message>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: message)

    }
    fun addItem(msg : message) {
        items.add(msg)
    }

    inner class ViewHolderMy(itemView:View):BaseViewHolder<message>(itemView){
        val text = itemView.findViewById<TextView>(R.id.chat_Text)
        val time = itemView.findViewById<TextView>(R.id.chat_Time)

        override fun bind(item: message) {
            text?.text = item.message
            time?.text = item.time
        }
    }

    inner class ViewHolderOther(itemView: View):BaseViewHolder<message>(itemView){
        val text = itemView.findViewById<TextView>(R.id.chat_Text)
        val time = itemView.findViewById<TextView>(R.id.chat_Time)
        val name = itemView.findViewById<TextView>(R.id.chat_Yourname)
        override fun bind(item: message) {
            text?.text = item.message
            time?.text = item.time
            name?.text = item.name
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        when(holder){
            is ViewHolderMy -> holder.bind(element as message)
            is ViewHolderOther -> holder.bind(element as message)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if ( items[position].clientNum == clientNum) {
            return 1 // my
        }
        else {
            return 0 // other
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if(viewType==1) {
            val view = LayoutInflater.from(context).inflate(R.layout.mychat, parent, false)
            return ViewHolderMy(view)
        }
        else {
            val view = LayoutInflater.from(context).inflate(R.layout.yourchat, parent, false)
            return ViewHolderOther(view)
        }
    }
}