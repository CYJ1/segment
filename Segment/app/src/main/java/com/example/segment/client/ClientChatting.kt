package com.example.segment.client

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket

class ClientChatting(clientNumber : Int, chattingNumber : Int, questionNumber : Int, socket: java.net.Socket) {
    var outputStream = socket.getOutputStream()
    var inputStream = socket.getInputStream()
    val clientNumber = clientNumber

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

    fun createSmallRoom(chattingNumber : Int, clientNumber: Int, message : String) : Boolean{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Create Small Room")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
        inputdata.put("message", message)
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))

        while(true) {
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
                if(output["ClientNumber"].toString().toInt() == clientNumber){
                    if(output["Result"].toString() == "Success"){
                        println("Client : Create Small Room Success")
                        return true
                    }else{
                        println("Client : Create Small Room Fail")
                        return false
                    }
                    break
                }
            }
        }
        return true
    }

    fun enterSmallRoom(chattingNumber : Int, questionNumber : Int) : Boolean{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Enter Small Room")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
        inputdata.put("QuestionNumber", questionNumber.toString())
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))

        while(true) {
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
                if(output["ClientNumber"].toString().toInt() == clientNumber){
                    if(output["Result"].toString() == "Success"){
                        println("Client : Enter Small Room Success")
                        return true
                    }else{
                        println("Client : Enter Small Room Fail")
                        return false
                    }
                    break
                }
            }
        }
        return true
    }

    fun destroySmallRoom(chattingNumber : Int, questionNumber : Int) : Boolean{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Destroy Small Room")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("QuestionNumber", questionNumber.toString())
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))

        while(true) {
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
                if(output["ClientNumber"].toString().toInt() == clientNumber){
                    if(output["Result"].toString() == "Success"){
                        println("Client : Enter Small Room Success")
                        return true
                    }else{
                        println("Client : Enter Small Room Fail")
                        return false
                    }
                    break
                }
            }
        }
        return true
    }
}