package server

import java.io.*
import java.net.*
import java.sql.*
import java.util.*

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
    val username = "root"
    val password = "hjmaharu"
    var connectionProps = Properties()



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
        return true
    }

    fun exitBigRoom( clientNumber : Int,  chattingNumber : Int) : Boolean{
        return true
    }

    fun sendMessage( clientNumber : Int,  chattingNumber : Int,  message :String) : Boolean{
        return true
    }

    fun createSmallRoom( clientNumber : Int,  message : String,  chattingNumber : Int) : Boolean{ //완료

        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from RoomDB join clients on RoomDB.chattingNum = clients.chattingNum where RoomDB.chattingNum = "+ chattingNumber.toString() + " and clients.clientNum = "+ clientNumber.toString())

            if(resultset!!.next()){
                val s1 = resultset.getObject("small1Num")
                val s2 = resultset.getObject("small2Num")
                val s3 = resultset.getObject("small3Num")
                var room = 0;
                if(s1 != null && s2 != null && s3 != null){
                    return false; //smallRoom 못만듦
                }else if(s1 == null) {
                    room = 1;
                }else if(s2 == null) {
                    room = 2;
                }else if(s3 == null) {
                    room = 3;
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

    fun requestOldMessage( clientNumber : Int,  chattingNumber : Int) : Boolean{
        return true
    }

    fun destroySmallRoom( chattingNumber : Int,  questionNumber : Int) : Boolean{ // 개발완료
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try{
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from clients where chattingNum = $chattingNumber;")
            if(resultset!!.next()){
                stmt!!.execute("delete from RoomDB where chattingNum = $questionNumber;")
                var room = questionNumber%10
                stmt!!.execute("update RoomDB set small"+ room.toString() + "Num = NULL where chattingNum = $chattingNumber;")
                conn!!.commit()
            }else{
                println("해당 소회의실은 이미 존재하지 않습니다")
                return false;
            }
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("Destroy Small Room Fail")
            return false;
        }
        return true
    }

    fun scatterMessage( chattingNumber : Int) : Boolean{
        return true
    }
}