package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import com.example.segment.client.ClientChatting
import com.example.segment.client.ClientMain
import com.example.segment.databinding.ActivitySmallchatlistBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy

class SmallchatlistActivity : AppCompatActivity() {
    //작은 채팅방 목록 보이는 화면
    lateinit var binding: ActivitySmallchatlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmallchatlistBinding.inflate(layoutInflater)
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

        var clientMain = ClientMain()
        val socket = clientMain.main()

        val clientChatting = ClientChatting(ClientNum, ChattingNum, socket)
        val smallRoomArray = clientChatting.getSmallRoomList(ChattingNum,ClientNum)

        if(smallRoomArray[0] == -1){
            var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton1)
            smallRoom1.text = "소채팅방 1번 만들기"
        }
        if(smallRoomArray[1] == -1){
            var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton2)
            smallRoom1.text = "소채팅방 2번 만들기"
        }
        if(smallRoomArray[2] == -1){
            var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton3)
            smallRoom1.text = "소채팅방 3번 만들기"
        }

        binding.apply {
            SmallRoomButton1.setOnClickListener {
                intent = Intent(this@SmallchatlistActivity, SmallchatroomActivity::class.java)
                intent.putExtra("ChattingNum", ChattingNum); // 일단 1번 채팅방이라고 할게영!!! (지영)
                intent.putExtra("ClientNum", ClientNum); //일단 1번 회원이라고 할게영!!!(지영)
                intent.putExtra("QuestionNum", ChattingNum*10 + 1); // 일단 1번 채팅방이라고 할게영!!! (지영)
                if(smallRoomArray[0] == -1){
                    intent.putExtra("Command", "Create")
                }else{
                    intent.putExtra("Command", "Get")
                }
                startActivity(intent)
            }

            SmallRoomButton2.setOnClickListener {
                intent = Intent(this@SmallchatlistActivity, SmallchatroomActivity::class.java)
                intent.putExtra("ChattingNum", ChattingNum); // 일단 1번 채팅방이라고 할게영!!! (지영)
                intent.putExtra("ClientNum", ClientNum); //일단 1번 회원이라고 할게영!!!(지영)
                intent.putExtra("QuestionNum", ChattingNum*10 + 2); // 일단 1번 채팅방이라고 할게영!!! (지영)
                if(smallRoomArray[1] == -1){
                    intent.putExtra("Command", "Create")
                }else{
                    intent.putExtra("Command", "Get")
                }
                startActivity(intent)
            }

            SmallRoomButton3.setOnClickListener {
                intent = Intent(this@SmallchatlistActivity, SmallchatroomActivity::class.java)
                intent.putExtra("ChattingNum", ChattingNum); // 일단 1번 채팅방이라고 할게영!!! (지영)
                intent.putExtra("ClientNum", ClientNum); //일단 1번 회원이라고 할게영!!!(지영)
                intent.putExtra("QuestionNum", ChattingNum*10 + 3); // 일단 1번 채팅방이라고 할게영!!! (지영)
                if(smallRoomArray[2] == -1){
                    intent.putExtra("Command", "Create")
                }else{
                    intent.putExtra("Command", "Get")
                }
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {
        finish()
    }
}