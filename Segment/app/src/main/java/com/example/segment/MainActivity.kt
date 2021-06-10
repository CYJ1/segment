package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.databinding.ActivityMainBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import com.example.segment.client.ClientMain
import com.example.segment.client.ClientStatus
import com.example.segment.client.ClientUser

class MainActivity : AppCompatActivity() {
    //로그인 후 이동하는 메인 화면
    //유저정보 볼수있는 화면으로 갈수있다
    //큰채팅방 목록 볼수있는 화면으로도 갈수있다
    //로그아웃도 할수있다

    lateinit var binding: ActivityMainBinding
    lateinit var Client: ClientStatus
    lateinit var DB: Database
    lateinit var id:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        init()

    }

    private fun init() {

        val Client = ClientMain()
        val Socket = Client.main()

        if(intent.hasExtra("id")){
            id = intent.getStringExtra("id").toString()
        }else{
            Toast.makeText(this, "로그인 오류!!!", Toast.LENGTH_SHORT).show()
        }
        val ClientNum = intent.getIntExtra("ClientNum",-1)

        DB = Database(this)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //유저 정보 받아온것
        binding.apply {
            btChat.setOnClickListener {
                //큰채팅방 목록으로
                intent = Intent(this@MainActivity, MainchatlistActivity::class.java)
                intent.putExtra("ClientNum", ClientNum)
                startActivity(intent)
            }
            btUser.setOnClickListener {
                //유저화면으로
                intent = Intent(this@MainActivity, UserActivity::class.java)
                intent.putExtra("ClientNum", ClientNum)
                startActivity(intent)
            }
            btLogout.setOnClickListener {
                //로그아웃
                //user status 변경
//              val result = DB.logout(id)
                val ClientStatus = ClientStatus("pw", Socket)
                val result = ClientStatus.logout(ClientNum)
                if(result){
                    Toast.makeText(this@MainActivity,"로그아웃",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@MainActivity,"로그아웃 실패",Toast.LENGTH_SHORT).show()
                }
                //intent.putExtra("ClientNum", ClientNum)
            }
        }
    }
}