package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load Fragment_Home when app starts
        loadFragment(Fragment_History())

        // Handle bottom navigation item clicks
        val homeButton: Button = findViewById(R.id.TabStartStudyingButton)
        val analyticsButton: Button = findViewById(R.id.TabAnalyticsButton)
        val historyButton: Button = findViewById(R.id.TabHistoryButton)

        historyButton.setOnClickListener {
            loadFragment(Fragment_History())
        }
    }
    // Method to load a fragment
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}