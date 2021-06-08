package com.example.segment

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        val DB_NAME = "user.db"
        val DB_VERSION = 1
        val TABLE_NAME = "UserDB"

        val ID = "userName"
        val PW = "userPass"
        val CLIENT_NUM = "clientNum"
        val USER_STATUS = "userStatus"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //테이블만들기
        val create_table = "create table if not exists $TABLE_NAME("+
                "$CLIENT_NUM integer primary key autoincrement, "+
                "$ID text,"+
                "$PW text,"+
                "$USER_STATUS integer);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //DB업그레이드시
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    fun signup(client: UserData):Boolean{

        //회원가입
        val values = ContentValues()
        values.put(ID, client.id)
        values.put(PW, client.pw)
        values.put(USER_STATUS, client.status)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values)>0
        db.close()
        return flag

    }

    fun login(id:String, pw:String):Boolean{
        //DB와 비교해서 user정보(id, pw) 존재하는지
        val strsql = "select * from $TABLE_NAME where userName = '$id';"
        //id로 먼저 찾고 거기있는 pw값이 받아온 pw와 일치하는지 확인하기
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)

        var flag = true

        var str = ""
        while(cursor.moveToNext()){
            str = cursor.getString(cursor.getColumnIndex(PW))
        }
        if(str!=pw){
            //비밀번호 틀림
            flag = false
        }else{
            flag = true
        }
        db.close()
        cursor.close()

        return flag
    }

}