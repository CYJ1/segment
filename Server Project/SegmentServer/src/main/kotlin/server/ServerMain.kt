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

    fun connect(): Unit {
        try {
            val serverSocket = ServerSocket(port)

            while(true) {
                val client = serverSocket.accept() // 서버에 접근한 클라이언트와 통신할 수 있는 소켓을 만듭니다.
                thread(start = true) {
                    var chatting = ServerChat()

                    var user = ServerUser("nickname", "password", 1, client)
                    var status = ServerStatus(1,"nickname","password",client)

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



                            if (return_value == true) {
                                inputdata.put("Result", "Success")
                            }else if(return_value == false){
                                inputdata.put("Result", "Fail")
                            }

                            if(return_clientNum != -1){
                                inputdata.put("ClientNum",return_clientNum.toString())
                                inputdata.put("nickname",output["UserID"].toString())
                                inputdata.put("Result", "Success")
                            }else if (return_clientNum == -1){
                                inputdata.put("nickname",output["UserID"].toString())
                                inputdata.put("Result", "Fail")
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
