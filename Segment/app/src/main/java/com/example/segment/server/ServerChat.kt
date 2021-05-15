package com.example.segment.server

import java.io.*
import java.net.*

class ServerChat(clientNumber : Int, chattingNumber : Int,  questionNumber : Int,  socket: java.net.Socket){
    fun showList() : Unit{
    }

    fun enterBigRoom( clientNumber : Int,  chattingNumber : Int) : Boolean {
        return true
    }

    fun exitBigRoom( clientNumber : Int,  chattingNumber : Int) : Boolean{
        return true
    }

    fun sendMessage( clientNumber : Int,  chattingNumber : Int,  message :String) : Boolean{
        return true
    }

    fun createSmallRoom( clientNumber : Int,  message : String,  chattingNumber : Int) : Boolean{
        return true
    }

    fun enterSmallRoom( clientNumber : Int,  chattingNumber : Int,  questionNumber : Int ) : Boolean{
        return true
    }

    fun requestOldMessage( clientNumber : Int,  chattingNumber : Int) : Boolean{
        return true
    }

    fun destroySmallRoom( chattingNumber : Int,  questionNumber : Int) : Boolean{
        return true
    }

    fun scatterMessage( chattingNumber : Int) : Boolean{
        return true
    }
}