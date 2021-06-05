package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivityMainchatlistBinding

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
        binding.apply {
            button2.setOnClickListener {
                intent = Intent(this@MainchatlistActivity,MainchatroomActivity::class.java)
                startActivity(intent)
            }
        }
    }
}