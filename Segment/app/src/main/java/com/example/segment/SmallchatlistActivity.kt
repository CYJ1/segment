package com.example.segment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class SmallchatlistActivity : AppCompatActivity() {
    //작은 채팅방 목록 보이는 화면
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smallchatlist)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
    }
}