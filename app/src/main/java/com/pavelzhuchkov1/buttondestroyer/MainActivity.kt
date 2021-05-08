package com.pavelzhuchkov1.buttondestroyer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private val buttons = mutableListOf<Button>()

    private var score: Int = 0
    private var yellowButtonPosition: Int = 0
    private var red: Int = 0
    private var green: Int = 0
    private var blue: Int = 0

    private var gameStarted: Boolean = false

    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameScoreTextView = findViewById(R.id.tv_score)
        timeLeftTextView = findViewById(R.id.tv_time)
        buttons.add(findViewById(R.id.button1))
        buttons.add(findViewById(R.id.button2))
        buttons.add(findViewById(R.id.button3))
        buttons.add(findViewById(R.id.button4))
        buttons.add(findViewById(R.id.button5))
        buttons.add(findViewById(R.id.button6))

        for(i in 0..5) {
            buttons[i].setOnClickListener {
                incrementScore(i)
            }
        }
        if(savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            yellowButtonPosition = savedInstanceState.getInt(POSITION_KEY)
            red = savedInstanceState.getInt(RED_COLOR_KEY)
            green = savedInstanceState.getInt(GREEN_COLOR_KEY)
            blue = savedInstanceState.getInt(BLUE_COLOR_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        outState.putInt(POSITION_KEY, yellowButtonPosition)
        outState.putInt(RED_COLOR_KEY, red)
        outState.putInt(GREEN_COLOR_KEY, green)
        outState.putInt(BLUE_COLOR_KEY, blue)
        countDownTimer.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.about_item) {
            showInfo()
        }

        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    private fun incrementScore(position: Int) {
        if(!gameStarted) {
            startGame()
        }

        if(position == yellowButtonPosition) {
            score++

            changeColorButton()

            val newScore = "Score: $score"
            gameScoreTextView.text = newScore
        }
    }

    private fun resetGame() {
        score = 0
        val initialScore = getString(R.string.tv_score, score)
        gameScoreTextView.text = initialScore
        val initialTimeLeft = getString(R.string.tv_time, 60)
        timeLeftTextView.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.tv_time, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }

        }

        gameStarted = false
    }

    private fun restoreGame() {
        val restoredGame  = getString(R.string.tv_score, score)
        gameScoreTextView.text = restoredGame

        val restoredTime = getString(R.string.tv_time, timeLeft)
        timeLeftTextView.text = restoredTime

        buttons[yellowButtonPosition].setBackgroundColor(Color.rgb(red, green, blue))



        countDownTimer = object : CountDownTimer(
                (timeLeft * 1000).toLong(), countDownInterval
        ) {
            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.tv_time, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    private fun startGame() {
        countDownTimer.start()
        changeColorButton()
        gameStarted = true
    }

    private fun endGame() {
        buttons[yellowButtonPosition].setBackgroundColor(Color.parseColor("#0C572A"))
        Toast.makeText(this, getString(R.string.game_over_message,
                score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun changeColorButton() {
        buttons[yellowButtonPosition].setBackgroundColor(Color.parseColor("#0C572A"))
        yellowButtonPosition = Random.nextInt(0,6)
        red = Random.nextInt(0,256)
        green = Random.nextInt(0,256)
        blue = Random.nextInt(0,256)
        buttons[yellowButtonPosition].setBackgroundColor(Color.rgb(red, green, blue))
    }

    companion object {

        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
        private const val POSITION_KEY = "POSITION_KEY"
        private const val RED_COLOR_KEY = "RED_COLOR_KEY"
        private const val GREEN_COLOR_KEY = "GREEN_COLOR_KEY"
        private const val BLUE_COLOR_KEY = "BLUE_COLOR_KEY"
    }
}