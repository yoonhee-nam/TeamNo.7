package com.example.team77

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.team77.member.DB
import com.example.team77.member.Member

import java.util.regex.Pattern

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailId =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val signUpId = data?.getStringExtra("dataFromSignUpId")
                }
            }


        // 이제 emailId 변수에 데이터가 들어있음

        val sign_up_id = findViewById<EditText>(R.id.signUpId)
        val textView5 = findViewById<TextView>(R.id.textView5)
        val btnNextToPass = findViewById<Button>(R.id.btnNextToPass)

        sign_up_id.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {

                var email = sign_up_id.text.toString()
                val pattern: Pattern = Patterns.EMAIL_ADDRESS
                //pattern:email을 치면 import할수있다.
                if (pattern.matcher(email).matches()) {
                    textView5.text = "정상적인 이메일입니다."
                    textView5.setTextColor(Color.WHITE)
                    btnNextToPass.setBackgroundColor(Color.parseColor("#FF5722"))
                    btnNextToPass.isEnabled = true

                } else {
                    textView5.text = "올바른 이메일을 입력해 주세요."
                    textView5.setTextColor(Color.RED)
                    btnNextToPass.setBackgroundColor(Color.GRAY)
                    btnNextToPass.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnNextToPass.setBackgroundColor(Color.GRAY)
                btnNextToPass.isEnabled = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnNextToPass.setBackgroundColor(Color.GRAY)
                btnNextToPass.isEnabled = false
            }
        }
        )

        btnNextToPass.setOnClickListener {
            val signUpId = sign_up_id.text.toString()

            if (signUpId.isNotEmpty()) {

                val mem = Member(sign_up_id.text.toString().trim())
                val dbHelper = DB.getInstance(this, "member.db")
                dbHelper.insert(mem)

                val intent = Intent(this, PasswordActivity:: class.java)
                intent.putExtra("dataFromSignUpId",signUpId)

                emailId.launch(intent)
                startActivity(intent)

                overridePendingTransition(R.anim.right_fade_in,R.anim.right_fade_out)
            } else {
                showtoast(getString(R.string.toast_try))
            }
        }
    }
}