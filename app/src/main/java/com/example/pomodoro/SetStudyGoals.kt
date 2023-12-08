package com.example.pomodoro

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SetStudyGoals : Fragment() {

    private val sharedViewModel: SharedViewModel by viewModels({ requireActivity() })

    private lateinit var studyGoalEditText: EditText
    private lateinit var subjectSpinner: Spinner
    private lateinit var studyOnSpinner: Spinner
    private lateinit var studyOffSpinner: Spinner
    private lateinit var roundsSpinner: Spinner
    private lateinit var timeFinishTextView: TextView

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.set_study_goals, container, false)

        // Initialize your UI components
        studyGoalEditText = view.findViewById(R.id.editTextStudyGoal)
        subjectSpinner = view.findViewById(R.id.subjectSpinner)
        studyOnSpinner = view.findViewById(R.id.studyOn)
        studyOffSpinner = view.findViewById(R.id.studyOff)
        roundsSpinner = view.findViewById(R.id.rounds)
        timeFinishTextView = view.findViewById(R.id.timeFinish)

        val startStudyingButton: TextView = view.findViewById(R.id.startStudyingButton)
        val calculateStudyTimeButton: TextView = view.findViewById(R.id.calculateStudyTime)

        // Set a click listener for the "START STUDYING" button
        startStudyingButton.setOnClickListener {
            // Get the selected values from the UI components
            val studyGoal = studyGoalEditText.text.toString()
            val selectedSubject = subjectSpinner.selectedItem.toString()
            val selectedStudyOn = studyOnSpinner.selectedItem.toString()
            val selectedStudyOff = studyOffSpinner.selectedItem.toString()
            val selectedRounds = roundsSpinner.selectedItem.toString()

            val currentDate = SimpleDateFormat("yyyy-MM-d", Locale.getDefault()).format(Date())
            val currentTimeStart = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

            // -------------------- INITIALIZE INTENT --------------------
            // Log or save the selected values (You can replace this with your desired functionality)
            logSelectedValues(studyGoal, selectedSubject, selectedStudyOn, selectedStudyOff, selectedRounds)

            // Calculate and set the time finish
            calculateAndSetTimeFinish(selectedStudyOn, selectedStudyOff, selectedRounds)

            val intent= Intent(requireActivity(),TimerActivity::class.java).apply {
                putExtra("currentDate", currentDate)
                putExtra("studyGoal", studyGoal)
                putExtra("selectedSubject", selectedSubject)
                putExtra("selectedStudyOn", selectedStudyOn)
                putExtra("selectedStudyOff", selectedStudyOff)
                putExtra("currentTimeStart", currentTimeStart)
                putExtra("selectedRounds", selectedRounds)
            }
            startActivity(intent)


        }

        // Set a click listener for the "Tap to calculate Study Time" button
        calculateStudyTimeButton.setOnClickListener {
            // Get the current selected values from the UI components
            val selectedStudyOn = studyOnSpinner.selectedItem.toString()
            val selectedStudyOff = studyOffSpinner.selectedItem.toString()
            val selectedRounds = roundsSpinner.selectedItem.toString()
            println("Calculating time with studyOn: $selectedStudyOn, studyOff: $selectedStudyOff, rounds: $selectedRounds")

            // Calculate and set the time finish without logging
            calculateAndSetTimeFinish(selectedStudyOn, selectedStudyOff, selectedRounds)
        }

        return view
    }

    private fun logSelectedValues(studyGoal: String, subject: String, studyOn: String, studyOff: String, rounds: String) {
        println("Study Goal: $studyGoal")
        println("Subject: $subject")
        println("Study On: $studyOn")
        println("Study Off: $studyOff")
        println("Rounds: $rounds")
    }

    private fun calculateAndSetTimeFinish(studyOn: String, studyOff: String, rounds: String) {
        var message = "";
        val studyOnMinutes = extractNumberFromString(studyOn)
        println("Type of studyOnMinutes: ${studyOnMinutes::class.java}")
        val studyOffMinutes = extractNumberFromString(studyOff)
        println("This is studyoff minutes [$studyOffMinutes]")
        val roundsCount = extractNumberFromString(rounds)
        println("This is round  [$roundsCount]")
        println("This is roundCount  [${roundsCount::class.java}]")


        // Calculate the total study time in minutes
        val totalStudyTime = (studyOnMinutes + studyOffMinutes) * roundsCount
        println("This is total [$totalStudyTime]")
        // finishing time
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.MINUTE, totalStudyTime)

        // Format the time in 12-hour format
        val timeFinishFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeFinish = timeFinishFormat.format(endTime.time)
        println(timeFinish)
        // Display the message with the calculated time
        message = getString(R.string.finish_message, timeFinish)
        timeFinishTextView.text = message
    }

    private fun extractNumberFromString(input: String): Int {
        // Assuming the timeString is in the format "X minutes"
        val regex = """^(\d+)""".toRegex()
        val matchResult = regex.find(input)

        return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }

}