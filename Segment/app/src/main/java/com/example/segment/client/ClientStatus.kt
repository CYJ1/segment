package com.example.segment.client

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket

class ClientStatus(password : String, socket: java.net.Socket){
    fun login(nickname : String, password : String) : Boolean{
        return true;
    }

    fun logout() : Boolean{
        return true;
    }

    fun enterUserPage() : Boolean{
        return true;
    }
}