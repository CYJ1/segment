package com.example.segment

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
    lateinit var UserData: UserData
    lateinit var DB: Database

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
        DB = Database(this)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding.apply {

            registerbtn.setOnClickListener{

                val id = regId.text.toString()
                val pw = regPw.text.toString()

                if(id==""||pw==""){
                    Toast.makeText(this@RegActivity, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }else{
                    //DB에 정보 저장
                    val user = UserData(id, pw, 0)  //0이면 로그아웃 상태
                    val result = DB.signup(user)

                    if(result){
                        Toast.makeText(this@RegActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        //로그인 화면으로 전환
                        finish()
                    }else{
                        Toast.makeText(this@RegActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


    }
    override fun onBackPressed() {
        finish()
    }

}