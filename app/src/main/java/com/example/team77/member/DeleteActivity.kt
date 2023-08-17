package com.example.team77.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.team77.R

class DeleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        val editTxtdelname = findViewById<EditText>(R.id.editTextTextPersonName2)
        val deleteBtn = findViewById<Button>(R.id.mod_Btn2)

        deleteBtn.setOnClickListener {

            val email = editTxtdelname.text.toString().trim()
            val dbHelper = DB.getInstance(this,"member.db",)
            dbHelper.delete(email)
        }
    }
}