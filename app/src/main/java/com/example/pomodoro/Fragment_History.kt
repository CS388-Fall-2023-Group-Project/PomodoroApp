package com.example.pomodoro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Fragment_History : Fragment() {

    private lateinit var dbHelper: MainDatabase
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: Adapter_HistoryRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = MainDatabase(requireContext())
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView)

        // RecyclerView setup
        historyAdapter = Adapter_HistoryRecyclerView()
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyRecyclerView.adapter = historyAdapter

        // DATE CLICKS
        val calendarView: CalendarView = view.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedDate = formatDate(year, month + 1, dayOfMonth)
            // Retrieve tasks for the selected date from the database
            val taskInfo = dbHelper.getTasksForDate(formattedDate)
            Log.d("MainDatabase", "Fragment_History getTasksForDate: $formattedDate")
            Log.d("MainDatabase", "Fragment_History getTasksForDate TASK DETAILS: $taskInfo")
            val taskInfo7 = dbHelper.getTasksForLast7Days()
            Log.d("MainDatabase", "Fragment_History getTasksForLast7Days: $taskInfo7")
            val streak = dbHelper.calculateStudyStreak()
            Log.d("MainDatabase", "Streak: $streak")
            historyAdapter.updateData(taskInfo)

        }
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


}

