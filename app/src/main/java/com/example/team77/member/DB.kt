package com.example.team77.member

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//Dao ==

class DB(context:Context, emailname:String):SQLiteOpenHelper(context,emailname,null,1) {

    // Singleton
    companion object {
        var daterbase: DB? = null
        fun getInstance(context: Context, emailname: String): DB {
            if (daterbase == null) {
                daterbase = DB(context, emailname)
            }
            return daterbase!!
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        var sql: String = "CREATE TABLE IF NOT EXISTS MEMBER( " +
                "SEQ INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "EMAIL STRING)"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {


    }

    fun insert(vo: Member) {
        var sql = " INSERT INTO MEMBER(email) " +
                " VALUES('${vo.email}')"

        var db = this.writableDatabase
        db.execSQL(sql)

    }

    fun search(email: String): String {
        var sql = " SELECT EMAIL FROM MEMBER WHERE EMAIL LIKE" +
                "'${email}'"

        var db = this.writableDatabase
        var result = db.rawQuery(sql, null)

        var str: String? = ""
        val columnIndex = result.getColumnIndex("EMAIL")

        while (result.moveToNext()) {
            str += result.getString(columnIndex)
        }

        if(str == ""){
            println("검색된 데이터가 없습니다.")
        }

        return str!!

    }

    fun delete(email: String) {
        var sql = " DELETE FROM MEMBER WHERE EMAIL =" +
                "" +
                "'${email}' "

        var db = this.writableDatabase
        db.execSQL(sql)

    }

    fun rebase(email: String) {
        var sql = " UPDATE MEMBER SET EMAIL = '${email}'"

        var db = this.writableDatabase
        db.execSQL(sql)

    }


}