package com.example.pomodoro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.jinatonic.confetti.CommonConfetti
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout

class CongratsActivity : AppCompatActivity() {
    private lateinit var confettiContainer: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.congrats_screen)

        confettiContainer = findViewById(R.id.confetti)

        // Update the congratulatory message
        val congratsMessage = findViewById<TextView>(R.id.congratsMessage)
        congratsMessage.text = "Congrats! You completed an entire study session"

        val returnHome = findViewById<Button>(R.id.gotoHome)

        returnHome.setOnClickListener {
            val gotoHomescreen = Intent(this@CongratsActivity, MainActivity::class.java)
            startActivity(gotoHomescreen)
        }

        // Start the confetti animation
        startConfettiAnimation()
    }

    private fun startConfettiAnimation() {
        val handler = Handler()
        val delay: Long = 1000 // Set the delay between each confetti animation (in milliseconds)

        val runnable = object : Runnable {
            override fun run() {
                // Trigger confetti animation
                CommonConfetti.rainingConfetti(
                    confettiContainer,
                    intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
                ).oneShot()

                // Repeat the animation
                handler.postDelayed(this, delay)
            }
        }

        // Start the initial confetti animation
        handler.postDelayed(runnable, delay)
    }

    // Add other logic as needed
}
