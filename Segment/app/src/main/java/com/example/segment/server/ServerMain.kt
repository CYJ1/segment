package com.example.segment.server

import com.example.segment.client.ClientMain
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket
import java.io.*
import java.net.*
import kotlin.concurrent.thread

class ServerMain(p : Int) {
    val port = p;

    fun connect(): Unit {
        try {
            val serverSocket = ServerSocket(port)
            val client = serverSocket.accept() // 서버에 접근한 클라이언트와 통신할 수 있는 소켓을 만듭니다.
            val clientInput = client.inputStream // 클라이언트와 연결된 inputstream
            val clientOutput = client.outputStream // 클라이언트와 연결된 outputstream

            while(true) {
                val available = clientInput.available() // 데이터가 있으면 데이터의 사이즈 없다면 -1을 반환합니다.
                if (available > 0) {
                    val dataArr = ByteArray(available) // 사이즈에 맞게 byte array를 만듭니다.
                    clientInput.read(dataArr) // byte array에 데이터를 씁니다.
                    var data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                    data = data.substring(1,data.length-1)
                    val output = data.split(", ")
                        .map { it.split("=") }
                        .map { it.first() to it.last().toString() }
                        .toMap()

                    for(i in output.keys){
                        println("Server : ${i} : ${output[i]}")
                    }

                    if(output["Command"] == "Create Small Room"){ // 작은방 생성 요청이 들어오면 처리
                        println("Server : Create Small Room Received")
                        var chatting = ServerChat()
                        chatting.createSmallRoom(output["ClientNumber"].toString().toInt(),
                            output["message"].toString(), output["ChattingNumber"].toString().toInt())
                    }
                }
            }
        } catch (e: Exception) {
            println("socket connect exception start!!")
            println("e: $e")
        }
    }

    fun main() {
        connect();
    }
}
