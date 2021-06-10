package server

import java.io.*
import java.net.*
import java.sql.*
import java.util.*

class ServerStatus( clientNumber : Int,  nickname : String,  password : String,  socket: java.net.Socket){
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

    fun login(nickname: String, password: String) : Int {
        //클라이언트에서 이름과 암호 받으면 DB에서 유저 확인하고 해당 clientnumber statement 변경하기
        var stmt : Statement? = null
        var resultset : ResultSet? = null
        var clientNum = -1  //반환할 clientNum 초기화

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from UserDB;")
            if(resultset!!.next()){
                //로그인 정보 확인
                try{
                    var dbpw = stmt!!.executeQuery("select userPass from UserDB where userName = '$nickname';")
                    conn!!.commit()
                    var flag = (dbpw.getString(1) == password)  //받아온 password와 userDB의 userPass가 일치하는지 확인한다
                    if(flag){   //아이디 패스워드 일치할 시 user status 1(접속)으로 변경
                        try{
                            stmt!!.execute("update UserDB set userStatus = 1 where userName = '$nickname';")
                            clientNum = (stmt!!.executeQuery("select clientNum from UserDB where userName = '$nickname';")).getInt(1)
                            conn!!.commit()
                        }catch (ex:SQLException){
                            println("로그인 실패")
                            ex.printStackTrace()
                        }
                    }else{
                        //아이디 패스워드 불일치
                        println("아이디와 패스워드가 일치하지 않습니다.")
                    }
                }catch(ex:SQLException){
                    // handle any errors
                    ex.printStackTrace()
                    println("User Login Fail")
                    return clientNum
                }
            }
            return clientNum
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("User Login Fail")
            return clientNum
        }
        //clientNum 반환
    }


    fun authCheck( nickname : String,  password: String,  clientNumber : Int) : Boolean {
        return true
    }

    fun logout(clientNumber:Int) : Boolean {
        //클라이언트에서 로그아웃 요청 받으면 해당 user의 clientNumber 받아서 DB에서 status 변경하기
        var stmt : Statement? = null
        var resultset : ResultSet? = null

        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("select * from UserDB;")
            if(resultset!!.next()){
                try{
                    stmt!!.execute("update UserDB set userStatus = 0 where clientNum = '$clientNumber';")   //status 0(접속X)로 변경
                    conn!!.commit()
                }catch (ex:SQLException){
                    println("로그아웃 실패")
                    ex.printStackTrace()
                }

            }
            return true
        }catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
            println("User Logout Fail")
            return false
        }
    }

    fun checkAllStatus() : Boolean {
        return true
    }

    fun scatterMessage() : Boolean {
        return true
    }
}
