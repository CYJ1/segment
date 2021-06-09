package com.example.segment

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.segment.client.ClientStatus
import com.example.segment.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    //로그인
    lateinit var binding: ActivityLoginBinding
    lateinit var userData: UserData
    lateinit var DB: Database
    var mBackWait:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

            loginbtn.setOnClickListener {

                val id = idEdit.text.toString()
                val pw = pwEdit.text.toString()

                if(id==""||pw==""){
                    Toast.makeText(this@LoginActivity, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }else{
                    val result = DB.login(id, pw)

                    if(result){
                        //로그인 성공 시 유저 로그인 정보 전달, 유저 status 변경
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)

                        intent.putExtra("id",id)
                        startActivity(intent)

                        cleartext()
                    }else{
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            regbtn.setOnClickListener{
                //회원가입 화면으로
                val intent = Intent(this@LoginActivity, RegActivity::class.java)
                startActivity(intent)
                cleartext()

            }


        }



    }
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        } else {
            cleartext()
            finish() //액티비티 종료
        }
    }

    fun cleartext(){
        binding.apply {
            idEdit.text.clear()
            pwEdit.text.clear()
        }
    }


}