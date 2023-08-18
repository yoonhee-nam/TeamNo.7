package com.example.team77

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat

class DetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        showtoast(getString(R.string.toast_wc))

        val mainbt = findViewById<Button>(R.id.mainbutton)
        mainbt.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_fade_in,R.anim.up_fade_out)
        }

        val loginbt = findViewById<Button>(R.id.loginbutton)
        loginbt.setOnClickListener{
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.down_faid_in,R.anim.down_fade_out)
        }

        val mypagebt = findViewById<Button>(R.id.mypagebutton)
        mypagebt.setOnClickListener{
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_fade_in,R.anim.up_fade_out)
        }

        val image = findViewById<ImageView>(R.id.imageView)
        val im = when((1..4).random()){
            1 -> R.drawable.cw_main
            2 -> R.drawable.cy_main2
            3 -> R.drawable.cy_main3
            4 -> R.drawable.cy_main4
            else -> R.drawable.cw_main
        }
        image.setImageDrawable(ResourcesCompat.getDrawable(resources, im, null))
    }
}

