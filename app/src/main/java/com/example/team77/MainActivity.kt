package com.example.team77

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logout = findViewById<TextView>(R.id.txtLogout)

        logout.setOnClickListener {
            val intent = Intent( this, LogInActivity ::class.java )
            startActivity(intent)
        }

        val myPage = findViewById<Button>(R.id.btnMypage)

        myPage.setOnClickListener {
            val intent = Intent( this, MyPageActivity ::class.java )
            intent.putExtra("AddLikeCount",count)
            startActivity(intent)

        }
        val like = findViewById<LinearLayout>(R.id.layout_like)
        like.setOnClickListener(View.OnClickListener {
            like.isSelected = like.isSelected != true

            if(like.isSelected){
                count = 1
            }
            else count = 0
        })
    }
}