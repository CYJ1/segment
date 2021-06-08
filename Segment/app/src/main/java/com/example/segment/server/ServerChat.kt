package com.example.segment.server

import java.io.*
import java.net.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

//clientNumber : Int, chattingNumber : Int,  questionNumber : Int,  socket: java.net.Socket
class ServerChat(){
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
        val database = Firebase.database.reference
        val dbin = database.child("RoomDB").child("chatting${chattingNumber}").get()
        if(dbin != null) {
            val post = dbin.getResult()
            if (post != null) {
                var bool1 = false
                var bool2 = true
                for (postSnapshot in post.children) {
                    val name = postSnapshot.getKey()
                    val values = postSnapshot.getValue()
                    if (name != null) {
                        if (name.contains("Client") && values == chattingNumber) {
                            println("Server : Client is found")
                            bool1 = true
                            break
                        } else if (name.contains("small")) {
                            println(values)
                            bool2 = false
                            break
                        }
                    }
                }
            }else{
                println("Server : post is null")
            }
        }else{
            println("Server : dbin is null")
        }
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