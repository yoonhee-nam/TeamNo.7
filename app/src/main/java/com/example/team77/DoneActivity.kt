package com.example.team77

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

    class DoneActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_done)

            val signUpId = intent.getStringExtra("dataFromSignUpId")
            val signUpPassValue = intent.getStringExtra("dataFromSignUpPass")


            val btn = findViewById<Button>(R.id.goToLogin)
            btn.setOnClickListener {
                val intent = Intent(this, LogInActivity::class.java)
                intent.putExtra("dataFromSignUpId",signUpId)
                intent.putExtra("dataFromSignUpPass",signUpPassValue)
                startActivity(intent)
                overridePendingTransition(R.anim.right_fade_in,R.anim.right_fade_out)
            }
        }
    }
