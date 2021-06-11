package server

import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.InetAddress
import java.net.Socket
import java.io.*
import java.net.*
import kotlin.concurrent.thread

class ServerMain(p : Int) {
    val port = p;
    var socketList : HashMap<Int, Socket> = HashMap<Int, Socket>()

    fun connect(): Unit {
        try {
            val serverSocket = ServerSocket(port)

            while(true) {
                val client = serverSocket.accept() // 서버에 접근한 클라이언트와 통신할 수 있는 소켓을 만듭니다.
                println("where")
                thread(start = true) {
                    var chatting = ServerChat()

                    var user = ServerUser("nickname", "password", 1, client)
                    var status = ServerStatus(1,"nickname","password", client) // client 추가해야함

                    chatting.init()

                    user.init()
                    status.init()

                    val clientInput = client.inputStream // 클라이언트와 연결된 inputstream
                    val clientOutput = client.outputStream // 클라이언트와 연결된 outputstream
                    while (true) {
                        val available = clientInput.available() // 데이터가 있으면 데이터의 사이즈 없다면 -1을 반환합니다.
                        if (available > 0) {
                            val dataArr = ByteArray(available) // 사이즈에 맞게 byte array를 만듭니다.
                            clientInput.read(dataArr) // byte array에 데이터를 씁니다.
                            var data = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다.
                            data = data.substring(1, data.length - 1)
                            val output = data.split(", ")
                                .map { it.split("=") }
                                .map { it.first() to it.last().toString() }
                                .toMap()

                            for (i in output.keys) {
                                println("Server : ${i} : ${output[i]}")
                            }
                            var return_value: Boolean? = null
                            var return_clientNum = -1
                            var return_status : Array<String> = arrayOf("fail","fail")
                            var inputdata: HashMap<String, String> = HashMap<String, String>()
                            inputdata.put("ClientNumber", output["ClientNumber"].toString())

                            var resultArray : Array<Int> = arrayOf(0)
                            if (output["Command"] == "Create Small Room") { // 작은방 생성 요청이 들어오면 처리
                                println("Server : Create Small Room Received")
                                return_value = chatting.createSmallRoom(
                                    output["ClientNumber"].toString().toInt(),
                                    output["message"].toString(), output["QuestionNumber"].toString().toInt(), output["ChattingNumber"].toString().toInt()
                                )
                            } else if (output["Command"] == "Enter Small Room") {
                                println("Server : Enter Small Room Received")
                                return_value = chatting.enterSmallRoom(
                                    output["ClientNumber"].toString().toInt(),
                                    output["ChattingNumber"].toString().toInt(),
                                    output["QuestionNumber"].toString().toInt()
                                )
                            } else if (output["Command"] == "Destroy Small Room") {
                                println("Server : Destroy Small Room Received")
                                return_clientNum = chatting.destroySmallRoom(
                                    output["ChattingNumber"].toString().toInt(),
                                    output["QuestionNumber"].toString().toInt(),
                                    output["ClientNumber"].toString().toInt()
                                )
                            }else if(output["Command"] == "Check Small Room"){
                                println("Server : Check Small Room")
                                resultArray= chatting.getSmallRoomList(output["ClientNumber"].toString().toInt(),output["ChattingNumber"].toString().toInt())
                                inputdata.put("Result", "Success")
                                inputdata.put("SmallRoom1", resultArray[0].toString())
                                inputdata.put("SmallRoom2", resultArray[1].toString())
                                inputdata.put("SmallRoom3", resultArray[2].toString())
                            }else if(output["Command"] == "Exit Small Room"){
                                println("Server : Exit Small Room")
                                return_value = chatting.exitSmallRoom(
                                    output["ClientNumber"].toString().toInt(),
                                    output["ChattingNumber"].toString().toInt(),
                                    output["QuestionNumber"].toString().toInt()
                                )
                            }


                            else if(output["Command"] == "User Signup"){    //회원가입 들어오면 처리하기
                                println("Server : User Signup")
                                return_clientNum = user.signup(
                                    output["UserID"].toString(),
                                    output["UserPW"].toString()
                                )
                            }else if(output["Command"] == "User Login"){    //로그인 들어오면 처리하고 clientNum 반환하기????
                                println("Server : User Login")
                                return_clientNum = status.login(
                                    output["UserID"].toString(),
                                    output["UserPW"].toString()
                                )
                            }else if(output["Command"] == "User Logout"){    //로그아웃 들어오면 처리하기
                                println("Server : User Logout")
                                return_value = status.logout(
                                    output["ClientNumber"].toString().toInt()
                                )
                            }
                            else if(output["Command"] == "Enter Big Room"){
                                println("Server : Enter Big Room")
                                return_value = chatting.enterBigRoom(output["ClientNumber"].toString().toInt(),output["ChattingNumber"].toString().toInt())
                            }else if(output["Command"] == "Exit Big Room"){
                                println("Server : Exit Big Room")
                                return_value = chatting.exitBigRoom(output["ClientNumber"].toString().toInt(),output["ChattingNumber"].toString().toInt())
                            }
                            else if(output["Command"] == "Check User Status"){
                                println("Server : Check User Status")
                                return_status = status.checkAllStatus()
                            }
                            else if(output["Command"] == "set socket list") {
                                println("Server : Set Socket List")
                                return_clientNum = output["ClientNumber"].toString().toInt()
                                socketList.put(return_clientNum , client)
                                for (i in socketList.keys) {
                                    println("Server : ${i} : ${socketList[i]}")
                                }
                            }
                            else if(output["Command"] == "Request Old Message") {
                                println("Server : Request Old Message")
                                return_clientNum = output["ClientNumber"].toString().toInt()
                                val chattingNum = output["ChattingNumber"].toString().toInt()
                                var list : HashMap<Int, String> = chatting.requestOldMessage(return_clientNum , chattingNum)
                                var index = 0
                                for ( i in list.keys) {
                                    inputdata.put(i.toString(), list[i]!!)
                                }
                            }
                            else if(output["Command"] == "Send Message") {
                                println("Server : Send Message")
                                return_clientNum = output["ClientNumber"].toString().toInt()
                                val chattingNum = output["ChattingNumber"].toString().toInt()
                                var message = output["message"].toString()
                                val msgNum : Int = chatting.sendMessage(return_clientNum, chattingNum, message)
                                if(msgNum == -1) {
                                    //cannot get index
                                    return_value = false
                                    return_clientNum = -1
                                }
                                else {
                                    return_value = true
                                    if(return_value == true) {
                                        inputdata.put("Result", "Success")
                                    }
                                    //clientOutput.write(inputdata.toString().toByteArray(Charsets.UTF_8))

                                    val map_clientNum_message : HashMap<Int, String> = chatting.scatterMessage(msgNum)
                                    println("<After scatterMessage> size : ${map_clientNum_message.size}")
                                    for ( i in map_clientNum_message.keys) {

                                        if (i.toString().toInt() == return_clientNum) {
                                            inputdata["data"] = map_clientNum_message[i].toString()
                                            println("checking the send : ${inputdata["data"]}")
                                        }
                                        else {
                                            var socket = socketList[i]
                                            if(socket!!.getOutputStream() == null ) {
                                                println("<After scatterMessage> not connect with client : $i")
                                            }
                                            else {
                                                println("<After scatterMessage> Send $i ${map_clientNum_message[i]}")
                                                var outputStream = socket!!.getOutputStream()
                                                var outData = HashMap<String, String>()
                                                outData["Command"] = "scatter message"
                                                outData["data"] = map_clientNum_message[i].toString()
                                                outputStream.write(outData.toString().toByteArray(Charsets.UTF_8))
                                            }
                                        }


                                    }
                                    //clientOutput.write(inputdata.toString().toByteArray(Charsets.UTF_8))
                                    println("<After send Message> index : $msgNum")
                                }
                            }
                            else if (output["Command"] == "Scatter Message") {
                                println("Server : Scatter Message")
                                return_clientNum = output["ClientNumber"].toString().toInt()
                                var chattingNum = output["ChattingNumber"].toString().toInt()
                                var datas : HashMap<Int, String> = HashMap<Int, String>()
                                datas = chatting.scatterMessage(chattingNum);
                                println("Server [Scatter Message] size : ${datas.size}")
                                for(i in datas.keys) {
                                    var Socket = socketList[i]
                                    var outputStream = Socket!!.getOutputStream()
                                    var outData = HashMap<String, String>()
                                    outData["Command"] = "Scatter Message"
                                    outData["ClientNumber"] = return_clientNum.toString()
                                    outData["ChattingNumber"] = chattingNum.toString()
                                    outData["data"] = datas[i].toString()
                                    for ( i in outData.keys) {
                                        println("[ScatterMessage] : $i -> ${outData[i]}")
                                    }
                                    outputStream.write(outData.toString().toByteArray(Charsets.UTF_8))
                                    println("Server [ScatterMessage] : Success to scatter message to $i")
                                }
                                return_value = true
                            }


                            if (return_value == true) {
                                inputdata.put("Result", "Success")
                                clientOutput.write(inputdata.toString().toByteArray(Charsets.UTF_8))
                                continue
                            }else if(return_value == false){
                                inputdata.put("Result", "Fail")
                                clientOutput.write(inputdata.toString().toByteArray(Charsets.UTF_8))
                                continue

                            }

                            if(return_clientNum != -1){
                                inputdata.put("ClientNum",return_clientNum.toString())
                                inputdata.put("nickname",output["UserID"].toString())
                                inputdata.put("Result", "Success")
                            }else if (return_clientNum == -1){
                                inputdata.put("nickname",output["UserID"].toString())
                                inputdata.put("Result", "Fail")
                            }
                            if(output["Command"] == "Check User Status"){
                                if(return_status[0] != "fail"){
                                    inputdata.put("ClientNum",return_clientNum.toString())
                                    inputdata.put("Result", "Success")
                                    inputdata.put("Online", return_status[0])
                                    inputdata.put("Offline", return_status[1])
                                }else{
                                    inputdata.put("ClientNum",return_clientNum.toString())
                                    inputdata.put("Result", "Fail")
                                }
                            }

                            println("check : ${inputdata["Result"]}")

                            for ( i in inputdata.keys) {
                                println("check check ehck : $i -> ${inputdata[i]}")
                            }
                            clientOutput.write(inputdata.toString().toByteArray(Charsets.UTF_8))

                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("socket connect exception start!!")
            println("e: $e")
        }
    }

    fun main() {
        connect();
    }
}