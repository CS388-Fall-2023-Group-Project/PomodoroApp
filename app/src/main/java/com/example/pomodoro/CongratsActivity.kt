package com.example.pomodoro

import android.os.Bundle
import android.widget.TextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.jinatonic.confetti.CommonConfetti
import com.github.jinatonic.confetti.ConfettiView
import android.graphics.Color
class CongratsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.congrats_screen)

        // Initialize confetti animation
        val confettiContainer = findViewById<FrameLayout>(R.id.confetti)
        CommonConfetti.rainingConfetti(confettiContainer, intArrayOf(Color.RED, Color.GREEN, Color.BLUE))
            .oneShot()

        // Update the congratulatory message
        val congratsMessage = findViewById<TextView>(R.id.congratsMessage)
        congratsMessage.text = "Congrats! You completed an entire study session"
    }

    // Add other logic as needed
}