package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartStudyingTab: Button
    private lateinit var btnAnalyticsTab: Button
    private lateinit var btnHistoryTab: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartStudyingTab = findViewById(R.id.TabStartStudyingButton)
        btnAnalyticsTab = findViewById(R.id.TabAnalyticsButton)
        btnHistoryTab = findViewById(R.id.TabHistoryButton)

        // INTENT to History
        btnHistoryTab.setOnClickListener {
            val intent = Intent(this, History::class.java)
            startActivity(intent)
        }
    }
}