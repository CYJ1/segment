package com.example.segment.client

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket

class ClientMain(){

    fun connect(port: Int) {
        try {
            val socketAddress = InetAddress.getByName("172.30.1.37")
            val socket = Socket(socketAddress, 3000)
            val outputStream = socket.getOutputStream()
            val inputStream = socket.getInputStream()
            outputStream.write(("Command : Create Small Room\nChattingNumber : 3").toByteArray(Charsets.UTF_8))
        } catch (e: Exception) {
            println("socket connect exception start!!")
            println("e: $e")
        }
    }

    fun main(){
        connect(3000);
    }
}