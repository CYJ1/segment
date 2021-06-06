package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivityMainchatroomBinding

class MainchatroomActivity : AppCompatActivity() {
    //큰채팅방 내부
    lateinit var binding:ActivityMainchatroomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainchatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
        init()
    }

    private fun init() {
        binding.apply {
            makesmallchatbtn.setOnClickListener {
                intent = Intent(this@MainchatroomActivity,SmallchatlistActivity::class.java)
                startActivity(intent)
            }
            closechatbtn.setOnClickListener {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

}