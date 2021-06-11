package com.example.segment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.TextView
import com.example.recyclerview_ex.UserRecyclerAdapter
import com.example.segment.client.ClientMain
import com.example.segment.client.ClientStatus
import com.example.segment.databinding.ActivityMainchatroomBinding

class UserActivity : AppCompatActivity() {
    //유저의 로그인 상태를 확인할 수 있는 화면
    //userstatus.row에 잇는 isOnline image 색상으로 구분가능
    lateinit var binding: ActivityMainchatroomBinding

    var userStatusData : MutableList<UserData> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()


        init()
    }

    private fun init()
    {
        val ClientNum = intent.getIntExtra("ClientNum",-1)
        val Client = ClientMain()
        val Socket = Client.main()
        val clientStatus = ClientStatus(Socket)

        val status_result = clientStatus.enterUserPage(ClientNum)
        val online = status_result[0]
        val offline = status_result[1]

        val online_arr = online.split(",")
        val offline_arr = offline.split(",")

        val i = 0
        for(username in online_arr){
            userStatusData.add(i, UserData(username, 1, "notuse"))
        }
        for(username in offline_arr){
            userStatusData.add(i, UserData(username, 0,"notuse"))
        }
        val recyclerAdapter = UserRecyclerAdapter(this)

        recyclerAdapter.datas = userStatusData
        recyclerAdapter.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        finish()
    }
}