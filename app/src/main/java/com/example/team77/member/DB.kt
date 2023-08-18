package com.example.team77.member

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DB(context: Context, emailname: String) : SQLiteOpenHelper(context, emailname, null, 1) {

    companion object {
        var database: DB? = null

        fun getInstance(context: Context, emailname: String): DB {
            if (database == null) {
                database = DB(context, emailname)
            }
            return database!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS MEMBER( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                "EMAIL STRING)"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Database upgrade logic (if needed) can be implemented here
    }

    fun insert(vo: Member) {
        val sql = "INSERT INTO MEMBER(email) VALUES('${vo.email}')"

        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun search(email: String): String {
        val sql = "SELECT EMAIL FROM MEMBER WHERE EMAIL LIKE '%$email%'"

        val db = this.writableDatabase
        val result = db.rawQuery(sql, null)

        var str: String? = ""
        val columnIndex = result.getColumnIndex("EMAIL")

        while (result.moveToNext()) {
            str += result.getString(columnIndex)
        }

        if (str == "") {
            println("검색된 데이터가 없습니다.")
        }

        return str!!
    }

    fun delete(email: String) {
        val sql = "DELETE FROM MEMBER WHERE EMAIL = '$email'"

        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun rebase(email: String) {
        val sql = "UPDATE MEMBER SET EMAIL = '$email'"

        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun checkIdExist(email: String): Boolean {
        val db = this.readableDatabase

        val projection = arrayOf("EMAIL") // 컬럼 이름 변경
        val selection = "EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            "MEMBER",   // 테이블
            projection, // 리턴 받고자 하는 컬럼
            selection,  // where 조건
            selectionArgs,  // where 조건에 해당하는 값의 배열
            null,       // 그룹 조건
            null,       // having 조건
            null        // orderby 조건 지정
        )

        return cursor.count > 0
    }

    fun logIn(email: String): Boolean {
        val db = this.readableDatabase

        val projection = arrayOf("EMAIL") // 컬럼 이름 변경
        val selection = "EMAIL = ?"
        val selectionArgs = arrayOf(email) // 일단 비밀번호까지를 함께 검사하도록 가정

        val cursor = db.query(
            "MEMBER",   // 테이블
            projection, // 리턴 받고자 하는 컬럼
            selection,  // where 조건
            selectionArgs,  // where 조건에 해당하는 값의 배열
            null,       // 그룹 조건
            null,       // having 조건
            null        // orderby 조건 지정
        )

        return cursor.count > 0
    }
}