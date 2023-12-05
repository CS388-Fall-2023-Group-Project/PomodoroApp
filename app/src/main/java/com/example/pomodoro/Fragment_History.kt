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
            val formattedDate = "$year-${month + 1}-$dayOfMonth"
            // Retrieve tasks for the selected date from the database
            val taskInfo = dbHelper.getTasksForDate(formattedDate)
            Log.d("MainDatabase", "Fragment_History getTasksForDate: $formattedDate")
            Log.d("MainDatabase", "Fragment_History getTasksForDate TASK DETAILS: $taskInfo")
            historyAdapter.updateData(taskInfo)

        }
    }


}

