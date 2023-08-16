package com.example.team77

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val sign_in_id = findViewById<EditText>(R.id.loginEmail)
        val sign_in_pass = findViewById<EditText>(R.id.logInpass)

        val btn1 = findViewById<Button>(R.id.btnLogin)
        btn1.setOnClickListener {
            val signInId = sign_in_id.text.toString()
            val signPass = sign_in_pass.text.toString()

            if (signInId.isNotEmpty() && signPass.isNotEmpty()) {

                val intent = Intent (this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this,"어서오세요", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "아이디, 비밀번호를 확인하세요",Toast.LENGTH_SHORT).show()
            }
        }

        val btn2 = findViewById<Button>(R.id.btnSignUp)

        btn2.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}