package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(SetStudyGoals())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_analytics -> {
                    loadFragment(AnalyticsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_history -> {
                    loadFragment(Fragment_History())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Generate dummy data when the app starts
        generateDummyDataFor7Days()

        // Load other fragment using nav bar
        val navView: BottomNavigationView = findViewById(R.id.bottomNavBar)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Load the initial fragment
        loadFragment(SetStudyGoals())

    }

    private fun generateDummyDataFor7Days() {
        val dummyDataGenerator = DummyDataGenerator(applicationContext)
        dummyDataGenerator.generateDummyDataFor7Days()
    }

    // Method to load a fragment
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {

    }

}