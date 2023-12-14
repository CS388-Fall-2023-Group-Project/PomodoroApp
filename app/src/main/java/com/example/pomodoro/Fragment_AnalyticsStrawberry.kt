package com.example.pomodoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Fragment_AnalyticsStrawberry : Fragment() {

    private lateinit var mainDatabase: MainDatabase
    private var currentStreak: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analytics_strawberry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainDatabase = MainDatabase(requireContext())

        currentStreak = mainDatabase.calculateStudyStreak()
        updateStreak()

    }

    private fun updateStreak() {
        val textViewStreak: TextView = requireView().findViewById(R.id.t2)
        textViewStreak.text = "🔥🔥 Your on a $currentStreak Study Streak!!! 🔥🔥"
    }

}