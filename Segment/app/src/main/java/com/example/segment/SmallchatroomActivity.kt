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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import kotlin.concurrent.thread

class SmallchatroomActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmallchatroomBinding
    lateinit var recyclerAdapter: RecyclerAdapter
    var messageData : MutableList<message> = mutableListOf()
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
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        var outputStream : OutputStream = Socket.getOutputStream()
        var inputStream : InputStream = Socket.getInputStream()
        inputdata.put("Command" , "set socket list")
        inputdata.put("ClientNumber" , ClientNum.toString())
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))

        while(true) {
            val available = inputStream.available()
            if(available>0) {
                val dataArr = ByteArray(available)
                inputStream.read(dataArr) // byte array에 데이터를 씁니다.
                var data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                data = data.substring(1,data.length-1)
                val output = data.split(", ")
                    .map { it.split("=") }
                    .map { it.first() to it.last().toString() }
                    .toMap()
                if(output["Result"].toString() == "Success") {
                    println("Client Success : Mapping with ClientNum & Socket")
                    break
                }
                else {
                    println("Client Fail : Mapping with ClientNum & Socket")
                    break
                }
            }
        }

        var oldMessage : ArrayList<message> = ClientChatting(ClientNum, ChattingNum, Socket).requestOldMessage(QuestionNum, ClientNum)
        var repeat = 1
        println("oldMessage's size $repeat : ${oldMessage.size}")
        for(i in oldMessage) {
            messageData.add(i)
            println("Check message : ${i.message} , ${i.time}, ${i.clientNum} , ${i.name}")
        }


        do {
            oldMessage = ClientChatting(ClientNum, ChattingNum, Socket).requestOldMessage(QuestionNum, ClientNum)
            repeat+=1
            println("oldMessage's size $repeat : ${oldMessage.size}")
            for(i in oldMessage) {
                messageData.add(0, i)
                println("Check message : ${i.message} , ${i.time}, ${i.clientNum} , ${i.name}")
            }

        } while ( oldMessage.size != 0 )
        messageData.sortBy { it.index }

        recyclerAdapter = RecyclerAdapter(this, messageData, ClientNum)
        var recyclerView : RecyclerView = findViewById(R.id.smallrecycler)
        recyclerView.adapter = recyclerAdapter
        var myLayoutManager : LinearLayoutManager = LinearLayoutManager(this)
        myLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = myLayoutManager
        recyclerView.scrollToPosition(messageData.size)

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

            smallsendbtn.setOnClickListener {
                var msg = smallchatsendtxt.text.toString()
                var value = ClientChatting(ClientNum, ChattingNum , Socket).sendMessage(ChattingNum, msg)
                if(value != null) {
                    println("Client : Success to send message")
                    val msg = value.split("/")
                        .map { it.split("+") }
                        .map { it.first() to it.last().toString()}
                        .toMap()
                    val chatTime = msg["chatTime"].toString()
                    val userName = msg["userName"].toString()
                    val message = msg["message"].toString()
                    val clientNum = msg["clientNum"].toString().toInt()
                    val messageNum = msg["messageNum"].toString().toInt()
                    println("<Done scatterMessage> check null : $chatTime, $userName, $message, $clientNum, $messageNum")
                    var Msg = message(userName, message, chatTime, clientNum, messageNum)
                    println("<Done scatterMessage> size : ${recyclerAdapter.items.size}")
                    recyclerAdapter.addItem(Msg)
                    recyclerAdapter.notifyDataSetChanged()
                    println("<Done scatterMessage>  update the adapter")
                }
                else {
                    println("Client : Fail to send message")
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

        thread(start = true) {
            try {
                while(true) {
                    val available = inputStream.available()
                    if(available>0) {

                        val dataArr = ByteArray(available)
                        inputStream.read(dataArr) // byte array에 데이터를 씁니다.
                        var data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                        data = data.substring(1,data.length-1)
                        val output = data.split(", ")
                            .map { it.split("=") }
                            .map { it.first() to it.last().toString() }
                            .toMap()
                        for ( i in output.keys) {
                            println("<Done scatterMessage> $i : ${output[i]}")
                        }
                        val outputS = output["data"].toString()
                        val msg = outputS!!.split("/")
                            .map { it.split("+")}
                            .map { it.first() to it.last().toString() }
                            .toMap()
                        val chatTime = msg["chatTime"].toString()
                        val userName = msg["userName"].toString()
                        val message = msg["message"].toString()
                        val clientNum = msg["clientNum"].toString().toInt()
                        val messageNum = msg["messageNum"].toString().toInt()
                        println("<Done scatterMessage> check null : $chatTime, $userName, $message, $clientNum, $messageNum")
                        var Msg = message(userName, message, chatTime, clientNum, messageNum)
                        println("<Done scatterMessage> size : ${recyclerAdapter.items.size}")
                        recyclerAdapter.addItem(Msg)

                        println("<Done scatteruMessage> done to create message")
//                        //recyclerAdapter.notifyItemInserted(0)
                        //handlerTest(1)
                        //Toast.makeText(this, output["Command"].toString(), Toast.LENGTH_SHORT)
                    }
                }
            } catch(e : Exception) {
                println("ERR : ${e.message}")
            }
        }
    }
    override fun onBackPressed() {

        finish()
    }
}