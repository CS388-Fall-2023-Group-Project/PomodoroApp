package com.example.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pomodoro.utils.HorizontalChartXLabels
import com.example.pomodoro.utils.VerticalChartXLabels
import com.example.pomodoro.utils.horizontalChartData
import com.example.pomodoro.utils.verticalChartData
import com.straiberry.android.charts.tooltip.PointTooltip
import com.straiberry.android.charts.view.BarChartView
import com.straiberry.android.charts.view.BarPercentChartView
import com.straiberry.android.charts.view.HorizontalBarChartView
import java.util.Calendar
import java.util.Date

class AnalyticsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analytics, container, false)
    }

    // this is a comment to teach about git
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHorizontalChart(view)
        setupBarChart(view)
        setupBarPercentChart(view)
    }

    // Comment
    private fun setupHorizontalChart(view: View) {
        val data = horizontalChartData(
            requireContext(), HorizontalChartXLabels(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "History",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "English",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "Math",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "Science"
            ), listOf(6F, 7F, 2F, 6F)
        )
        val horizontalBarChartViewBrushingDigit =
            view.findViewById<HorizontalBarChartView>(R.id.horizontalBarChartViewBrushingDigit)
        horizontalBarChartViewBrushingDigit.animate(data)
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

    private fun setupBarPercentChart() {
        // Prepare the tooltip to show on chart
        val pointTooltip = PointTooltip()
        // Add your tooltip setup code here
    }

    private fun setupBarChart(view: View) {
        val currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        val data = verticalChartData(
            requireContext(), VerticalChartXLabels(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
            ), listOf(6F, 7F, 2F, 6F, 7F, 7F, 7F)
        )
        val barChartViewBrushing =
            view.findViewById<BarChartView>(R.id.barChartViewBrushing)
        barChartViewBrushing.animate(data)
    }
}
