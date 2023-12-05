package com.example.pomodoro


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class TimerActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var countdownTimer: CountdownTimerHelper
    private lateinit var exitButton: Button

    private val mainDatabase: MainDatabase by lazy {
        MainDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_timer)

        progressBar = findViewById(R.id.progressbar)
        timerText = findViewById(R.id.timerTexts)
        exitButton = findViewById(R.id.exitStudy)

        val currentDate = intent.getStringExtra("currentDate")
        val studyGoal = intent.getStringExtra("studyGoal")
        val selectedSubject = intent.getStringExtra("selectedSubject")
        val selectedStudyOn = intent.getStringExtra("selectedStudyOn")
        val selectedStudyOff = intent.getStringExtra("selectedStudyOff")
        val currentTimeStart = intent.getStringExtra("currentTimeStart")
        val selectedRounds = intent.getStringExtra("selectedRounds")

        Log.e("MainDatabase", "data received: $currentDate")

        val studyOnMinutes = selectedStudyOn?.let { extractNumberFromString(it) } ?: 0

        val restartTimer = intent.getBooleanExtra("restartTimer", false)

        // If the flag is true, restart the timer
        if (restartTimer) {
            restartTimer()
        }

        val totalTimeInMillis = studyOnMinutes.toLong() // 1 minute
        val interval = 1000L // 1 second

        countdownTimer = CountdownTimerHelper(
            totalTimeInMillis = totalTimeInMillis,
            interval = interval,
            onTick = { millisUntilFinished ->
                val progress = (millisUntilFinished.toFloat() / totalTimeInMillis * 100).toInt()
                progressBar.progress = progress

                // Update the timer text
                val formattedTime = formatTime(millisUntilFinished)
                timerText.text = formattedTime
            },
            onFinish = {
                // Timer finished, handle it as needed
                val intent= Intent(this@TimerActivity,BreakActivity::class.java)
                startActivity(intent)
            }
        )

        // Start the countdown timer
        countdownTimer.start()


        exitButton.setOnClickListener {
            val currentTimeEnd = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

            insertDataIntoDatabase(currentTimeEnd)
            Log.e("MainDatabase", "Exit Button at $currentTimeEnd")

            // Navigate back to the home fragment or activity
            finish()

        }
    }

    private fun insertDataIntoDatabase(currentTimeEnd: String) {
        // OLD DATA FROM SetStudyGoals -----------------------------
        val currentDate = intent.getStringExtra("currentDate")?: ""
        val studyGoal = intent.getStringExtra("studyGoal")?: ""
        val selectedSubject = intent.getStringExtra("selectedSubject")?: ""
        val selectedStudyOn = intent.getStringExtra("selectedStudyOn")?: ""
        val selectedStudyOff = intent.getStringExtra("selectedStudyOff")?: ""
        val currentTimeStart = intent.getStringExtra("currentTimeStart")?: ""
        val selectedRounds = intent.getStringExtra("selectedRounds")?: ""
        // NEW DATA ------------------------------------------------
        // Duration: Convert the duration from milliseconds to hours
        val startMillis = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(currentTimeStart)?.time ?: 0
        val endMillis = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(currentTimeEnd)?.time ?: 0
        val durationInMillis = endMillis - startMillis
        val durationInHours = TimeUnit.MILLISECONDS.toHours(durationInMillis).toInt()
        // Time Range: Fill in Time Range based on collected times
        val timeRange = "$currentTimeStart - $currentTimeEnd"
        // INSERT TO MAINDATABASE ----------------------------------
        mainDatabase.insertStudySession(
            currentDate,
            studyGoal,
            selectedSubject,
            selectedStudyOn,
            selectedStudyOff,
            currentTimeStart,
            currentTimeEnd = currentTimeEnd,
            timeRange,
            duration = durationInHours,
            selectedRounds
        )
        Log.d("MainDatabase", "TimerActivity inserted data of $currentDate to TABLE_TASKDETAILS")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer to avoid memory leaks
        countdownTimer.cancel()
    }

    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun restartTimer() {
        // Your logic to restart the timer goes here
        // For example, create a new instance of CountdownTimerHelper and start it
        countdownTimer.cancel()

        // Start a new timer
        countdownTimer.start()

    }

    private fun extractNumberFromString(timeString: String): Long {
        val regex = """(\d+) (\w+)""".toRegex()
        val matchResult = regex.find(timeString)
        val value = matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val unit = matchResult?.groupValues?.get(2)?.toLowerCase() ?: "minutes"

        return when (unit) {
            "seconds" -> value * 1000L
            "minutes" -> value * 60 * 1000L
            "hours" -> value * 60 * 60 * 1000L
            else -> throw IllegalArgumentException("Unsupported time unit: $unit")
        }
    }


}