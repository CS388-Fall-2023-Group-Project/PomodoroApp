package com.example.pomodoro


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.example.pomodoro.utils.HorizontalChartXLabels
import com.example.pomodoro.utils.VerticalChartXLabels
import com.example.pomodoro.utils.horizontalChartData
import com.example.pomodoro.utils.verticalChartData

import com.straiberry.android.charts.tooltip.PointTooltip
import com.straiberry.android.charts.view.HorizontalBarChartView
import java.util.Calendar
import java.util.Date



class Analytics : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setupHorizontalChart()
        setupBarChart()
        setupBarPercentChart()
    }

    private fun setupHorizontalChart() {

        val data = horizontalChartData(
            this, HorizontalChartXLabels(
                ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)!!, "History",
                ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)!!, "English",
                ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)!!, "Math",
                ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)!!, "Science"
            ), listOf(6F, 7F, 2F, 6F)
        )
        val horizontalBarChartViewBrushingDigit =
            findViewById<HorizontalBarChartView>(R.id.horizontalBarChartViewBrushingDigit)
        horizontalBarChartViewBrushingDigit.animate(data)
    }

    private fun setupBarPercentChart() {
        // Prepare the tooltip to show on chart
        val pointTooltip = PointTooltip()

    }

    private fun setupBarChart() {
        val currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate


        val data = verticalChartData(
            this, VerticalChartXLabels(
                "Monday", "Tuesday", "Wensdasy", "Thursday", "Friday", "Saturday", "Sunday"
            ), listOf(6F, 7F, 2F, 6F, 7F, 10F, 20F)
        )

    }


}