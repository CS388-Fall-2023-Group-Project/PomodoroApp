package com.example.pomodoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Fragment_History : Fragment() {

    private lateinit var dbHelper: Database_Example
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dbHelper = Database_Example(requireContext())
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView)

        // RecyclerView setup
        historyAdapter = HistoryRVAdapter()
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyRecyclerView.adapter = historyAdapter

        // DATE CLICKS
        val calendarView: CalendarView = view.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(requireContext(), "Selected Date: $formattedDate", Toast.LENGTH_SHORT).show()
            // Retrieve tasks for the selected date from the database
            val taskInfo = dbHelper.getTasksForDate(formattedDate)
            // Update the RecyclerView with the retrieved tasks
            historyAdapter.updateData(taskInfo)
        }

    }

}

