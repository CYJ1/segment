package com.example.segment.client

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket

class ClientUser(val nickname:String, val password : String, val socket: java.net.Socket){
    fun singup(nickname: String, password: String) : Boolean{
        return true;
    }
}