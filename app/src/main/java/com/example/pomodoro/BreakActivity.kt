package com.example.pomodoro

import android.content.Intent
import  android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BreakActivity: AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var countdownTimer: CountdownTimerHelper
    private lateinit var exitButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.challenege_list)


        progressBar = findViewById(R.id.studyoffTimer)
        timerText = findViewById(R.id.timerTextView)
        exitButton = findViewById(R.id.return_btn)



        exitButton.setOnClickListener {
            // Navigate back to the home fragment or activity
            val intent= Intent(this@BreakActivity,TimerActivity::class.java)
            intent.putExtra("restartTimer", true)
            startActivity(intent)

        }
    }
}