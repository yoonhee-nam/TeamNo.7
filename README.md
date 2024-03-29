# TeamNo.7
팀원 : 남윤희 임주리 남소진 황수연 이주용

깃허브 : https://github.com/yoonhee-nam/TeamNo.7

팀노션 : https://teamsparta.notion.site/7-ed265b56330b4aaa9549b0e00fbcea1a

![image](https://github.com/yoonhee-nam/TeamNo.7/assets/126261375/07aec1fb-2994-4cb2-99a0-c87e09d36e48)

#  파일 목록

##  Package[member]

###  DB.kt

DB클래스, emailname을 String자료형으로 받고, SQLiteOpenHelper 클래스 import후 상속받아 데이터베이스를 생성합니다.
onCreate에서 MEMEBER라는 테이블을 생성한뒤 insert에서 입력한 email정보를 받아와 데이터베이스에 저장합니다.
search로 특정 이메일을 검색하고 delete에서 입력된 이메일을 가진 데이터를 삭제합니다. rebase에서 입력된 이메일의 회원정보를 수정하는 역할을 합니다.
checkIdExist에서 입력된 이메일이 저장소에 있는지 확인하는 함수입니다.


```
package com.example.team77.member

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DB(context: Context, emailname: String) : SQLiteOpenHelper(context, emailname, null, 1) {

    companion object {
        var database: DB? = null

        fun getInstance(context: Context, emailname: String): DB {
            if (database == null) {
                database = DB(context, emailname)
            }
            return database!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS MEMBER( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                "EMAIL STRING)"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Database upgrade logic (if needed) can be implemented here
    }

    fun insert(vo: Member) {
        val sql = "INSERT INTO MEMBER(email) VALUES('${vo.email}')"

        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun search(email: String): String {
        val sql = "SELECT EMAIL FROM MEMBER WHERE EMAIL LIKE '%$email%'"

        val db = this.writableDatabase
        val result = db.rawQuery(sql, null)

        var str: String? = ""
        val columnIndex = result.getColumnIndex("EMAIL")

        while (result.moveToNext()) {
            str = result.getString(columnIndex)
        }

        if (str == "") {
            println("검색된 데이터가 없습니다.")
        }

        return str!!
    }

    fun delete(email: String) {
        val sql = "DELETE FROM MEMBER WHERE EMAIL = '$email'"

        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun rebase(email: String) {
        val sql = "UPDATE MEMBER SET EMAIL = '$email'"

        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun checkIdExist(email: String): Boolean {
        val db = this.readableDatabase

        val projection = arrayOf("EMAIL") // 컬럼 이름 변경
        val selection = "EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            "MEMBER",   // 테이블
            projection, // 리턴 받고자 하는 컬럼
            selection,  // where 조건
            selectionArgs,  // where 조건에 해당하는 값의 배열
            null,       // 그룹 조건
            null,       // having 조건
            null        // orderby 조건 지정
        )

        return cursor.count > 0
    }
}
```

### Member.kt
email정보를 추가할 때 Member 클래스의 객체를 생성하고 이메일 정보를 넣어 초기화한 다음, 
DB 클래스의 insert() 메서드를 사용하여 데이터베이스에 회원 정보를 추가합니다.

```
package com.example.team77.member

class Member(var email: String){

}

###SearchActivity.kt
email확인,수정,삭제 역할을 하는 페이지입니다.모든 페이지에 overridePendingTransition로 주어 화면 전환 시 애니메이션 효과를 주었습니다.
findBtn버튼 클릭 시 search함수를 으로 DB에서 해당 값을 찾은 후 rebase함수로 입력한 정보를 수정하고 delete함수로 삭제기능을 추가하고 다이얼로그를 통해 다시 한번 되물은 후 확인 버튼 클릭 시
앱을 강제종료합니다.

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
```

```
### BaseActivity.kt 


import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showtoast(inputText: String) {

        val inflater = layoutInflater
        val layout =
            inflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_layout))
        val text = layout.findViewById<TextView>(R.id.me)
        text.text = inputText

        with(Toast(applicationContext)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}
```

### DetailActivity.kt

가로/세로 모드 UI 구현했습니다.
화면이 바뀔 때마다 캐릭터 이미지도 변경이 되도록 랜덤값을 주었습니다.
my page, main page, login page로 이동할 수 있도록 버튼의 기능을 주었습니다.

```
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
```

### DoneActivity.kt
회원가입 완료 창으로, 버튼을 누르면  intent.putExtra로 아이디와 패스워드를 각 저장소에 담아 LogInActivity로 넘깁니다.

```
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

    class DoneActivity : BaseActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_done)

            showtoast(getString(R.string.toast_wc))

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
```

### LoginActivity.kt

intent.getStringExtra로 id와password를 받아옵니다. val pwPattern = "^(?=.*[A-Za-z])(?=.*[$@$!%*#?&.])[A-Za-z$@$!%*#?&.]{8,20}\$"로 비밀번호 정규식을 이용하여 제한을 걸었고,
이메일도 pattern: Pattern = Patterns.EMAIL_ADDRESS를 통해 이메일 형식만 기재할 수 있도록 했습니다. val db = DB.getInstance(this, "your_email_name_here")로 DB를 받아오고
if (db.checkIdExist(emailId) 으로 존재여부를 판단하빈다.
구글 및 네이버 로그인 버튼 클릭 리스너로 각 로그인 화면으로 이동하는 것을  Intent.ACTION_VIEW로 이동 만 구현했습니다.

```
    import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.team77.member.DB
import java.util.regex.Pattern

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

            val pwPattern = "^(?=.*[A-Za-z])(?=.*[$@$!%*#?&.])[A-Za-z$@$!%*#?&.]{8,20}\$"
            val emailId = idText.text.toString()
            val signPass = passText.text.toString()
            val pattern: Pattern = Patterns.EMAIL_ADDRESS
            val pattern2 = Pattern.compile(pwPattern)
            val matcher = pattern2.matcher(signPass)


            if (emailId.isNotEmpty() && signPass.isNotEmpty() && pattern.matcher(emailId).matches() && matcher.matches()) {
                val db = DB.getInstance(this, "your_email_name_here")
                if (db.checkIdExist(emailId)) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.up_fade_in,R.anim.up_fade_out)
                } else {
                    showtoast("존재하지 않는 계정입니다.")
                }

            } else {
                showtoast(getString(R.string.toast_noinform_text))
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
```

### MainActivity.kt
#### 1.로그아웃 버튼 
로그아웃 버튼 클릭시 LogInActivyty로 이동 intent

#### 2. MYPAGE로 이동 버튼
MYPAGE로 이동 버튼 클릭시 MyPageActivyty로 이동 intet

#### 3. like
Like 클릭시 MyPage Like 101로, 취소시 100으로 구현 

```
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


class MainActivity : BaseActivity() {

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showtoast(getString(R.string.toast_wcto_text))


        val logout = findViewById<TextView>(R.id.txtLogout)
        logout.setOnClickListener {
            val intent = Intent( this, LogInActivity ::class.java )
            startActivity(intent)

            overridePendingTransition(R.anim.left_fade_in,R.anim.left_fade_out)
        }

        val myPage = findViewById<Button>(R.id.btnMypage)

        myPage.setOnClickListener {

            val intent = Intent( this, MyPageActivity ::class.java )
            intent.putExtra("AddLikeCount",count)
            startActivity(intent)

            overridePendingTransition(R.anim.right_fade_in,R.anim.right_fade_out)

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
```

### MyPageActivity.kt

## 주요 기능

### 1. 비쥐엠 (BGM) 기능

MyPage 앱은 사용자가 자신의 마이페이지를 방문할 때 BGM을 재생할 수 있는 기능을 제공합니다. 재생과 다음곡,이전곡 등 다양한 곡을 감상하며 마이페이지를 더욱 특별하게 꾸밀 수 있습니다.

### 2. 아이콘 랜덤 변경

마이페이지의 아이콘은 매번 새로운 아이콘으로 랜덤으로 변경됩니다. 이를 통해 사용자의 마이페이지가 매번 다르게 보이며, 더욱 다채로워집니다.

### 3. 더보기 기능

마이페이지에는 더보기 기능이 구현되어 있습니다. 사용자는 마이페이지에서 자신의 이야기를 더 자세히 나눌 수 있고, 2줄 이상 적혀있을시 더보기 기능이 동작됩니다.

### 4. TOday 기능

MainPage 에서 MyPage 로 넘어오면 Today 수가 증가합니다. 이를 통해 몇명의 방문객이 마이페이지를 방문했는지 확인하실 수 있습니다.


### 5. 좋아요 기능

MainPage 에서 좋아요 버튼을 누르면 MyPage 에서 받아옵니다. 좋아요를 통해 사용자 간의 소통을 원활하게 할 수 있습니다.

```
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.team77.member.SearchActivity

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import kr.co.prnd.readmore.ReadMoreTextView

import kotlin.random.Random


class MyPageActivity : BaseActivity() {

    val icons = arrayOf(
        R.drawable.computer, R.drawable.love, R.drawable.money, R.drawable.smile, R.drawable.star
    )
    val iconTexts = arrayOf(
        "코딩 중 ..", "사랑", "돈벌자!", "행복", "반짝반짝"
    )

    val currentCount = 100


    var todayCount = 0
    lateinit var count: TextView // 지연초기화

    var mediaPlayer: MediaPlayer? = null

    private lateinit var playButton: AppCompatImageButton
    private lateinit var nextButton: AppCompatImageButton
    private lateinit var stopButton: AppCompatImageButton
    private lateinit var beforeButton: AppCompatImageButton

    private val songs = listOf(R.raw.candy, R.raw.difference, R.raw.izi, R.raw.y)
    private var songIndex = 0
    private val songtitle = listOf(
        "NCT DREAM - CANDY",
        "윤도현 - 사랑했나봐 (difference) ",
        "izi - 응급실",
        "프리스타일 - Y "
    )


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val miniroom = findViewById<Button>(R.id.miniroom)

        miniroom.setOnClickListener {
            intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.up_fade_in,R.anim.up_fade_out)
        }

        showtoast(getString(R.string.toast_youcan))

        var iconImage = findViewById<ImageView>(R.id.icon)
        var iconText = findViewById<TextView>(R.id.icon_text)

        val randomIndex = Random.nextInt(icons.size)
        iconImage.setImageResource(icons[randomIndex])
        iconText.text = iconTexts[randomIndex]


        val setting = findViewById<ImageView>(R.id.btnsetting)
        setting.setOnClickListener {


            intent = Intent(this, SearchActivity:: class.java)
            startActivity(intent)
        }

        count = findViewById(R.id.count)

        todayCount++
        count.text = todayCount.toString()

        var team_text = findViewById<ReadMoreTextView>(R.id.team_text)
        team_text.text =
            "안녕하세요 저는 7조 황수연 입니다. 싸이월드 하면 추억의 곡이죠, 오늘의 추천 곡은 엔시티드림의 캔디 입니다. (HOT - Candy)"

        var team_text2 = findViewById<ReadMoreTextView>(R.id.team_text2)
        team_text2.text =
            "사실은 오늘 너와의 만남을 정리하고싶어~ 널 만날거야 이런 날 이해해 어렵게 맘 정한거라 네게 말할거지만 사실 오늘 아침에 그냥 나 생각한거야 "

        var team_text3 = findViewById<ReadMoreTextView>(R.id.team_text3)
        team_text3.text =
            "햇살에 일어나보니 너무나 눈부셔 모든게 다 변한거야 널 향한 마음도 그렇지만 널 사랑 않는게 아냐 이제는 나를 변화 시킬테니까~ 머리 위로 비친 내 하늘 바라다보며 널 향한 마음을 이제 나 굳혔지만 "

        var team_text4 = findViewById<ReadMoreTextView>(R.id.team_text4)
        team_text4.text =
            "단지 널 사랑해~ 이렇게 말했지~ 이제껏 준비했던 많은 말을 뒤로 한채 언제나 니 옆에 있을게 이렇게 약속을 하겠어 저 하늘을 바라다보며 ~ 캔디!"



        playButton = findViewById(R.id.play)
        stopButton = findViewById(R.id.pause)
        nextButton = findViewById(R.id.next)
        beforeButton = findViewById(R.id.before)

// play stop before next  버튼 구현 

        playButton.setOnClickListener {
            mediaPlayer?.release() //release previous media players
            mediaPlayer = MediaPlayer.create(this@MyPageActivity, songs[songIndex])
            mediaPlayer?.start()
            val songtext = findViewById<TextView>(R.id.bgmtitle)
            songtext.text = songtitle[songIndex]
        }
        stopButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }

        beforeButton.setOnClickListener {
            songIndex = (songIndex - 1 + songs.size) % songs.size
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this@MyPageActivity, songs[songIndex])
            mediaPlayer?.start()
            val songtext = findViewById<TextView>(R.id.bgmtitle)
            songtext.text = songtitle[songIndex]
        }

        nextButton.setOnClickListener {
            songIndex = (songIndex + 1 + songs.size) % songs.size
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this@MyPageActivity, songs[songIndex])
            mediaPlayer?.start()
            val songtext = findViewById<TextView>(R.id.bgmtitle)
            songtext.text = songtitle[songIndex]
        }



//메인에서 좋아요 버튼 클릭 시 like 1증가
        val count2 = findViewById<TextView>(R.id.like_count)
        val likeCount = intent.getIntExtra("AddLikeCount",0)
        val text = currentCount + likeCount
        count2.text = text.toString()

    }
}
```

### PasswordActivity.kt
addTextChangedListener = EditText가 변결 될 때마다 호출되는 리스너를 추가하고
TextWatcher로 비밀번호 형식 및 일치 여부를 확인하여 상황에 따라 UI를 변경합니다.
beforeTextChanged,onTextChanged,afterTextChanged 세가지 메서드를 포함 각각 변겅 전, 중 후 상태를 변경할 수 있습니다.
matcher함수로 비밀번호를 검사합니다.식을 검증합니다. 입력된 비밀번호와 비교하여 형식이 맞고 일치하면, 해당 결과를 TextView에 표시하고, 버튼의 활성화 상태 및 배경색을 변경합니다.

```
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class PasswordActivity : BaseActivity() {
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
                val matcher = pattern.matcher(pw1)

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
                overridePendingTransition(R.anim.right_fade_in,R.anim.right_fade_out)
            } else {
                showtoast(getString(R.string.toast_pass_text))
            }
        }

    }
}
```

### SignUpActivity.kt


```
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
```

### layout-land -> detail_activity.xml

가로/세로 모드 UI 구현 중 가로 모드에 해당되는 xml 파일입니다.


```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    tools:context=".DetailActivity">

    <ImageView
        android:id="@+id/back"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/back" />


    <ImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="300dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintVertical_bias="0.8"
        app:srcCompat="@drawable/cw_main" />

    <Button
        android:id="@+id/mainbutton"
        android:layout_width="wrap_content"
        android:layout_height="200dp"
        android:layout_marginStart="30dp"
        android:background="@color/button"
        android:fontFamily="@font/font2"
        android:rotation="-27"
        android:text="@string/mainbutton_msg"
        android:textColor="@color/pink"
        android:textSize="15sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/mypagebutton"
        android:layout_width="wrap_content"
        android:layout_height="200dp"
        android:layout_marginEnd="30dp"
        android:layout_marginBottom="20dp"
        android:backgroundTint="@color/button"
        android:fontFamily="@font/font2"
        android:rotation="23"
        android:text="@string/mypagebutton_msg"
        android:textColor="@color/black"
        android:textSize="15sp"

        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.417" />

    <Button
        android:id="@+id/loginbutton"
        android:layout_width="wrap_content"
        android:layout_height="150dp"
        android:layout_marginTop="185dp"
        android:layout_marginBottom="10dp"
        android:background="@color/button"
        android:fontFamily="@font/font2"
        android:text="@string/loginbutton_msg"
        android:textColor="@color/brown"
        android:textSize="15sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

## layout
### activity_done

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".DoneActivity">


    <TextView
        android:id="@+id/textVie2w"
        android:layout_width="wrap_content"
        android:layout_height="50dp"
        android:layout_marginTop="160dp"
        android:gravity="center"
        android:text="@string/done_text"
        android:textColor="@color/orange"
        android:textSize="28sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/textVie2w2"
        android:layout_width="wrap_content"
        android:layout_height="100dp"
        android:layout_marginTop="5dp"
        android:gravity="center"
        android:text="@string/ment_text"
        android:textSize="16sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textVie2w" />

    <ImageView
        android:id="@+id/imageView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textVie2w2"
        app:srcCompat="@drawable/humanss" />

    <Button
        android:id="@+id/goToLogin"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="30dp"
        android:layout_marginRight="20dp"
        android:backgroundTint="@color/orange"
        android:text="@string/login_text"
        android:textSize="20sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/imageView2" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

### activity_login.xml

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:foregroundTint="@color/white"
    tools:context=".LogInActivity">


    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/constraintLayout"
        android:layout_width="match_parent"
        android:layout_height="65dp"
        android:background="@color/orange"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">


        <TextView
            android:id="@+id/textView8"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:gravity="center"
            android:text="@string/login_text"
            android:textColor="@color/white"
            android:textSize="27sp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <ImageView
            android:id="@+id/imageView4"
            android:layout_width="59dp"
            android:layout_height="61dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:srcCompat="@drawable/arrow" />


    </androidx.constraintlayout.widget.ConstraintLayout>


    <EditText
        android:id="@+id/loginEmail"
        android:layout_width="330dp"
        android:layout_height="62dp"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="20dp"
        android:layout_marginRight="20dp"
        android:ems="10"
        android:hint="@string/email_text"
        android:inputType="textEmailAddress"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.486"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/imageView" />

    <EditText
        android:id="@+id/logInpass"
        android:layout_width="330dp"
        android:layout_height="64dp"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="15dp"
        android:layout_marginRight="20dp"
        android:ems="10"
        android:hint="@string/password_text"
        android:inputType="textPassword"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/loginEmail" />

    <Button
        android:id="@+id/btnLogin"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginLeft="30dp"
        android:layout_marginTop="20dp"
        android:layout_marginRight="30dp"
        android:backgroundTint="@color/orange"
        android:text="@string/login_text"
        android:textSize="21sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/logInpass" />

    <Button
        android:id="@+id/btnSignUp"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginLeft="30dp"
        android:layout_marginTop="10dp"
        android:layout_marginRight="30dp"
        android:backgroundTint="@color/orange"
        android:text="@string/sign_up_text"
        android:textSize="21sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.333"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/btnLogin" />

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/btngoogle"
        android:layout_width="165dp"
        android:layout_height="40dp"
        android:layout_marginTop="50dp"
        android:background="@drawable/googlelogin"
        app:layout_constraintEnd_toStartOf="@+id/btnNaver"
        app:layout_constraintHorizontal_chainStyle="packed"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/btnSignUp">

        <TextView
            android:id="@+id/textView4"
            android:layout_width="110dp"
            android:layout_height="0dp"
            android:layout_marginEnd="5dp"
            android:gravity="center"
            android:text="@string/google_text"
            android:textStyle="bold"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintVertical_bias="1.0" />

        <ImageView
            android:id="@+id/imageView5"
            android:layout_width="35dp"
            android:layout_height="40dp"
            android:layout_marginStart="40dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:srcCompat="@drawable/google" />

    </androidx.constraintlayout.widget.ConstraintLayout>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/btnNaver"
        android:layout_width="165dp"
        android:layout_height="40dp"
        android:layout_marginStart="10dp"
        android:background="@drawable/googlelogin"
        app:layout_constraintBottom_toBottomOf="@+id/btngoogle"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@+id/btngoogle"
        app:layout_constraintTop_toTopOf="@+id/btngoogle">

        <TextView
            android:id="@+id/textView11"
            android:layout_width="110dp"
            android:layout_height="0dp"
            android:layout_marginEnd="12dp"
            android:layout_marginTop="3dp"
            android:gravity="center"
            android:text="@string/naver_text"
            android:textStyle="bold"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintVertical_bias="1.0" />

        <ImageView
            android:id="@+id/imageView11"
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:layout_marginStart="25dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:srcCompat="@drawable/naver" />

    </androidx.constraintlayout.widget.ConstraintLayout>

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="130dp"
        android:layout_marginTop="10dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/constraintLayout"
        app:srcCompat="@drawable/people" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

### activity_main.xml
#### 1. HorizontalScrollView(좌우 스크롤뷰)
좌우 스크롤뷰로 오늘의 추천친구 구현

#### 2. NestedScrollView(상하 스크롤뷰)
ScrollView와 같지만 중첩되어 오류가 있을경우 사용, UI수정과정 중 ScrollView로 사용가능하나, 시간상 관계로 바꾸지 않음

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/top_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="10dp"
        android:layout_marginEnd="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">


        <TextView
            android:id="@+id/textView4"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20sp"
            android:fontFamily="@font/neodgm"
            android:text="싸이월드"
            android:textColor="@color/blue2"
            android:textSize="20sp"
            android:textStyle="bold"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/txtLogout"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="20dp"
            android:fontFamily="@font/neodgm"
            android:text="로그아웃"
            android:textColor="#FFAB91"
            android:textSize="20sp"
            android:textStyle="bold"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent" />


    </androidx.constraintlayout.widget.ConstraintLayout>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/middle_layout"
        android:layout_width="match_parent"
        android:layout_height="90dp"
        android:layout_marginStart="16dp"
        android:layout_marginTop="10dp"
        android:layout_marginEnd="16dp"
        android:background="@drawable/minicap"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/layout_recomend">

        <ImageView-->
            android:id="@+id/cr_1"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:layout_marginStart="60dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintVertical_bias="0.466"
            app:srcCompat="@drawable/run_gif" />

        <TextView
            android:id="@+id/textView5"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="18dp"
            android:layout_marginEnd="30dp"
            android:fontFamily="@font/neodgm"
            android:text="@string/seven_text"
            android:textColor="@color/black"
            android:textSize="30sp"
            android:textStyle="bold"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <Button
            android:id="@+id/btnMypage"
            android:layout_width="wrap_content"
            android:layout_height="80dp"
            android:layout_marginEnd="55dp"
            android:layout_marginBottom="3dp"
            android:background="@color/button"
            android:backgroundTint="#FEFAFD"
            android:fontFamily="@font/neodgm"
            android:gravity="center|bottom"
            android:text="@string/my_page_text"
            android:textColor="@color/black"
            android:textSize="20sp"
            android:textStyle="bold"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent" />


    </androidx.constraintlayout.widget.ConstraintLayout>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/constraintLayout"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:layout_marginStart="16dp"
        android:layout_marginTop="10dp"
        android:layout_marginEnd="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/middle_layout">

        <LinearLayout
            android:id="@+id/layout_like"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:background="@drawable/main_bg_white"
            android:onClick="like_count"
            android:orientation="horizontal"
            app:layout_constraintEnd_toStartOf="@+id/linearLayout"
            app:layout_constraintHorizontal_weight="1"
            app:layout_constraintStart_toStartOf="parent"
            tools:ignore="MissingConstraints"
            tools:layout_editor_absoluteY="0dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:layout_marginStart="10dp"
                android:layout_marginTop="1dp"
                android:fontFamily="@font/neodgm"
                android:text="LIKE"
                android:textColor="@color/blue2"
                android:textSize="20sp"
                android:textStyle="bold" />

            <ImageView
                android:id="@+id/heart"
                android:layout_width="25dp"
                android:layout_height="25dp"
                android:layout_gravity="center"
                android:layout_marginStart="5dp"
                android:layout_marginEnd="10dp"
                android:layout_weight="1"
                android:background="@drawable/like" />


        </LinearLayout>

        <LinearLayout
            android:id="@+id/linearLayout"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_marginEnd="70dp"
            android:background="@drawable/main_bg_white"
            android:orientation="horizontal"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_weight="1"
            tools:layout_editor_absoluteY="0dp">

            <TextView
                android:id="@+id/one"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:layout_marginStart="10dp"
                android:layout_marginTop="1dp"
                android:layout_weight="1"
                android:fontFamily="@font/neodgm"
                android:text="TODAY"
                android:textColor="@color/blue2"
                android:textSize="20sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toStartOf="@+id/textView6"
                tools:layout_editor_absoluteY="16dp" />

            <TextView
                android:id="@+id/textView6"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:layout_marginStart="10dp"
                android:layout_marginEnd="10dp"
                android:layout_weight="1"
                android:fontFamily="@font/neodgm"
                android:text="@string/todaynumber"
                android:textColor="@color/background"
                android:textSize="20sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="parent"
                tools:layout_editor_absoluteY="16dp" />
        </LinearLayout>

    </androidx.constraintlayout.widget.ConstraintLayout>

    <TextView
        android:id="@+id/textView7"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="10dp"
        android:fontFamily="@font/neodgm"
        android:text="오늘의 추천 친구"
        android:textSize="20sp"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/top_layout" />

    <HorizontalScrollView
        android:id="@+id/layout_recomend"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toBottomOf="@+id/textView7"
        tools:layout_editor_absoluteX="52dp">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/recomend"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            app:layout_constraintTop_toBottomOf="@+id/textView7"
            tools:layout_editor_absoluteX="-16dp">

            <ImageView
                android:id="@+id/dohyun"
                android:layout_width="60dp"
                android:layout_height="60dp"
                android:layout_marginStart="30dp"
                android:background="@drawable/main_img_cycle"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/dohyun" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:fontFamily="@font/neodgm"
                android:text="이도현"
                android:textSize="15sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="@+id/dohyun"
                app:layout_constraintStart_toStartOf="@+id/dohyun"
                app:layout_constraintTop_toBottomOf="@+id/dohyun" />

            <ImageView
                android:id="@+id/jisu"
                android:layout_width="60dp"
                android:layout_height="60dp"
                android:layout_marginStart="30dp"
                android:background="@drawable/main_img_cycle"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toEndOf="@+id/dohyun"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/jisoo" />

            <TextView
                android:id="@+id/textView9"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:fontFamily="@font/neodgm"
                android:text="지수"
                android:textSize="15sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="@+id/jisu"
                app:layout_constraintStart_toStartOf="@+id/jisu"
                app:layout_constraintTop_toBottomOf="@+id/jisu" />

            <ImageView
                android:id="@+id/iu"
                android:layout_width="60dp"
                android:layout_height="60dp"
                android:layout_marginStart="30dp"
                android:background="@drawable/main_img_cycle"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toEndOf="@+id/jisu"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/iu" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:fontFamily="@font/neodgm"
                android:text="아이유"
                android:textSize="15sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="@+id/iu"
                app:layout_constraintStart_toStartOf="@+id/iu"
                app:layout_constraintTop_toBottomOf="@+id/iu" />

            <ImageView
                android:id="@+id/jichangwook"
                android:layout_width="60dp"
                android:layout_height="60dp"
                android:layout_marginStart="30dp"
                android:background="@drawable/main_img_cycle"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toEndOf="@+id/iu"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/jichangwook" />

            <TextView
                android:id="@+id/textView8"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:fontFamily="@font/neodgm"
                android:text="지창욱"
                android:textSize="15sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="@+id/jichangwook"
                app:layout_constraintStart_toStartOf="@+id/jichangwook"
                app:layout_constraintTop_toBottomOf="@+id/jichangwook" />

            <ImageView
                android:id="@+id/imageView7"
                android:layout_width="60dp"
                android:layout_height="60dp"
                android:layout_marginStart="30dp"
                android:layout_marginEnd="30dp"
                android:background="@drawable/main_img_cycle"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toEndOf="@+id/jichangwook"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/hyunjin" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:fontFamily="@font/neodgm"
                android:text="서현진"
                android:textSize="15sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="@+id/imageView7"
                app:layout_constraintStart_toStartOf="@+id/imageView7"
                app:layout_constraintTop_toBottomOf="@+id/imageView7" />

        </androidx.constraintlayout.widget.ConstraintLayout>

    </HorizontalScrollView>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="10dp"
        android:fontFamily="@font/neodgm"
        android:text="일촌"
        android:textSize="20sp"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/constraintLayout">

    </TextView>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="335dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:background="@drawable/main_bg_orange"
        tools:layout_editor_absoluteX="0dp"
        tools:layout_editor_absoluteY="471dp">

            <ImageView
                android:id="@+id/img1"
                android:layout_width="70dp"
                android:layout_height="70dp"
                android:layout_marginStart="30dp"
                android:layout_marginTop="20dp"
                android:background="@drawable/img_edge"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/img1" />

            <TextView
                android:id="@+id/name_1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="30dp"
                android:fontFamily="@font/neodgm"
                android:text="이름 : 남윤희"
                android:textSize="20sp"
                android:textStyle="bold"
                app:layout_constraintStart_toEndOf="@+id/img1"
                app:layout_constraintTop_toTopOf="parent" />

            <TextView
                android:id="@+id/mbti_1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="10dp"
                android:fontFamily="@font/neodgm"
                android:text="MBTI : ENFP"
                android:textSize="23sp"
                android:textStyle="bold"
                app:layout_constraintStart_toEndOf="@+id/img1"
                app:layout_constraintTop_toBottomOf="@+id/name_1" />

            <ImageView
                android:id="@+id/img2"
                android:layout_width="70dp"
                android:layout_height="70dp"
                android:layout_marginStart="30dp"
                android:layout_marginTop="20dp"
                android:background="@drawable/img_edge"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/img1"
                app:srcCompat="@drawable/img2" />

            <TextView
                android:id="@+id/name_2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="40dp"
                android:fontFamily="@font/neodgm"
                android:text="이름 : 임주리"
                android:textSize="20sp"
                android:textStyle="bold"
                app:layout_constraintStart_toEndOf="@+id/img2"
                app:layout_constraintTop_toBottomOf="@+id/mbti_1" />

            <TextView
                android:id="@+id/mbti_2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="10dp"
                android:fontFamily="@font/neodgm"
                android:text="MBTI : ENFJ"
                android:textSize="23sp"
                android:textStyle="bold"
                app:layout_constraintStart_toEndOf="@+id/img1"
                app:layout_constraintTop_toBottomOf="@+id/name_2" />

            <ImageView
                android:id="@+id/img3"
                android:layout_width="70dp"
                android:layout_height="70dp"
                android:layout_marginStart="30dp"
                android:layout_marginTop="20dp"
                android:background="@drawable/img_edge"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/img2"
                app:srcCompat="@drawable/img3" />

            <TextView
                android:id="@+id/name_3"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="35dp"
                android:fontFamily="@font/neodgm"
                android:text="이름 : 황수연"
                android:textSize="20sp"
                android:textStyle="bold"
                app:layout_constraintStart_toEndOf="@+id/img3"
                app:layout_constraintTop_toBottomOf="@+id/mbti_2" />

            <TextView
                android:id="@+id/mbti_3"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="10dp"
                android:fontFamily="@font/neodgm"
                android:text="MBTI : ENFP"
                android:textSize="23sp"
                android:textStyle="bold"
                app:layout_constraintStart_toEndOf="@+id/img3"
                app:layout_constraintTop_toBottomOf="@+id/name_3" />

            <ImageView
                android:id="@+id/img4"
                android:layout_width="70dp"
                android:layout_height="70dp"
                android:layout_marginStart="30dp"
                android:layout_marginTop="20dp"
                android:background="@drawable/img_edge"
                android:clipToOutline="true"
                android:scaleType="centerCrop"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/img3"
                app:srcCompat="@drawable/img4" />

            <TextView
                android:id="@+id/name_4"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="35dp"
                android:fontFamily="@font/neodgm"
                android:text="이름 : 남소진"
                android:textSize="20sp"
                android:textStyle="bold"
                app:layout_constraintStart_toEndOf="@+id/img4"
                app:layout_constraintTop_toBottomOf="@+id/mbti_3" />

            <TextView
                android:id="@+id/mbti_4"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="30dp"
                android:layout_marginTop="10dp"
                android:layout_marginBottom="20dp"
                android:fontFamily="@font/neodgm"
                android:text="MBTI : INFP"
                android:textSize="23sp"
                android:textStyle="bold"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toEndOf="@+id/img4"
                app:layout_constraintTop_toBottomOf="@+id/name_4" />


        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.core.widget.NestedScrollView>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### activity_my_page.xml

```
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/rounded"
        tools:context=".MainActivity">


        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/play"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/play"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toEndOf="@+id/before"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/next"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/next"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toEndOf="@+id/pause"
            app:layout_constraintTop_toBottomOf="@+id/line_top2"
            app:layout_constraintVertical_bias="0.514" />

        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/before"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:layout_marginStart="250dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/before"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2"
            app:layout_constraintVertical_bias="0.514" />

        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/pause"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/pause"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toEndOf="@+id/play"
            app:layout_constraintTop_toBottomOf="@+id/line_top2"
            app:layout_constraintVertical_bias="0.514" />

        <View
            android:id="@+id/line_top5"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginTop="60dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.451"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <TextView
            android:id="@+id/textView20"
            android:layout_width="166dp"
            android:layout_height="24dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 퀸ㅋΓ 소ズlLI"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/imageView6"
            app:layout_constraintTop_toBottomOf="@+id/team_text3" />

        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text4"
            android:layout_width="277dp"
            android:layout_height="80dp"
            android:layout_marginStart="16dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintStart_toEndOf="@+id/imageView6"
            app:layout_constraintTop_toBottomOf="@+id/textView20"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />

        <ImageView
            android:id="@+id/imageView6"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/imageView5"
            app:srcCompat="@drawable/five" />

        <ImageView
            android:id="@+id/imageView5"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/team_one2"
            app:srcCompat="@drawable/four" />

        <TextView
            android:id="@+id/textView18"
            android:layout_width="158dp"
            android:layout_height="23dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 간Zi 윤ㅎi"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/team_one2"
            app:layout_constraintTop_toBottomOf="@+id/team_text" />

        <TextView
            android:id="@+id/textView19"
            android:layout_width="156dp"
            android:layout_height="21dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 얼ㅉ6 주Zl"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/imageView5"
            app:layout_constraintTop_toBottomOf="@+id/team_text2" />

        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text3"
            android:layout_width="275dp"
            android:layout_height="64dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/imageView5"
            app:layout_constraintTop_toBottomOf="@+id/textView19"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />


        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text2"
            android:layout_width="255dp"
            android:layout_height="66dp"
            android:layout_marginStart="16dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/team_one2"
            app:layout_constraintTop_toBottomOf="@+id/textView18"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />

        <ImageView
            android:id="@+id/team_one2"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/team_one"
            app:srcCompat="@drawable/three" />

        <TextView
            android:id="@+id/textView17"
            android:layout_width="157dp"
            android:layout_height="21dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 Zi존 수야ㄴi"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/team_one"
            app:layout_constraintTop_toBottomOf="@+id/line_top3" />

        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text"
            android:layout_width="259dp"
            android:layout_height="58dp"
            android:layout_marginStart="16dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/team_one"
            app:layout_constraintTop_toBottomOf="@+id/textView17"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />

        <TextView
            android:id="@+id/textView15"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:layout_marginEnd="20dp"
            android:fontFamily="@font/neodgm"
            android:text="ズl존 7조 미니룸"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top5" />

        <TextView
            android:id="@+id/today_icon_text3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="24dp"
            android:layout_marginTop="10dp"
            android:fontFamily="@font/neodgm"
            android:text="Comments"
            android:textColor="#6BBED3"
            android:textSize="18sp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top3" />

        <View
            android:id="@+id/line_top3"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginStart="30dp"
            android:layout_marginTop="15dp"
            android:layout_marginEnd="30dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/miniroom" />

        <ImageView
            android:id="@+id/miniroompng"
            android:layout_width="364dp"
            android:layout_height="171dp"
            android:layout_marginTop="20dp"
            android:src="@drawable/miniroom"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/today_icon_text2" />

        <Button
            android:id="@+id/miniroom"
            android:layout_width="364dp"
            android:layout_height="171dp"
            android:layout_marginTop="20dp"
            android:backgroundTint="@color/button"
            android:fontFamily="@font/neodgm"
            android:text="미니룸 들어가기"
            android:textColor="#070707"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/today_icon_text2" />

        <TextView
            android:id="@+id/today_icon_text2"
            android:layout_width="84dp"
            android:layout_height="16dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:fontFamily="@font/neodgm"
            android:text="Mini Room"
            android:textColor="#6BBED3"
            android:textSize="18sp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top5" />

        <TextView
            android:id="@+id/today_icon_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="Today is ..."
            android:textColor="#6BBED3"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/profile_image" />

        <TextView
            android:id="@+id/like_count2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="5dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="100"
            android:textColor="#A6A6A6"
            app:layout_constraintStart_toEndOf="@+id/like_text2"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/like_text2"
            android:layout_width="30dp"
            android:layout_height="12dp"
            android:layout_marginStart="10dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="일촌"
            android:textColor="#6BBED3"
            android:textSize="14sp"
            app:layout_constraintStart_toEndOf="@+id/like_count"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/like_count"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="5dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="100"
            android:textColor="#989696"
            app:layout_constraintStart_toEndOf="@+id/like_text"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/like_text"
            android:layout_width="30dp"
            android:layout_height="12dp"
            android:layout_marginStart="40dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="like"
            android:textColor="#6BBED3"
            android:textSize="14sp"
            app:layout_constraintStart_toEndOf="@+id/icon_text"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/icon_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="3dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="코딩 중.."
            android:textSize="12sp"
            app:layout_constraintStart_toEndOf="@+id/icon"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <ImageView
            android:id="@+id/icon"
            android:layout_width="29dp"
            android:layout_height="27dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="8dp"
            app:layout_constraintStart_toEndOf="@+id/today_icon_text"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text"
            app:srcCompat="@drawable/computer" />

        <View
            android:id="@+id/line_top2"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginStart="30dp"
            android:layout_marginTop="5dp"
            android:layout_marginEnd="30dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.636"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/icon" />

        <TextView
            android:id="@+id/introduce_text"
            android:layout_width="223dp"
            android:layout_height="52dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="3dp"
            android:fontFamily="@font/neodgm"
            android:text="최강 ズl존 7조ㄱr ㄴrㄱr신⊂ト ㅋㅋ 길을 ㉥ㅣ켜ㄹΓ 코딩 캡짱 재밌⊂ト   ๑˃̶͈̀Ⱉ˂̶͈́๑  일촌 받ㅇr요 ~"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintStart_toEndOf="@+id/profile_image"
            app:layout_constraintTop_toBottomOf="@+id/name_text" />

        <TextView
            android:id="@+id/name_text"
            android:layout_width="107dp"
            android:layout_height="20dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="10dp"
            android:fontFamily="@font/neodgm"
            android:text="Oi퓨zl호6"
            android:textSize="20sp"
            app:layout_constraintStart_toEndOf="@+id/profile_image"
            app:layout_constraintTop_toBottomOf="@+id/today_text" />

        <TextView
            android:id="@+id/maximum"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginTop="8dp"
            android:fontFamily="@font/neodgm"
            android:text="ㅣ 999"
            android:textColor="#A6A6A6"
            app:layout_constraintStart_toEndOf="@+id/count"
            app:layout_constraintTop_toBottomOf="@+id/line_top" />

        <TextView
            android:id="@+id/count"
            android:layout_width="17dp"
            android:layout_height="17dp"
            android:layout_marginStart="16dp"
            android:layout_marginTop="8dp"
            android:fontFamily="@font/neodgm"
            android:text="0"
            android:textColor="#FF9800"
            app:layout_constraintStart_toEndOf="@+id/today_text"
            app:layout_constraintTop_toBottomOf="@+id/line_top" />

        <TextView
            android:id="@+id/today_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="8dp"
            android:fontFamily="@font/neodgm"
            android:text="To day"
            android:textColor="#A6A6A6"
            app:layout_constraintStart_toEndOf="@+id/profile_image"
            app:layout_constraintTop_toBottomOf="@+id/line_top" />

        <ImageView
            android:id="@+id/profile_image"
            android:layout_width="92dp"
            android:layout_height="94dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top"
            app:srcCompat="@drawable/one" />

        <TextView
            android:id="@+id/title_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="사이좋은 사람들, 싸이월드"
            android:textColor="#6BBED3"
            android:textSize="18sp"
            app:layout_constraintBottom_toTopOf="@+id/line_top"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <View
            android:id="@+id/line_top"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginStart="30dp"
            android:layout_marginTop="70dp"
            android:layout_marginEnd="30dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <ImageView
            android:id="@+id/team_one"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/today_icon_text3"
            app:srcCompat="@drawable/two" />


        <TextView
            android:id="@+id/bgm"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="5dp"
            android:fontFamily="@font/neodgm"
            android:text="BGM"
            android:textColor="#6BBED3"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <TextView
            android:id="@+id/bgmtitle"
            android:layout_width="213dp"
            android:layout_height="31dp"
            android:layout_marginTop="23dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="NCT DREAM - CANDY"
            app:layout_constraintEnd_toStartOf="@+id/before"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <ImageView
            android:id="@+id/btnsetting"
            android:layout_width="34dp"
            android:layout_height="34dp"
            android:layout_marginTop="10dp"
            android:layout_marginRight="10dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:srcCompat="@drawable/cog_wheel" />


    </androidx.constraintlayout.widget.ConstraintLayout>

</ScrollView>
```

### activity_password.xml

```
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/rounded"
        tools:context=".MainActivity">


        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/play"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/play"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toEndOf="@+id/before"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/next"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/next"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toEndOf="@+id/pause"
            app:layout_constraintTop_toBottomOf="@+id/line_top2"
            app:layout_constraintVertical_bias="0.514" />

        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/before"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:layout_marginStart="250dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/before"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2"
            app:layout_constraintVertical_bias="0.514" />

        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/pause"
            android:layout_width="27dp"
            android:layout_height="24dp"
            android:padding="0dp"
            android:scaleType="centerCrop"
            android:src="@drawable/pause"
            app:layout_constraintBottom_toTopOf="@+id/line_top5"
            app:layout_constraintStart_toEndOf="@+id/play"
            app:layout_constraintTop_toBottomOf="@+id/line_top2"
            app:layout_constraintVertical_bias="0.514" />

        <View
            android:id="@+id/line_top5"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginTop="60dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.451"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <TextView
            android:id="@+id/textView20"
            android:layout_width="166dp"
            android:layout_height="24dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 퀸ㅋΓ 소ズlLI"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/imageView6"
            app:layout_constraintTop_toBottomOf="@+id/team_text3" />

        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text4"
            android:layout_width="277dp"
            android:layout_height="80dp"
            android:layout_marginStart="16dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintStart_toEndOf="@+id/imageView6"
            app:layout_constraintTop_toBottomOf="@+id/textView20"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />

        <ImageView
            android:id="@+id/imageView6"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/imageView5"
            app:srcCompat="@drawable/five" />

        <ImageView
            android:id="@+id/imageView5"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/team_one2"
            app:srcCompat="@drawable/four" />

        <TextView
            android:id="@+id/textView18"
            android:layout_width="158dp"
            android:layout_height="23dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 간Zi 윤ㅎi"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/team_one2"
            app:layout_constraintTop_toBottomOf="@+id/team_text" />

        <TextView
            android:id="@+id/textView19"
            android:layout_width="156dp"
            android:layout_height="21dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 얼ㅉ6 주Zl"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/imageView5"
            app:layout_constraintTop_toBottomOf="@+id/team_text2" />

        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text3"
            android:layout_width="275dp"
            android:layout_height="64dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/imageView5"
            app:layout_constraintTop_toBottomOf="@+id/textView19"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />


        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text2"
            android:layout_width="255dp"
            android:layout_height="66dp"
            android:layout_marginStart="16dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/team_one2"
            app:layout_constraintTop_toBottomOf="@+id/textView18"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />

        <ImageView
            android:id="@+id/team_one2"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/team_one"
            app:srcCompat="@drawable/three" />

        <TextView
            android:id="@+id/textView17"
            android:layout_width="157dp"
            android:layout_height="21dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="40dp"
            android:ellipsize="none"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="7조 Zi존 수야ㄴi"
            android:textSize="16sp"
            app:layout_constraintStart_toEndOf="@+id/team_one"
            app:layout_constraintTop_toBottomOf="@+id/line_top3" />

        <kr.co.prnd.readmore.ReadMoreTextView
            android:id="@+id/team_text"
            android:layout_width="259dp"
            android:layout_height="58dp"
            android:layout_marginStart="16dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/team_one"
            app:layout_constraintTop_toBottomOf="@+id/textView17"
            app:readMoreColor="@android:color/holo_blue_light"
            app:readMoreMaxLine="2"
            app:readMoreText="...더보기" />

        <TextView
            android:id="@+id/textView15"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:layout_marginEnd="20dp"
            android:fontFamily="@font/neodgm"
            android:text="ズl존 7조 미니룸"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top5" />

        <TextView
            android:id="@+id/today_icon_text3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="24dp"
            android:layout_marginTop="10dp"
            android:fontFamily="@font/neodgm"
            android:text="Comments"
            android:textColor="#6BBED3"
            android:textSize="18sp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top3" />

        <View
            android:id="@+id/line_top3"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginStart="30dp"
            android:layout_marginTop="15dp"
            android:layout_marginEnd="30dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/miniroom" />

        <ImageView
            android:id="@+id/miniroompng"
            android:layout_width="364dp"
            android:layout_height="171dp"
            android:layout_marginTop="20dp"
            android:src="@drawable/miniroom"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/today_icon_text2" />

        <Button
            android:id="@+id/miniroom"
            android:layout_width="364dp"
            android:layout_height="171dp"
            android:layout_marginTop="20dp"
            android:backgroundTint="@color/button"
            android:fontFamily="@font/neodgm"
            android:text="미니룸 들어가기"
            android:textColor="#070707"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/today_icon_text2" />

        <TextView
            android:id="@+id/today_icon_text2"
            android:layout_width="84dp"
            android:layout_height="16dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="20dp"
            android:fontFamily="@font/neodgm"
            android:text="Mini Room"
            android:textColor="#6BBED3"
            android:textSize="18sp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top5" />

        <TextView
            android:id="@+id/today_icon_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="Today is ..."
            android:textColor="#6BBED3"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/profile_image" />

        <TextView
            android:id="@+id/like_count2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="5dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="100"
            android:textColor="#A6A6A6"
            app:layout_constraintStart_toEndOf="@+id/like_text2"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/like_text2"
            android:layout_width="30dp"
            android:layout_height="12dp"
            android:layout_marginStart="10dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="일촌"
            android:textColor="#6BBED3"
            android:textSize="14sp"
            app:layout_constraintStart_toEndOf="@+id/like_count"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/like_count"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="5dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="100"
            android:textColor="#989696"
            app:layout_constraintStart_toEndOf="@+id/like_text"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/like_text"
            android:layout_width="30dp"
            android:layout_height="12dp"
            android:layout_marginStart="40dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="like"
            android:textColor="#6BBED3"
            android:textSize="14sp"
            app:layout_constraintStart_toEndOf="@+id/icon_text"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <TextView
            android:id="@+id/icon_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="3dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="코딩 중.."
            android:textSize="12sp"
            app:layout_constraintStart_toEndOf="@+id/icon"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text" />

        <ImageView
            android:id="@+id/icon"
            android:layout_width="29dp"
            android:layout_height="27dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="8dp"
            app:layout_constraintStart_toEndOf="@+id/today_icon_text"
            app:layout_constraintTop_toBottomOf="@+id/introduce_text"
            app:srcCompat="@drawable/computer" />

        <View
            android:id="@+id/line_top2"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginStart="30dp"
            android:layout_marginTop="5dp"
            android:layout_marginEnd="30dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.636"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/icon" />

        <TextView
            android:id="@+id/introduce_text"
            android:layout_width="223dp"
            android:layout_height="52dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="3dp"
            android:fontFamily="@font/neodgm"
            android:text="최강 ズl존 7조ㄱr ㄴrㄱr신⊂ト ㅋㅋ 길을 ㉥ㅣ켜ㄹΓ 코딩 캡짱 재밌⊂ト   ๑˃̶͈̀Ⱉ˂̶͈́๑  일촌 받ㅇr요 ~"
            android:textColor="#A6A6A6"
            android:textSize="12sp"
            app:layout_constraintStart_toEndOf="@+id/profile_image"
            app:layout_constraintTop_toBottomOf="@+id/name_text" />

        <TextView
            android:id="@+id/name_text"
            android:layout_width="107dp"
            android:layout_height="20dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="10dp"
            android:fontFamily="@font/neodgm"
            android:text="Oi퓨zl호6"
            android:textSize="20sp"
            app:layout_constraintStart_toEndOf="@+id/profile_image"
            app:layout_constraintTop_toBottomOf="@+id/today_text" />

        <TextView
            android:id="@+id/maximum"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginTop="8dp"
            android:fontFamily="@font/neodgm"
            android:text="ㅣ 999"
            android:textColor="#A6A6A6"
            app:layout_constraintStart_toEndOf="@+id/count"
            app:layout_constraintTop_toBottomOf="@+id/line_top" />

        <TextView
            android:id="@+id/count"
            android:layout_width="17dp"
            android:layout_height="17dp"
            android:layout_marginStart="16dp"
            android:layout_marginTop="8dp"
            android:fontFamily="@font/neodgm"
            android:text="0"
            android:textColor="#FF9800"
            app:layout_constraintStart_toEndOf="@+id/today_text"
            app:layout_constraintTop_toBottomOf="@+id/line_top" />

        <TextView
            android:id="@+id/today_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="8dp"
            android:fontFamily="@font/neodgm"
            android:text="To day"
            android:textColor="#A6A6A6"
            app:layout_constraintStart_toEndOf="@+id/profile_image"
            app:layout_constraintTop_toBottomOf="@+id/line_top" />

        <ImageView
            android:id="@+id/profile_image"
            android:layout_width="92dp"
            android:layout_height="94dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top"
            app:srcCompat="@drawable/one" />

        <TextView
            android:id="@+id/title_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:fontFamily="@font/neodgm"
            android:text="사이좋은 사람들, 싸이월드"
            android:textColor="#6BBED3"
            android:textSize="18sp"
            app:layout_constraintBottom_toTopOf="@+id/line_top"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <View
            android:id="@+id/line_top"
            android:layout_width="1000px"
            android:layout_height="5px"
            android:layout_marginStart="30dp"
            android:layout_marginTop="70dp"
            android:layout_marginEnd="30dp"
            android:background="#EEEEEE"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <ImageView
            android:id="@+id/team_one"
            android:layout_width="67dp"
            android:layout_height="63dp"
            android:layout_marginStart="20dp"
            android:layout_marginTop="15dp"
            android:background="@drawable/rounded"
            android:clipToOutline="true"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/today_icon_text3"
            app:srcCompat="@drawable/two" />


        <TextView
            android:id="@+id/bgm"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginTop="5dp"
            android:fontFamily="@font/neodgm"
            android:text="BGM"
            android:textColor="#6BBED3"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <TextView
            android:id="@+id/bgmtitle"
            android:layout_width="213dp"
            android:layout_height="31dp"
            android:layout_marginTop="23dp"
            android:fontFamily="@font/neodgm"
            android:gravity="center"
            android:text="NCT DREAM - CANDY"
            app:layout_constraintEnd_toStartOf="@+id/before"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/line_top2" />

        <ImageView
            android:id="@+id/btnsetting"
            android:layout_width="34dp"
            android:layout_height="34dp"
            android:layout_marginTop="10dp"
            android:layout_marginRight="10dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:srcCompat="@drawable/cog_wheel" />


    </androidx.constraintlayout.widget.ConstraintLayout>

</ScrollView>
```

### activity_search.xml

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".member.SearchActivity">


    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="80dp"
        android:text="회원 정보 수정"
        android:textColor="@color/orange"
        android:textSize="30dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/searchnameTxt"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="수정하실 이메일을 적어주세요."
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView" />

    <Button
        android:id="@+id/findBtn"
        android:layout_width="137dp"
        android:layout_height="50dp"
        android:layout_marginTop="5dp"
        android:text="이메일확인"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editTxtname" />

    <TextView
        android:id="@+id/textViewinfo"
        android:layout_width="277dp"
        android:layout_height="198dp"
        android:textSize="20dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/findBtn" />

    <EditText
        android:id="@+id/editTxtname"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="15dp"
        android:ems="10"
        android:hint="이메일주소"
        android:inputType="textPersonName"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/searchnameTxt" />

    <Button
        android:id="@+id/revise"
        android:layout_width="156dp"
        android:layout_height="56dp"
        android:layout_marginTop="30dp"
        android:layout_marginLeft="30dp"
        android:text="수정하기"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textViewinfo" />

    <Button
        android:id="@+id/delete"
        android:layout_width="156dp"
        android:layout_height="56dp"
        android:layout_marginRight="30dp"
        android:text="회원 탈퇴"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="@+id/revise" />

    <Button
        android:id="@+id/btnBack"
        android:layout_width="356dp"
        android:layout_height="55dp"
        android:layout_marginTop="10dp"
        android:text="뒤로 가기"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/revise" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

### activity_signup.xml

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SignUpActivity">


    <ImageView
        android:id="@+id/imageView6"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/dotori" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="25dp"
        android:text="@string/email_id_enter_text"
        android:textColor="@color/orange"
        android:textSize="30sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/imageView6" />


    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:text="@string/id_ment_text"
        android:gravity="center"
        android:textSize="17sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView"
        tools:ignore="TextSizeCheck" />

    <TextView
        android:id="@+id/textView5"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"
        android:text=""
        app:layout_constraintStart_toStartOf="@+id/signUpId"
        app:layout_constraintTop_toBottomOf="@+id/signUpId" />

    <EditText
        android:id="@+id/signUpId"
        android:layout_width="330dp"
        android:layout_height="62dp"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="20dp"
        android:layout_marginRight="20dp"
        android:hint="@string/email_text"
        android:inputType="textEmailAddress"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.629"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView2" />

    <Button
        android:id="@+id/btnNextToPass"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginLeft="30dp"
        android:layout_marginTop="30dp"
        android:layout_marginRight="30dp"
        android:backgroundTint="@color/orange"
        android:text="@string/next_text"
        android:textSize="20sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.666"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView5" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

### detail_activity.xml

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".DetailActivity">

    <ImageView
        android:id="@+id/back"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/back" />


    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="210dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.6"
        app:srcCompat="@drawable/cw_main" />

    <Button
        android:id="@+id/mainbutton"
        android:layout_width="wrap_content"
        android:layout_height="100dp"
        android:layout_marginBottom="150dp"
        android:background="@color/button"
        android:fontFamily="@font/font2"
        android:rotation="-26"
        android:text="@string/mainbutton_msg"
        android:textColor="@color/pink"
        android:textSize="10sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.53" />

    <Button
        android:id="@+id/mypagebutton"
        android:layout_width="wrap_content"
        android:layout_height="100dp"
        android:layout_marginTop="70dp"
        android:backgroundTint="@color/button"
        android:fontFamily="@font/font2"
        android:rotation="23"
        android:text="@string/mypagebutton_msg"
        android:textColor="@color/black"
        android:textSize="10sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.417" />

    <Button
        android:id="@+id/loginbutton"
        android:layout_width="wrap_content"
        android:layout_height="150dp"
        android:layout_marginTop="185dp"
        android:background="@color/button"
        android:fontFamily="@font/font2"
        android:text="@string/loginbutton_msg"
        android:textColor="@color/brown"
        android:textSize="10sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

### toast_layout.xml

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/toast_layout"
    android:background="@drawable/main_bg_color"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center">

    <ImageView
        android:id="@+id/run_icon"
        android:layout_width="80dp"
        android:layout_height="50dp"
        android:src="@drawable/run_gif"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/me"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/me"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="@color/black"
        android:textSize="15sp"
        android:gravity="center"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />


</androidx.constraintlayout.widget.ConstraintLayout>
```
