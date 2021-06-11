package com.example.recyclerview_ex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.segment.R
import com.example.segment.UserData

class UserRecyclerAdapter(val userStatusData: MutableList<UserData>, val context:Context) : RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.userstatus_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = userStatusData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userStatusData[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtName: TextView = itemView.findViewById(R.id.username)
        private val imgProfile: ImageView = itemView.findViewById(R.id.isOnline)

        fun bind(item: UserData) {
            txtName.text = item.id

            if(item.status != 1) {
                imgProfile.visibility = View.INVISIBLE
            }else{
                imgProfile.visibility = View.VISIBLE
            }

        }
    }


}