package com.example.pomodoro

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity(){
    private lateinit var timerTextView: TextView
    private lateint var circularProgressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_on)

        timerTextView = findViewById(R.id.timetext)
        circularProgressBar = findViewById(R.id.progressBar)
    }

}