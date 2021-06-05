package com.example.segment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class SmallchatroomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smallchatroom)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
    }
}