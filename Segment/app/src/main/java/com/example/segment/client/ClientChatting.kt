package com.example.segment.client

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket

class ClientChatting(clientNumber : Int, chattingNumber : Int, questionNumber : Int, socket: java.net.Socket) {

    fun requestList() : Boolean{
        return true;
    }

    fun enterBigRoom(chattingNumber : Int) : Boolean{
        return true;
    }

    fun exitBigRoom(chattingNumber : Int) : Boolean{
        return true;
    }

    fun sendMessage(chattingNumber : Int, message : String) : Boolean{
        return true;
    }

    fun createSmallRoom(chattingNumber : Int, outputStream: OutputStream) : Boolean{
        outputStream.write(("Command : Create Small Room\nChattingNumber : " + chattingNumber).toByteArray(Charsets.UTF_8))
        return true;
    }

    fun enterSmallRoom(chattingNumber : Int, questionNumber : Int) : Boolean{
        return true;
    }

    fun destroySmallRoom(chattingNumber : Int, questionNumber : Int) : Boolean{
        return true;
    }
}