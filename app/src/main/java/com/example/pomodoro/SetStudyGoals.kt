package com.example.pomodoro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SetStudyGoals : Fragment() {

    private lateinit var studyGoalEditText: EditText
    private lateinit var subjectSpinner: Spinner
    private lateinit var studyOnSpinner: Spinner
    private lateinit var studyOffSpinner: Spinner
    private lateinit var roundsSpinner: Spinner
    private lateinit var timeFinishTextView: TextView

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

            // Log or save the selected values (You can replace this with your desired functionality)
            logSelectedValues(studyGoal, selectedSubject, selectedStudyOn, selectedStudyOff, selectedRounds)

            // Calculate and set the time finish
            calculateAndSetTimeFinish(selectedStudyOn, selectedStudyOff, selectedRounds)

           val intent= Intent(requireActivity(),TimerActivity::class.java).apply {
               putExtra("studyGoal", studyGoal)
               putExtra("selectedSubject", selectedSubject)
               putExtra("selectedStudyOn", selectedStudyOn)
               putExtra("selectedStudyOff", selectedStudyOff)
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

            // Calculate and set the time finish without logging
            calculateAndSetTimeFinish(selectedStudyOn, selectedStudyOff, selectedRounds)
        }

        return view
    }

    private fun logSelectedValues(studyGoal: String, subject: String, studyOn: String, studyOff: String, rounds: String) {
        // Replace this with your logic to save or log the selected values
        // For now, we'll print them to the console
        println("Study Goal: $studyGoal")
        println("Subject: $subject")
        println("Study On: $studyOn")
        println("Study Off: $studyOff")
        println("Rounds: $rounds")
    }

    private fun calculateAndSetTimeFinish(studyOn: String, studyOff: String, rounds: String) {
        val studyOnMinutes = extractNumberFromString(studyOn)
        val studyOffMinutes = extractNumberFromString(studyOff)
        val roundsCount = extractNumberFromString(rounds)

        val currentTime = Calendar.getInstance()

        // Calculate the total study time in minutes
        val totalStudyTime = (studyOnMinutes + studyOffMinutes) * roundsCount

        // Add the total study time to the current time
        currentTime.add(Calendar.MINUTE, totalStudyTime)

        val timeFinishFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeFinish = timeFinishFormat.format(currentTime.time)

        // Set the calculated time finish in the TextView
        timeFinishTextView.text = timeFinish
    }




    private fun extractNumberFromString(timeString: String): Int {
        // Assuming the timeString is in the format "X minutes"
        val regex = """(\d+) minutes""".toRegex()
        val matchResult = regex.find(timeString)

        return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }


}
