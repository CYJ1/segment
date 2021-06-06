package com.example.segment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class UserActivity : AppCompatActivity() {
    //유저의 로그인 상태를 확인할 수 있는 화면
    //userstatus.row에 잇는 isOnline image 색상으로 구분가능
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
    }
    override fun onBackPressed() {
        finish()
    }
}