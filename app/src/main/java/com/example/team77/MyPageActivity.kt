package com.example.team77

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


class MyPageActivity : AppCompatActivity() {

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

        val message = "당신의 가능성에 코딩을 곱해보세요. \n 스파르타 코딩클럽"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

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
