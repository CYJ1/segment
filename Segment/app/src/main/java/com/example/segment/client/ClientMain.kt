package com.example.segment.client

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket

class ClientMain(){

    fun connect(port: Int) {
        try {
            val socketAddress = InetAddress.getByName("10.0.2.2")
            val socket = Socket(socketAddress, port)
            println("Client : Success socket")
            val outputStream = socket.getOutputStream()
            val inputStream = socket.getInputStream()
            outputStream.write(("Command : Create Small Room\nChattingNumber : 3").toByteArray(Charsets.UTF_8))
            println("Client : Output Success")
            val dataArr = ByteArray(inputStream.available())
            inputStream.read(dataArr)
            val data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
            println("Client : data : ${data}")
        } catch (e: Exception) {
            println("Client : socket connect exception start!!")
            println("e: $e")
        }
    }

    fun main() : Socket{
        val port = 3000
        val socketAddress = InetAddress.getByName("192.168.0.11")
        val socket = Socket(socketAddress, port)

        //var clientChat = ClientChatting(1,1,11,socket)
        //clientChat.createSmallRoom(1,1,"NOTICE")
        //clientChat.enterSmallRoom(1,13)
        //clientChat.destroySmallRoom(1,11)

        return socket
    }
}