package server

import java.io.*
import java.net.*
import java.sql.*
import java.util.*

class ServerUser( nickname : String,  password : String,  clientNumber : Int,  socket: java.net.Socket) {

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
            println("Success to connect with db")
        } catch (ex: SQLException) {
            println("wrong sql")
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            println("fail to connect with db")
            // handle any errors
            ex.printStackTrace()
        }
    }

    fun signup(nickname: String, password: String): Int {
        //클라이언트에서 이름과 암호 받으면 DB에 유저 정보 저장하기
        var stmt : Statement? = null
        var resultset : ResultSet? = null
        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from UserDB;")
            if(resultset!!.next()){
                try{
                    stmt!!.execute("insert into UserDB (userName, userPass, userStatus) values (\"$nickname\", $password, 1);")
                    conn!!.commit()
                }catch (ex:SQLException){
                    println("회원가입 실패")
                    ex.printStackTrace()
                }
            }

            resultset = stmt!!.executeQuery("select clientNum from UserDB where userName = \"$nickname\"")
            var clientNum = -1
            if(resultset!!.next()){
                clientNum = resultset.getInt(1)
            }
            return clientNum
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("User Signup Fail")
            return -1
        }
    }
}