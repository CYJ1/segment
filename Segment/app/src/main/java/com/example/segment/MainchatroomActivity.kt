package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivityMainchatroomBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy

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
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var ChattingNum = intent.getIntExtra("ChattingNum", -1)
        var ClientNum = intent.getIntExtra("ClientNum", -1)
        var title_textview = findViewById<TextView>(R.id.mainchatname)
        if(ChattingNum == 1){
            title_textview.text = "C++"
        }else if(ChattingNum == 2){
            title_textview.text = "Java"
        }else if(ChattingNum == 3){
            title_textview.text = "Python"
        }else if(ChattingNum == 4){
            title_textview.text = "Html"
        }
        else if(ChattingNum == 5){
            title_textview.text = "JavaScript"
        }


        binding.apply {
            makesmallchatbtn.setOnClickListener {
                intent = Intent(this@MainchatroomActivity,SmallchatlistActivity::class.java)
                intent.putExtra("ChattingNum", ChattingNum); // 일단 1번 채팅방이라고 할게영!!! (지영)
                intent.putExtra("ClientNum", ClientNum); //일단 1번 회원이라고 할게영!!!(지영)
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