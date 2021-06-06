package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivitySmallchatlistBinding

class SmallchatlistActivity : AppCompatActivity() {
    //작은 채팅방 목록 보이는 화면
    lateinit var binding: ActivitySmallchatlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmallchatlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
        init()
    }

    private fun init() {
        binding.apply {
            button.setOnClickListener {
                intent = Intent(this@SmallchatlistActivity,SmallchatroomActivity::class.java)
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {
        finish()
    }
}