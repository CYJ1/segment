package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.client.ClientChatting
import com.example.segment.client.ClientMain
import com.example.segment.databinding.ActivitySmallchatroomBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy

class SmallchatroomActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmallchatroomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmallchatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
        init()
    }

    private fun init() {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var ChattingNum = intent.getIntExtra("ChattingNum", -1)
        var ClientNum = intent.getIntExtra("ClientNum", -1)
        var QuestionNum = intent.getIntExtra("QuestionNum", -1)
        var Command = intent.getStringExtra("Command")

        val Client = ClientMain()
        val Socket = Client.main()

        val ClientChatting = ClientChatting(ClientNum, ChattingNum, Socket)

        if(Command == "Create"){
            ClientChatting.createSmallRoom(ChattingNum, ClientNum, QuestionNum, "NOTHING YET")
        }else{
            ClientChatting.enterSmallRoom(ChattingNum, QuestionNum)
        }
        binding.apply {
            closesmallchatbtn.setOnClickListener {
                ClientChatting.exitSmallRoom(ChattingNum,QuestionNum)
                finish()
            }
        }
    }
    override fun onBackPressed() {
        finish()
    }
}