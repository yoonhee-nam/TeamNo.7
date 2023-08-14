package com.example.team77

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val message = "당신의 가능성에 코딩을 곱해보세요. \n 스파르타 코딩클럽"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}