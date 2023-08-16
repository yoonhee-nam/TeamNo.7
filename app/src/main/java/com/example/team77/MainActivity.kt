package com.example.team77

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logout = findViewById<TextView>(R.id.txtLogout)

        logout.setOnClickListener {
            intent = Intent( this, LogInActivity ::class.java )
            startActivity(intent)
        }

        val myPage = findViewById<Button>(R.id.btnMypage)

        myPage.setOnClickListener {
            intent = Intent( this, MyPageActivity ::class.java )
            startActivity(intent)
        }


    }
}