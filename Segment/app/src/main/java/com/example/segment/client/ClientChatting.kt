package com.example.segment.client

import com.example.segment.message

class ClientChatting(clientNumber : Int, chattingNumber : Int, socket: java.net.Socket) {
    var outputStream = socket.getOutputStream()
    var inputStream = socket.getInputStream()
    val clientNumber = clientNumber


    fun requestList() : Boolean{
        return true;
    }

    fun enterBigRoom(chattingNumber : Int) : Boolean{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Enter Big Room")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
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
                        println("Client : Enter Big Room Success")
                        return true
                    }else{
                        println("Client : Enter Big Room Fail")
                        return false
                    }
                    break
                }
            }
        }
        return true
    }

    fun exitBigRoom(chattingNumber : Int) : Boolean{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Exit Big Room")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
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
                        println("Client : Exit Big Room Success")
                        return true
                    }else{
                        println("Client : Exit Big Room Fail")
                        return false
                    }
                    break
                }
            }
        }
        return true
    }

    fun sendMessage(chattingNumber: Int, message: String) : String{
        var inputdata : HashMap<String, String> = HashMap<String, String> ()
        inputdata.put("Command", "Send Message")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
        inputdata.put("message" ,message)
        var str = ""
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
                for ( i in output.keys) {
                    println("<check> : $i, ${output[i]}")
                }
                return output["data"].toString()
            }
        }
    }

    fun requestOldMessage(chattingNumber: Int, clientNumber: Int) : ArrayList<message> {
        var inputdata : HashMap<String, String> = HashMap<String,String>()
        var result : ArrayList<message> = ArrayList<message>()
        inputdata.put("Command", "Request Old Message")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))
        while(true){
            val available = inputStream.available()
            if(available>0) {
                val dataArr = ByteArray(available)
                inputStream.read(dataArr) // byte array에 데이터를 씁니다.
                var data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                data = data.substring(1,data.length-1)
                println("CheckCheck : $data")
                val output = data.split(", ")
                    .map { it.split("=") }
                    .map { it.first() to it.last().toString() }
                    .toMap()
                for ( i in output.keys) {
                    println("please : $i , ${output[i]}")
                    if(output[i]!!.contains("/") == true) {
                        val msgData = output[i]!!.split("/")
                            .map { it.split("+") }
                            .map { it.first() to it.last().toString() }
                            .toMap()
                        var clientNum = msgData["clientNum"].toString().toInt()
                        var chatTime = msgData["chatTime"].toString()
                        var message = msgData["message"].toString()
                        var userName = msgData["userName"].toString()
                        var messageNum = i.toString().toInt()
                        result.add(0,com.example.segment.message(userName, message, chatTime, clientNum, messageNum))
                    }
                    else {
                        //pass
                    }
                }
                break
            }
        }
        return result
    }

    fun getSmallRoomList(chattingNumber: Int, clientNumber: Int) : Array<Int>{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Check Small Room")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))

        var resultArray : Array<Int> = arrayOf(0)

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

                if(output["ClientNumber"].toString().toInt() == clientNumber){
                    resultArray = arrayOf(output["SmallRoom1"].toString().toInt(),output["SmallRoom2"].toString().toInt(),output["SmallRoom3"].toString().toInt())
                }
                break
            }
        }
        return resultArray
    }

    fun createSmallRoom(chattingNumber : Int, clientNumber: Int, questionNumber: Int, message : String) : Boolean{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Create Small Room")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
        inputdata.put("QuestionNumber", questionNumber.toString())
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

    fun exitSmallRoom(chattingNumber : Int, questionNumber : Int) : Boolean{
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Exit Small Room")
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
                        println("Client : Exit Small Room Success")
                        return true
                    }else{
                        println("Client : Exit Small Room Fail")
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
        inputdata.put("ClientNumber", clientNumber.toString())
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

    fun scatterMessage( clientNumber: Int, chattingNumber: Int ) : message? {
        var inputdata : HashMap<String, String> = HashMap<String, String>()
        inputdata.put("Command", "Scatter Message")
        inputdata.put("ChattingNumber", chattingNumber.toString())
        inputdata.put("ClientNumber", clientNumber.toString())
        outputStream.write(inputdata.toString().toByteArray(Charsets.UTF_8))
        var msg : message? = null
        println("Client command : Scatter message")
        while(true) {
            val available = inputStream.available()
            if(available>0) {
                val dataArr = ByteArray(available)
                inputStream.read(dataArr) // byte array에 데이터를 씁니다.
                var data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                data = data.substring(1,data.length-1)
                //chatTime+$chatTime/userName+$userName/message+$message/clientNum+$clientNum
                println("[Scatter message] data : $data")
                val outs = data.split(", ")
                    .map { it.split("=") }
                    .map { it.first() to it.last().toString() }
                    .toMap()
                val output = outs["data"]!!.split("/")
                    .map { it.split("+") }
                    .map { it.first() to it.last().toString() }
                    .toMap()
                for ( i in output.keys ) {
                    println("[Scatter message] : $i -> ${output[i]}")
                }
                if(output["ClientNumber"].toString().toInt() == clientNumber){
                    if(output["Result"].toString() == "Success"){
                        println("Client : Scatter Message Success")
                        val userName = output["userName"].toString()
                        val chatTime = output["chatTime"].toString()
                        val message = output["message"].toString()
                        val clientNum = output["clientNum"].toString().toInt()
                        val messageNum = output["messageNum"].toString().toInt()
                        msg = com.example.segment.message(userName, message, chatTime, clientNum, messageNum)
                        println("[Scatter message] success")
                        return msg
                    }else{
                        println("Client : Scatter Message Fail")
                        return null
                    }
                    break
                }
            }
        }
    }
}