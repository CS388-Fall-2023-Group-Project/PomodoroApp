package com.example.pomodoro


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class TimerActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var countdownTimer: CountdownTimerHelper
    private lateinit var exitButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_timer)

        progressBar = findViewById(R.id.progressbar)
        timerText = findViewById(R.id.timerTexts)
        exitButton = findViewById(R.id.exitStudy)

        val totalTimeInMillis = 60000L // 1 minute
        val interval = 1000L // 1 second

        countdownTimer = CountdownTimerHelper(
            totalTimeInMillis = totalTimeInMillis,
            interval = interval,
            onTick = { millisUntilFinished ->
                val progress = (millisUntilFinished.toFloat() / totalTimeInMillis * 100).toInt()
                progressBar.progress = progress

                // Update the timer text
                val formattedTime = formatTime(millisUntilFinished)
                timerText.text = formattedTime
            },
            onFinish = {
                // Timer finished, handle it as needed
               val intent= Intent(this@TimerActivity,BreakActivity::class.java)
                startActivity(intent)
            }
        )

        // Start the countdown timer
        countdownTimer.start()


        exitButton.setOnClickListener {
            // Navigate back to the home fragment or activity
            finish()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer to avoid memory leaks
        countdownTimer.cancel()
    }

    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}