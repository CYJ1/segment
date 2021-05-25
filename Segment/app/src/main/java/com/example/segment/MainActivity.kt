package com.example.segment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    //로그인 후 이동하는 메인 화면
    //유저정보 볼수있는 화면으로 갈수있다
    //큰채팅방 목록 볼수있는 화면으로도 갈수있다
    //로그아웃도 할수있다 와!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}