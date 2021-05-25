package com.example.segment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.segment.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    //로그인
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init(){

        binding.apply {

            loginbtn.setOnClickListener {

                val id = idEdit.text.toString()
                val pw = pwEdit.text.toString()
                //id와 pw 정보 대조해서 있으면 로그인 성공 후 메인화면으로 이동
                //로그인 성공 시 유저 로그인 정보 전달, 유저 status 변경
                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)

                //아니면 로그인 실패

//              Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()



            }

            regbtn.setOnClickListener{
                //회원가입 화면으로
                val intent = Intent(this@LoginActivity, RegActivity::class.java)
                startActivity(intent)

            }


        }



    }



}