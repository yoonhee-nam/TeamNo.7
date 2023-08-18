package com.example.team77

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class LogInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val idData = intent.getStringExtra("dataFromSignUpId")
        val passData = intent.getStringExtra("dataFromSignUpPass")
        val idText = findViewById<EditText>(R.id.loginEmail)
        val passText = findViewById<EditText>(R.id.logInpass)
        idText.setText(idData)
        passText.setText(passData)

        val btn1 = findViewById<Button>(R.id.btnLogin)
        btn1.setOnClickListener {
            val signInId = idText.text.toString()
            val signPass = passText.text.toString()

            if (signInId.isNotEmpty() && signPass.isNotEmpty()) {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                overridePendingTransition(R.anim.up_fade_in,R.anim.up_fade_out)

            } else {
                showtoast("입력되지 않은 정보가 있습니다.")
            }
        }

        val btn2 = findViewById<Button>(R.id.btnSignUp)

        btn2.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.down_faid_in,R.anim.down_fade_out)
        }

        val btngoogle = findViewById<ConstraintLayout>(R.id.btngoogle)
        btngoogle.setOnClickListener {
            val address =
                "https://accounts.google.com/v3/signin/identifier?authuser=0&continue=https%3A%2F%2Fwww.google.com%2F&ec=GAlAmgQ&hl=ko&flowName=GlifWebSignIn&flowEntry=AddSession&dsh=S-1089772944%3A1692081081985953"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
            startActivity(intent)

            val btnNaver = findViewById<ConstraintLayout>(R.id.btnNaver)
            btnNaver.setOnClickListener {
                val address =
                    "https://nid.naver.com/nidlogin.login?mode=form&url=https://www.naver.com/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
                startActivity(intent)
            }
        }
    }
}