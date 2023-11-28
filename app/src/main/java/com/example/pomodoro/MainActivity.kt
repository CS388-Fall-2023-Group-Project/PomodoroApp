package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(SetStudyGoals())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_analytics -> {
                    loadFragment(Analytics())
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
        // Load other fragment using nav bar
        val navView: BottomNavigationView = findViewById(R.id.bottomNavBar)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Load the initial fragment
        loadFragment(SetStudyGoals())
    }
    // Method to load a fragment
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()

    }
}