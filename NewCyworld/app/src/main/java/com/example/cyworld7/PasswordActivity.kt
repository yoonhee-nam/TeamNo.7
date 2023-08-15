package com.example.cyworld7

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.util.regex.Pattern

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val signUp2Id = findViewById<EditText>(R.id.signUp2Id)
        val signUp2Id2 = findViewById<EditText>(R.id.signUp2Id2)
        val textView3 = findViewById<TextView>(R.id.textView3)
        val btnNextTo2Pass = findViewById<Button>(R.id.btnNextTo2Pass)
        val textView6 = findViewById<TextView>(R.id.textView6)

        signUp2Id2.addTextChangedListener(object : TextWatcher {
            //addTextChangedListener = EditText가 변결 될 때마다 호출되는 리스너를 추가
            // TextWatcher = beforeTextChanged,onTextChanged,afterTextChanged 세가지 메서드를 포함 각각 변겅 전, 중 후 호출
            override fun afterTextChanged(editable: Editable?) {
                //텍스트가 변경 후 호출.
                //Editable? = 텍스트뷰의 내용을 수정할 수 있는 클래스
                val pwPattern = "^(?=.*[A-Za-z])(?=.*[$@$!%*#?&.])[A-Za-z$@$!%*#?&.]{8,20}\$"
                // 정규식 영문,특수문자 8~20
                val pw1 = signUp2Id.text.toString()
                val pw2 = editable.toString()
                val pattern = Pattern.compile(pwPattern)
                var matcher = pattern.matcher(pw1)

                if (matcher.matches()){
                    textView6.text = "안전한 비밀번호입니다."
                    textView6.setTextColor(Color.WHITE)
                }else {
                    textView6.text = "비밀번호를 확인해주세요."
                    textView6.setTextColor(Color.RED)
                }


                if (pw1 == pw2 && matcher.matches()) {
                    textView3.text = "비밀번호가 일치합니다."
                    textView3.setTextColor(Color.WHITE)
                    btnNextTo2Pass.setBackgroundColor(Color.parseColor("#FF5722"))
                    btnNextTo2Pass.isEnabled = true

                } else {
                    textView3.text = "비밀번호를 확인해주세요."
                    textView3.setTextColor(Color.RED)
                    btnNextTo2Pass.setBackgroundColor(Color.GRAY)
                    btnNextTo2Pass.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 변경 되기 전에 호출 이전의텍스트'p0', 변경될 텍스트의 시작위치 'p1' 변경될 텍스트의 길이'p2'
                // 변경될 텍스트로 바뀌기 전의 텍스트의 길이'p3'
                // CharSequence 문자열 인터페이스 ?로 Nullable

                btnNextTo2Pass.setBackgroundColor(Color.GRAY)
                btnNextTo2Pass.isEnabled = false
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                btnNextTo2Pass.setBackgroundColor(Color.GRAY)
                btnNextTo2Pass.isEnabled = false
                    }
            }
        )


        val signUpId = intent.getStringExtra("dataFromSignUpId")
        val signUpPass = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val signUpPassValue = data?.getStringExtra("dataFromSignUpPass")

                // 이제 signUpPassValue 변수에 데이터가 들어있음
            }
        }

        val sign_up_pass = findViewById<EditText>(R.id.signUp2Id)

        val btn = findViewById<Button>(R.id.btnNextTo2Pass)
        btn.setOnClickListener {
            val signUpPassValue = sign_up_pass.text.toString()

            if (signUpPassValue.isNotEmpty()) {
                val intent = Intent(this, DoneActivity::class.java)
                intent.putExtra("dataFromSignUpPass", signUpPassValue)
                intent.putExtra("dataFromSignUpId",signUpId)
                signUpPass.launch(intent) // 이 부분을 삭제하지 않음
            } else {
                Toast.makeText(this, "비밀번호를 다시 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }
}