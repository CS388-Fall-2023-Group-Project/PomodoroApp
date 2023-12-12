package com.example.pomodoro

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DummyDataGenerator(private val context: Context) {

    private val database: MainDatabase = MainDatabase(context)

    private val subjects = listOf("Math", "Reading", "Chemistry", "Computing", "Spanish", "History")

    fun generateDummyDataFor7Days() {
        val currentDate = Calendar.getInstance()

        for (i in 1..7) {
            val date = SimpleDateFormat("yyyy-MM-d", Locale.US).format(currentDate.time)
            val taskName = "Task $i"
            val subject = getRandomSubject()
            val studyOn = "Study On $i"
            val studyOff = "Study Off $i"
            val currentTimeStart = "Start Time $i"
            val currentTimeEnd = "End Time $i"
            val timeRange = "00:00 AM - 00:00 AM"
            val duration = (1..3).random()
            val rounds = (1..3).random()

            database.insertTableTaskDetails(
                date, taskName, subject, studyOn, studyOff,
                currentTimeStart, currentTimeEnd, timeRange, duration, rounds
            )

            // Move to the previous day
            currentDate.add(Calendar.DAY_OF_MONTH, -1)
            Log.d("DummyDataGenerator", "Generated data for $date: $taskName, $subject, $duration minutes")

        }
    }

    private fun getRandomSubject(): String {
        return subjects.random()
    }
}