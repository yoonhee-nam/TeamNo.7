package com.example.team77

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.team77.member.RebaseActivity
import com.example.team77.member.SearchActivity
import kotlin.random.Random

class MyPageActivity : AppCompatActivity() {

    val icons = arrayOf(
        R.drawable.computer, R.drawable.love, R.drawable.money, R.drawable.smile, R.drawable.star
    )
    val iconTexts = arrayOf(
        "코딩 중 ..", "사랑", "돈벌자!", "행복" ,"반짝반짝"
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val miniroom = findViewById<Button>(R.id.miniroom)

        miniroom.setOnClickListener {
            intent = Intent(this, DetailActivity ::class.java)
            startActivity(intent)
        }

        val message = "당신의 가능성에 코딩을 곱해보세요. \n 스파르타 코딩클럽"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        var iconImage= findViewById<ImageView>(R.id.icon)
        var iconText = findViewById<TextView>(R.id.icon_text)

        val randomIndex = Random.nextInt(icons.size)
        iconImage.setImageResource(icons[randomIndex])
        iconText.text = iconTexts[randomIndex]

        val setting = findViewById<Button>(R.id.btnsetting)
        setting.setOnClickListener {


            intent = Intent(this, SearchActivity:: class.java)
            startActivity(intent)
        }

    }
}