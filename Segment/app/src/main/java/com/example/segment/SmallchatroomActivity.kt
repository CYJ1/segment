package com.example.segment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.segment.client.ClientChatting
import com.example.segment.client.ClientMain
import com.example.segment.databinding.ActivitySmallchatroomBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class SmallchatroomActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmallchatroomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmallchatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
        init()
    }

    var ChattingNum  = -1
    var ClientNum = -1
    private fun init() {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        ChattingNum = intent.getIntExtra("ChattingNum", -1)
        ClientNum = intent.getIntExtra("ClientNum", -1)
        var QuestionNum = intent.getIntExtra("QuestionNum", -1)
        var Command = intent.getStringExtra("Command")

        var title_textview = findViewById<TextView>(R.id.smallchatname)
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

        val Client = ClientMain()
        val Socket = Client.main()

        val ClientChatting = ClientChatting(ClientNum, ChattingNum, Socket)

        if(Command == "Create"){
            ClientChatting.createSmallRoom(ChattingNum, ClientNum, QuestionNum, "NOTHING YET")
        }else{
            ClientChatting.enterSmallRoom(ChattingNum, QuestionNum)
        }
        binding.apply {
            deletesmallroom.setOnClickListener{
                val result = ClientChatting.destroySmallRoom(ChattingNum,QuestionNum)

                if(result == false){
                    Toast.makeText(this@SmallchatroomActivity, "소채팅방 삭제 실패" , Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@SmallchatroomActivity, "소채팅방 삭제 성공" , Toast.LENGTH_SHORT).show()
                    intent = Intent(this@SmallchatroomActivity,SmallchatlistActivity::class.java)
                    intent.putExtra("ChattingNum", ChattingNum); // 일단 1번 채팅방이라고 할게영!!! (지영)
                    intent.putExtra("ClientNum", ClientNum); //일단 1번 회원이라고 할게영!!!(지영)
                    startActivity(intent)
                }
            }
            closesmallchatbtn.setOnClickListener {
                ClientChatting.exitSmallRoom(ChattingNum,QuestionNum)
                var clientMain = ClientMain()
                val socket = clientMain.main()

                val clientChatting = ClientChatting(ClientNum, ChattingNum, socket)
                val smallRoomArray = clientChatting.getSmallRoomList(ChattingNum,ClientNum)

                if(smallRoomArray[0] == -1){
                    var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton1)
                    smallRoom1.text = "소채팅방 1번 만들기"
                }else{
                    var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton1)
                    smallRoom1.text = "소채팅방 1번 입장하기"
                }
                if(smallRoomArray[1] == -1){
                    var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton2)
                    smallRoom1.text = "소채팅방 2번 만들기"
                }else{
                    var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton1)
                    smallRoom1.text = "소채팅방 2번 입장하기"
                }
                if(smallRoomArray[2] == -1){
                    var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton3)
                    smallRoom1.text = "소채팅방 3번 만들기"
                }else{
                    var smallRoom1 = findViewById<Button>(R.id.SmallRoomButton1)
                    smallRoom1.text = "소채팅방 3번 입장하기"
                }
                finish()
            }
        }
    }
    override fun onBackPressed() {

        finish()
    }
}