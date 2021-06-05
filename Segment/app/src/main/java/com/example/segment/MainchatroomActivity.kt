package com.example.segment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class MainchatroomActivity : AppCompatActivity() {
    //큰채팅방 내부
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainchatroom)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
    }
}