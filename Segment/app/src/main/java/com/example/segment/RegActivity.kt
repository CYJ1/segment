package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivityRegBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy

class RegActivity : AppCompatActivity() {
    //회원가입 화면
    lateinit var binding: ActivityRegBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        init()

    }

    private fun init(){
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding.apply {

            registerbtn.setOnClickListener{

                val id = regId.text.toString()
                val pw = regPw.text.toString()

                //DB에 정보 저장
                val result = true  //성공시 1 실패시 0 반환하는거...


                Toast.makeText(this@RegActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                //로그인 화면으로 전환
                finish()

//                Toast.makeText(this@RegActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()



            }

        }


    }
    override fun onBackPressed() {
        finish()
    }

}