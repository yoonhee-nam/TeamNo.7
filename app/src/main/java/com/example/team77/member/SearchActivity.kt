package com.example.team77.member

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.team77.R


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val editTxtname = findViewById<EditText>(R.id.editTxtname)
        val textViewinfo = findViewById<TextView>(R.id.textViewinfo)
        val findBtn = findViewById<Button>(R.id.findBtn)
        var revise = findViewById<Button>(R.id.revise)
        val delete = findViewById<Button>(R.id.delete)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.left_fade_in,R.anim.left_fade_out)
        }


        findBtn.setOnClickListener {

            val name = editTxtname.text.toString().trim()
            val dbHelper = DB.getInstance(this,"member.db",)
            val result = dbHelper.search(name)

            textViewinfo.text = result


        }

        revise.setOnClickListener {

            val email = editTxtname.text.toString().trim()

            val dbHelper = DB.getInstance(this,"member.db",)
            dbHelper.rebase(email)

            val modInfo = dbHelper.search(email)
            textViewinfo.text = modInfo
            val message = "수정되었습니다."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            finish()
            overridePendingTransition(R.anim.left_fade_in,R.anim.left_fade_out)
        }

        delete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
                .setTitle("경고") // 다이얼로그 타이틀 설정
                .setMessage("정말 삭제하시겠습니까? 삭제시 앱은 종료됩니다.") // 다이얼로그 메시지 설정
                .setPositiveButton("확인") { dialog, _ ->
                    // 확인 버튼을 클릭했을 때의 동작
                    // 다이얼로그를 띄워주기


            val email = editTxtname.text.toString().trim()
            val dbHelper = DB.getInstance(this,"member.db",)
            dbHelper.delete(email)


            val message = "삭제되었습니다."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            moveTaskToBack(true); // 태스크를 백그라운드로 이동
            finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid()); // 앱 프로세스 종료
        }
                .setNegativeButton("취소") { dialog, _ ->
                    // 취소 버튼을 클릭했을 때의 동작
                    dialog.dismiss() // 다이얼로그 닫기

    }
            // AlertDialog 인스턴스 생성
            val alertDialog = alertDialogBuilder.create()

            alertDialog.show() // AlertDialog 표시

}
    }
}