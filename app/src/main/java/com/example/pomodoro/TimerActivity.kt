// TimerActivity
package com.example.pomodoro


import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import android.app.Dialog
import android.graphics.Color


class TimerActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var countdownTimer: CountdownTimerHelper
    private lateinit var exitButton: Button
    private lateinit var roundCounter: TextView

    private lateinit var welcomeBackDialog: Dialog
    private var roundNumber =1

    // making a a unique notif based on what time it is
    private val NOTIFICATION_ID = System.currentTimeMillis().toInt()
    private val CHANNEL_ID = "timer_channel"


    private val mainDatabase: MainDatabase by lazy {
        MainDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_timer)
        createNotificationChannel()

        progressBar = findViewById(R.id.progressbar)
        timerText = findViewById(R.id.studyOnTimerTV)
        exitButton = findViewById(R.id.exitStudy)
        roundCounter = findViewById(R.id.roundCounterTV)


        val selectedStudyOn = intent.getStringExtra("selectedStudyOn")
        val intentSelectedRoundsString = intent.getStringExtra("selectedRounds")

        val selectedStudyOff = intent.getStringExtra("selectedStudyOff")

        val studyOnMinutes = selectedStudyOn?.let { extractNumberFromString(it) } ?: 0
        val restartTimer = intent.getBooleanExtra("restartTimer", false)

        val intentSelectedRounds = intentSelectedRoundsString?.toIntOrNull() ?: 0


        if (savedInstanceState != null) {
            roundNumber = savedInstanceState.getInt("roundNumber", 1)

        }

        updateRoundCounterText()

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
                val gotoBreak= Intent(this@TimerActivity,BreakActivity::class.java)
                gotoBreak.putExtra("selectedStudyOff", selectedStudyOff)
                startActivity(gotoBreak)
                roundNumber++
            }

        )


        // Start the countdown timer
        countdownTimer.start()

        exitButton.setOnClickListener {
            // Current Time End: Get the time when user end the session
            val currentTimeEnd = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            // Week Number
            val calendar = Calendar.getInstance()
            val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)

            // Week Monday: Get the date of the Monday for the current week
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val weekMonday = simpleDateFormat.format(calendar.time)
            insertDataIntoDatabase(weekNumber, weekMonday, currentTimeEnd, roundNumber)
            // Navigate back to the home fragment or activity
            finish()



        }

        if (roundNumber > intentSelectedRounds) {
            countdownTimer.cancel()
            roundNumber--;
            val congratsIntent = Intent(this@TimerActivity, CongratsActivity::class.java)

            startActivity(congratsIntent)


        }

        // Initialize the welcome back dialog
        welcomeBackDialog = Dialog(this)
        welcomeBackDialog.setContentView(R.layout.welcome_back_dialog)
        welcomeBackDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val okButton: Button = welcomeBackDialog.findViewById(R.id.okButton)
        okButton.setOnClickListener {
            welcomeBackDialog.dismiss()
        }




    }


    private fun updateRoundCounterText() {
        roundCounter.text = getString(R.string.round_counter, roundNumber)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current round number to restore it later
        outState.putInt("roundNumber", roundNumber)
    }
    private fun insertDataIntoDatabase(weekNumber: Int, weekMonday: String, currentTimeEnd: String, roundNumber: Int) {
        // OLD DATA FROM SetStudyGoals -----------------------------
        val currentDate = intent.getStringExtra("currentDate")?: ""
        val studyGoal = intent.getStringExtra("studyGoal")?: ""
        val selectedSubject = intent.getStringExtra("selectedSubject")?: ""
        val selectedStudyOn = intent.getStringExtra("selectedStudyOn")?: ""
        val selectedStudyOff = intent.getStringExtra("selectedStudyOff")?: ""
        val currentTimeStart = intent.getStringExtra("currentTimeStart")?: ""
        val selectedRounds = intent.getStringExtra("selectedRounds")?: ""
        // NEW DATA ------------------------------------------------
        // 1) Week Number
        // 2) Week Monday Date
        // 3) DurationInHours: Convert the duration from milliseconds to hours as follows
        val startMillis = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(currentTimeStart)?.time ?: 0
        val endMillis = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(currentTimeEnd)?.time ?: 0
        val durationInMillis = endMillis - startMillis
        val durationInHours = TimeUnit.MILLISECONDS.toHours(durationInMillis).toInt()
        // 4) Time Range: Fill in Time Range based on collected times as follows
        val timeRange = "$currentTimeStart - $currentTimeEnd"
        // 5) Round Number

        // INSERT TO MAIN DATABASE ----------------------------------
        mainDatabase.insertTableTaskDetails(
                weekNumber,
                weekMonday,
                currentDate,
                studyGoal,
                selectedSubject,
                selectedStudyOn,
                selectedStudyOff,
                currentTimeStart,
                currentTimeEnd,
                timeRange,
                duration = durationInHours,
                roundNumber
        )
        Log.d("MainDatabase", "TimerActivity inserted data of $currentDate to TABLE_TASK_DETAILS")
        // DELETE OUTDATED DATE
        // mainDatabase.deleteOutdatedTasks(fromDate = currentDate)
        // mainDatabase.getTasksForLast7Days(fromDate = currentDate)
        // mainDatabase.getTotalDurationByCategoryLast7Days(fromDate = currentDate)
        val taskInfo7 = mainDatabase.getTasksForDate(currentDate)
        Log.d("MainDatabase", "TimerActivity getTasksForLast7Days() TASK INFO7: $taskInfo7")
        val taskDuration7 = mainDatabase.getTotalDurationByCategoryLast7Days(currentDate)
        Log.d("MainDatabase", "TimerActivity getTotalDurationByCategoryLast7Days: $taskDuration7")
    }

    override fun onDestroy() {
        // Cancel the timer to avoid memory leaks
        super.onDestroy()
        countdownTimer.cancel()
    }

    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun restartTimer() {
        // to be called if user swipes out at any moment
        countdownTimer.cancel()
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


    /* ==============================================================================
    *
    *   NOTIFICATION LOGIC
    *
    ================================================================================= */

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // User swiped out of the app, show notification
        showSwipeAwayNotification()
        restartTimer()
    }

    private fun showSwipeAwayNotification() {
        val notificationIntent = Intent(this, TimerActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val soundUri = Uri.parse("android.resource://${packageName}/${R.raw.womp_womp}")

        // Create a notification builder
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.pomodoro_logo)
            .setContentTitle("Pomodoro Timer")
            .setContentText("Your timer was reset to 0. Get back to work!")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority to heads-up
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(contentIntent)
            .setFullScreenIntent(contentIntent, true) // Set full screen intent
            .setAutoCancel(true)
            .setSound(soundUri)

        // Optionally, add a custom sound
        builder.setSound(Uri.parse("android.resource://${packageName}/${R.raw.womp_womp}"))

        // Show the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
        welcomeBackDialog.show()
    }


    private fun createNotificationChannel() {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications for timer events"
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}