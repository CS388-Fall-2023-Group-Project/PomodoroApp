package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartStudyingTab: Button
    private lateinit var btnAnalyticsTab: Button
    private lateinit var btnHistoryTab: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_on)

        btnStartStudyingTab = findViewById(R.id.TabStartStudyingButton)
        btnAnalyticsTab = findViewById(R.id.TabAnalyticsButton)
        btnHistoryTab = findViewById(R.id.TabHistoryButton)
    }
}