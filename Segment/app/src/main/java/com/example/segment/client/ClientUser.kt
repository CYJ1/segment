package com.example.segment.client

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket

class ClientUser(val nickname:String, val password : String, val socket: java.net.Socket){

    var outputStream = socket.getOutputStream()
    var inputStream = socket.getInputStream()

    fun signup(nickname: String, password: String) : Int{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "User Signup")
        inputdata.put("UserID", nickname.toString())
        inputdata.put("UserPW", password.toString())
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))

        while(true){
            val available = inputStream.available()
            if(available>0) {
                val dataArr = ByteArray(available)
                inputStream.read(dataArr) // byte array에 데이터를 씁니다.
                var data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                data = data.substring(1,data.length-1)
                val output = data.split(", ")
                        .map { it.split("=") }
                        .map { it.first() to it.last().toString() }
                        .toMap()
                if(output["nickname"].toString() == nickname){
                    if(output["Result"].toString() == "Success"){
                        println("Client : User Signup Success")
                        return output["ClientNum"].toString().toInt()
                    }else{
                        println("Client : User Signup Fail")
                        return -1
                    }
                    break
                }
            }
        }

        return -1;
    }
}