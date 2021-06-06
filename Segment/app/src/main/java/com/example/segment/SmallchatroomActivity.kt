package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivitySmallchatroomBinding

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
        binding.apply {
            closesmallchatbtn.setOnClickListener {
                finish()
            }
        }
    }
    override fun onBackPressed() {
        finish()
    }
}