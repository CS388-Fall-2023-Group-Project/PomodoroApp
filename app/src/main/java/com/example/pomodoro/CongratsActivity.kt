package com.example.pomodoro

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.jinatonic.confetti.CommonConfetti
import android.graphics.Color
import android.widget.Button
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

class CongratsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.congrats_screen)
        val confettiContainer= findViewById<ConstraintLayout>(R.id.confetti)
         CommonConfetti.rainingConfetti(confettiContainer, intArrayOf(Color.RED, Color.GREEN, Color.BLUE)).oneShot()

        // Update the congratulatory message
        val congratsMessage = findViewById<TextView>(R.id.congratsMessage)
        congratsMessage.text = "Congrats! You completed an entire study session"

        val returnHome = findViewById<Button>(R.id.gotoHome)


        returnHome.setOnClickListener {
            val gotoHomescreen = Intent(this@CongratsActivity, MainActivity::class.java)
            startActivity(gotoHomescreen)
        }


    }

    // Add other logic as needed
}