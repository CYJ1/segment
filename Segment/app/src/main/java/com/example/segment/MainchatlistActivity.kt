package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivityMainchatlistBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy

class MainchatlistActivity : AppCompatActivity() {
    //메인 채팅방 목록 보이는 화면
    lateinit var binding: ActivityMainchatlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainchatlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
        init()
    }

    private fun init() {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        binding.apply {
            button2.setOnClickListener {
                intent = Intent(this@MainchatlistActivity,MainchatroomActivity::class.java)
                intent.putExtra("ChattingNum", 1); // 일단 1번 채팅방이라고 할게영!!! (지영)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

}