package com.example.cyworld7

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val idData = intent.getStringExtra("dataFromSignUpId")
        val passData = intent.getStringExtra("dataFromSignUpPass")
        val idText = findViewById<EditText>(R.id.loginEmail)
        val passText = findViewById<EditText>(R.id.logInpass)
        idText.setText(idData)
        passText.setText(passData)

        val sign_in_id = findViewById<EditText>(R.id.loginEmail)
        val sign_in_pass = findViewById<EditText>(R.id.logInpass)


        val btn1 = findViewById<Button>(R.id.btnLogin)
        btn1.setOnClickListener {
            val signInId = sign_in_id.text.toString()
            val signPass = sign_in_pass.text.toString()

            if (signInId.isNotEmpty() && signPass.isNotEmpty()) {

                val intent = Intent (this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this,"싸이월드에 오신걸 환영합니다.", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "아이디, 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            }
        }

        val btn2 = findViewById<Button>(R.id.btnSignUp)

        btn2.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val btngoogle = findViewById<ConstraintLayout>(R.id.btngoogle)
        btngoogle.setOnClickListener {
            val address = "https://accounts.google.com/v3/signin/identifier?authuser=0&continue=https%3A%2F%2Fwww.google.com%2F&ec=GAlAmgQ&hl=ko&flowName=GlifWebSignIn&flowEntry=AddSession&dsh=S-1089772944%3A1692081081985953"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
            startActivity(intent)

            val btnNaver = findViewById<ConstraintLayout>(R.id.btnNaver)
            btnNaver.setOnClickListener {
                val address = "https://nid.naver.com/nidlogin.login?mode=form&url=https://www.naver.com/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
                startActivity(intent)
            }
        }
    }
}