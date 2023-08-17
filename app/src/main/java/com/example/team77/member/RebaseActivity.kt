package com.example.team77.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.team77.R

class RebaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rebase)

        val editTxtmodname = findViewById<EditText>(R.id.editTextTextPersonName2)
        val mod_Btn = findViewById<Button>(R.id.mod_Btn)
        val dbHelper = DB.getInstance(this,"member.db")
        val result = dbHelper.search(email)

        editTxtmodname.text = result

        mod_Btn.setOnClickListener {

            val email = editTxtmodname.text.toString().trim()

            val dbHelper = DB.getInstance(this,"member.db",)
            dbHelper.rebase(email)

            val modInfo = dbHelper.rebase(email)
            editTxtmodname.text =modInfo
        }
    }
}