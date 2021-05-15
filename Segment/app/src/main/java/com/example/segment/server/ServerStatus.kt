package com.example.segment.server

import java.io.*
import java.net.*

class ServerStatus( clientNumber : Int,  nickname : String,  password : String,  socket: java.net.Socket){
    fun login( clientNumber : Int) : Boolean {
        return true
    }

    fun authCheck( nickname : String,  password: String,  clientNumber : Int) : Boolean {
        return true
    }

    fun logout( clientNumber:Int) : Boolean {
        return true
    }

    fun checkAllStatus() : Boolean {
        return true
    }

    fun scatterMessage() : Boolean {
        return true
    }
}
