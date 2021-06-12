package server

import java.io.*
import java.net.*
import java.sql.*
import java.util.*
import javax.swing.plaf.nimbus.State
import kotlin.collections.HashMap

//clientNumber : Int, chattingNumber : Int,  questionNumber : Int,  socket: java.net.Socket

//stmt!!.execute("insert into UserDB(userName, userPass, clientNum, userStatus) Values(\"HI\", \"1234\", 100, 1);")
//conn!!.commit()

//while (resultset!!.next()) {
//    val d = resultset.findColumn("small1Num")
//    println(d)
//    println(resultset.getInt("small1Num"))
//}

class ServerChat(){
    var conn : Connection? = null
    val username = "#####"
    val password = "########"
    var connectionProps = Properties()
    val limit = 10
    var now = -1



    fun init() : Unit{
        connectionProps = Properties()
        connectionProps.put("user", username)
        connectionProps.put("password", password)
        val connection_str = "jdbc:" + "mysql" + "://" +"127.0.0.1" + ":" + "3306" + "/" + "segment?useUnicode=true&characterEncoding=utf8"
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection(connection_str,connectionProps)
            conn!!.setAutoCommit(false)

        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }

    fun showList() : Unit{
    }

    fun enterBigRoom( clientNumber : Int,  chattingNumber : Int) : Boolean {
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from clients where chattingNum = $chattingNumber and clientNum = $clientNumber;")

            if(!resultset!!.next()){
                stmt!!.execute("insert into clients(chattingNum, clientNum) Values($chattingNumber, $clientNumber);")
                conn!!.commit()
            }else{
                println("해당 client는 이미 해당 큰 채팅방에 속해있습니다")
                return false;
            }
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Enter Big Room Fail")
            return false;
        }
        return true
    }

    fun exitBigRoom( clientNumber : Int,  chattingNumber : Int) : Boolean{
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from clients where chattingNum = $chattingNumber and clientNum = $clientNumber;")
            if(resultset!!.next()){
                stmt!!.execute("delete from clients where chattingNum = $chattingNumber and clientNum = $clientNumber;")
                conn!!.commit()
            }else{
                println("해당 대회의실에 해당 클라이언트는 이미 존재하지 않습니다")
                return false;
            }
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Exit Small Room Fail")
            return false;
        }
        return true
    }

    fun sendMessage( clientNumber : Int,  chattingNumber : Int,  message :String) : Int{
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try {
            stmt = conn!!.createStatement()
            //println("insert into message(chattingNum, clientNum, message) values ($chattingNumber, $clientNumber, \"$message\");")
            stmt!!.execute("insert into message(chattingNum, clientNum, message) values ($chattingNumber, $clientNumber, \"$message\");")
//            return true
            conn!!.commit()

            println("success to insert")
            println("sql : select messageNum from message where chattingNum = $chattingNumber and clientNum = $clientNumber and message = \"$message\" order by messageNum desc limit 1;")
            resultset = stmt!!.executeQuery("select messageNum from message where chattingNum = $chattingNumber and clientNum = ${clientNumber} and message = \"$message\" order by messageNum desc limit 1;")
            if(resultset!!.next() ) {
                val index = resultset.getObject("messageNum").toString().toInt()
                if(index == null ){
                    return -1
                }
                else {
                    println("<sendMessage Success> messageNum = $index")
                    return index
                }
            }
        } catch ( e: Exception) {
            println("[ERROR] sendMessage")
            println("e : ${e.message}")
            return -1
        }
        return -1
    }

    fun getSmallRoomList(clientNumber: Int, chattingNumber: Int) : Array<Int>{
        var stmt : Statement? = null
        var resultset : ResultSet? = null
        var resultArray : Array<Int> = arrayOf(0)

        try{
            stmt = conn!!.createStatement()
            //println("select * from RoomDB join clients on RoomDB.chattingNum = clients.chattingNum where RoomDB.chattingNum = "+ chattingNumber.toString() + " and clients.clientNum = "+ clientNumber.toString())
            resultset = stmt!!.executeQuery("select * from RoomDB join clients on RoomDB.chattingNum = clients.chattingNum where RoomDB.chattingNum = "+ chattingNumber.toString() + " and clients.clientNum = "+ clientNumber.toString())

            if(resultset!!.next()) {
                var s1 = resultset.getObject("small1Num")
                var s2 = resultset.getObject("small2Num")
                var s3 = resultset.getObject("small3Num")

                if(s1 == null){
                    s1 = -1
                }
                s1 = s1.toString().toInt()
                if(s2 == null){
                    s2 = -1
                }
                s2 = s2.toString().toInt()
                if(s3==null){
                    s3 = -1
                }
                s3 = s3.toString().toInt()
                resultArray = arrayOf(s1,s2,s3)
            }
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Create Small Room Fail")
        }

        return resultArray
    }

    fun createSmallRoom( clientNumber : Int,  message : String, questionNumber: Int, chattingNumber : Int) : Boolean{ //완료

        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from RoomDB join clients on RoomDB.chattingNum = clients.chattingNum where RoomDB.chattingNum = "+ chattingNumber.toString() + " and clients.clientNum = "+ clientNumber.toString())

            if(resultset!!.next()){
                val s1 = resultset.getObject("small1Num")
                val s2 = resultset.getObject("small2Num")
                val s3 = resultset.getObject("small3Num")
                var room = questionNumber;
                if(s1 == room || s2 == room || s3 == room){
                    return false
                }else{
                    room = room%10
                }

                if(room >= 1 && room <= 3){
                    val smallRoom = 10*chattingNumber + room
                    try {
                        stmt!!.execute("update RoomDB set small"+ room.toString() +"Num = $smallRoom where chattingNum = $chattingNumber;")
                        stmt!!.execute("insert into RoomDB(chattingNum, bigChatNum, notice) values($smallRoom, $chattingNumber, \"$message\");")
                        stmt!!.execute("insert into clients(chattingNum, clientNum) Values($smallRoom, $clientNumber);")
                        conn!!.commit()
                    }catch(ex : SQLException){
                        println("SmallRoom 생성 실패")
                        ex.printStackTrace()
                    }
                    return true;
                }
                return false;
            }

        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Create Small Room Fail")
            return false;
        }
        return true
    }

    fun enterSmallRoom( clientNumber : Int,  chattingNumber : Int,  questionNumber : Int ) : Boolean{ //개발 완료
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from clients where chattingNum = $chattingNumber and clientNum = $clientNumber;")

            if(resultset!!.next()){
                resultset = stmt!!.executeQuery("select * from clients where chattingNum = $questionNumber and clientNum = $clientNumber;")
                if(!resultset!!.next()){
                    stmt!!.execute("insert into clients(chattingNum, clientNum) Values($questionNumber, $clientNumber);")
                    conn!!.commit()
                }else{
                    println("해당 client는 이미 소채팅방에 속해있습니다.")
                    return false;
                }
            }else{
                println("해당 client는 소채팅방에 해당하는 큰 채팅방에 속해있지 않습니다")
                return false;
            }
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Enter Small Room Fail")
            return false;
        }
        return true
    }

    fun requestOldMessage( clientNumber : Int,  chattingNumber : Int) :HashMap<Int, String>{
        var stmt : Statement ? =null
        var resultset : ResultSet? = null
        var result = HashMap<Int, String>()

        try {
            stmt = conn!!.createStatement()

            if ( now == -1) {
                //select messageNum from message where chattingNum = 1 order by messageNum desc limit 1;
                resultset = stmt!!.executeQuery("select messageNum from message where chattingNum = $chattingNumber order by messageNum desc limit 1;")
                if(resultset!!.next()) {
                    var size = resultset.getObject("messageNum")
                    if(size == null ){
                        now = 0
                    }
                    else {
                        now = size.toString().toInt()
                    }
                }
            }

            //select * from message, UserDB where UserDB.clientNum = message.clientNum and message.messageNum <= $now and message.chattingNum = $chattingNumber order by message.messageNum desc limit $limit;
            resultset = stmt!!.executeQuery("select * from message, UserDB where UserDB.clientNum = message.clientNum and message.messageNum <= $now and message.chattingNum = $chattingNumber order by message.messageNum desc limit $limit;")
            var index=0;
            var str =""
            while(resultset!!.next()) {
                var chatTime = resultset.getObject("chatTime").toString()
                var userName = resultset.getObject("userName").toString()
                var clientNum = resultset.getObject("clientNum").toString()
                var message = resultset.getObject("message").toString()
                var messageNum = resultset.getObject("messageNum").toString()
                str = "chatTime+$chatTime/userName+$userName/message+$message/clientNum+$clientNum"
                result.put(messageNum.toInt(), str)
                now = messageNum.toInt()
            }
            now-=1
//            println("Server : Succes to request old message and now = $now")
            return result;
        }catch(e : Exception) {
            return result
        }
    }

    fun exitSmallRoom(clientNumber: Int, chattingNumber: Int, questionNumber: Int) : Boolean{
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from clients where chattingNum = $questionNumber and clientNum = $clientNumber;")
            if(resultset!!.next()){
                stmt!!.execute("delete from clients where chattingNum = $questionNumber and clientNum = $clientNumber;")
                conn!!.commit()
            }else{
                println("해당 소회의실에 해당 클라이언트는 이미 존재하지 않습니다")
                return false;
            }
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Exit Small Room Fail")
            return false;
        }
        return true
    }

    fun destroySmallRoom( chattingNumber : Int,  questionNumber : Int, clientNumber: Int) : Int{ // 개발완료
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from clients where chattingNum = $chattingNumber;")
            if(resultset!!.next()) {
                resultset = stmt!!.executeQuery("select * from clients where chattingNum = $questionNumber;")
                var r = resultset.row
                var row = 0;
                while(resultset!!.next()){
                    row+=1
                }
                if (row> 1) {
                    return -1
                }else {
                    stmt!!.execute("delete from RoomDB where chattingNum = $questionNumber;")
                    var room = questionNumber % 10
                    stmt!!.execute("update RoomDB set small" + room.toString() + "Num = NULL where chattingNum = $chattingNumber;")
                    conn!!.commit()
                }

            }else{
                println("해당 소회의실은 이미 존재하지 않습니다")
                return -1;
            }
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Destroy Small Room Fail")
            return -1;
        }
        return clientNumber
    }

    fun scatterMessage( msgNum : Int ) : HashMap<Int, String>{
        var stmt : Statement? = null
        var resultset : ResultSet? = null
        var messages : HashMap<Int, String> = HashMap<Int, String>()
        try {
            stmt = conn!!.createStatement()
            //SELECT * FROM "TABLE NAME" ORDER BY "COLUMN NAME" DESC LIMIT 1
            //println("select * from message order by messageNum where chattingNum = $chattingNumber desc limit 1")
            // select * from message, UserDB order by messageNum where message.chattingNum=1 and message. desc limit 1
            //select * from UserDB, clients, message where message.chattingNum = $chattingNumber and clients.chattingNum = $chattingNumber and UserDB.userStatus = 1 order by message.messageNum desc limit 1
            var sql = "select s1D.chattingNum, s1D.message, s1D.chatTime, s2D.userName, s2D.clientNum from message as s1D join (select uD.clientNum, uD.userName, cD.chattingNum from UserDB as uD join clients as cD on uD.clientNum = cD.clientNum where uD.userStatus = 1) as s2D on s1D.chattingNum = s2D.chattingNum where s1D.messageNum = $msgNum;"
            //println("Server [ Scatter Message ] sql : $sql")
            resultset = stmt!!.executeQuery(sql)
            var index=0;

            while(resultset!!.next()) {
                println("Server : ScatterMessage dd")
                var chatTime = resultset.getObject("chatTime").toString()
                var clientNum = resultset.getObject("clientNum").toString()
                var message = resultset.getObject("message").toString()
                var userName = resultset.getObject("userName").toString()
                var messageNum = msgNum
                var str = "chatTime+$chatTime/userName+$userName/message+$message/clientNum+$clientNum/messageNum+$messageNum"
                println("Check : $str")
                messages[clientNum.toInt()] = str
                println("Server : scatterMessage success")
            }
            return messages
        } catch ( e : Exception ) {
            return HashMap<Int, String>()
        }
    }
}
