package com.example.segment.server

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket
import java.io.*
import java.net.*

class ServerMain(p : Int) {
    val port = p;

    fun connect(): Unit {
        val port = 3000 // port를 설정합니다.

        val serverSocket = ServerSocket(port)
        val client = serverSocket.accept() // 서버에 접근한 클라이언트와 통신할 수 있는 소켓을 만듭니다.
        val clientInput = client.inputStream // 클라이언트와 연결된 inputstream
        val clientOutput = client.outputStream // 클라이언트와 연결된 outputstream

        while(true) {
            val available = clientInput.available() // 데이터가 있으면 데이터의 사이즈 없다면 -1을 반환합니다.
            if (available > 0) {
                val dataArr = ByteArray(available) // 사이즈에 맞게 byte array를 만듭니다.
                clientInput.read(dataArr) // byte array에 데이터를 씁니다.
                val data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                println("data : ${data}")
            }
        }
    }

    fun main() {
        connect();
    }
}
