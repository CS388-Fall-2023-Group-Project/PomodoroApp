package com.example.pomodoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pomodoro.utils.VerticalChartXLabels
import com.example.pomodoro.utils.verticalChartData
import com.straiberry.android.charts.tooltip.PointTooltip
import com.straiberry.android.charts.view.BarChartView
import com.straiberry.android.charts.view.BarPercentChartView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class Fragment_AnalyticsStrawberry : Fragment() {

    private lateinit var mainDatabase: MainDatabase
    private var currentStreak: Int = 0
    private val WeekList= mutableListOf(0F, 0F, 0F, 0F, 0F, 0F, 0F)
    private val Past7days= mutableListOf<String>()
    private val CategoryList= mutableListOf(0F,0F,0F,0F)

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

        // Set fixed values for each bar
        WeekList[0] = 2.0F
        WeekList[1] = 4.0F
        WeekList[2] = 1.5F
        WeekList[3] = 3.0F
        WeekList[4] = 5.0F
        WeekList[5] = 2.5F
        WeekList[6] = 4.5F


        setupBarChart(view)
        setupBarPercentChart(view)

    }

    private fun updateStreak() {
        val textViewStreak: TextView = requireView().findViewById(R.id.t2)
        textViewStreak.text = "🔥🔥 Your on a $currentStreak Study Streak!!! 🔥🔥"
    }

    private fun setupBarChart(view: View) {
        val currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        val data = verticalChartData(
            requireContext(), VerticalChartXLabels(
                Past7days.elementAt(6), Past7days.elementAt(5), Past7days.elementAt(4), Past7days.elementAt(3), Past7days.elementAt(2), Past7days.elementAt(1), Past7days.elementAt(0)
            ), WeekList
        )
        val barChartViewBrushing =
            view.findViewById<BarChartView>(R.id.barChartViewBrushing)
        barChartViewBrushing.animate(data)
    }

    private fun setupBarPercentChart(view: View) {
        // Prepare the tooltip to show on chart
        val pointTooltip = PointTooltip()
        pointTooltip.onCreateTooltip(view.findViewById(R.id.parentView))

        val barPercentChartWhitening =
            view.findViewById<BarPercentChartView>(R.id.barPercentChartWhitening)

        barPercentChartWhitening.apply {
            tooltip = pointTooltip
            currentAverage = 20
            average = 50
            previousAverage = 30
            createBarPercent()
            disableTouchAndClick()
        }
    }

}