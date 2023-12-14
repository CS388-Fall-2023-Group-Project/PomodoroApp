package com.example.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AnalyticsFragment : Fragment() {
    private lateinit var dbHelper: MainDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =   inflater.inflate(R.layout.fragment_analytics, container, false)

        // Assume you have a TextView with the ID "textView" in your fragment layout
        val textView = view.findViewById<TextView>(R.id.t2)
if (streak!= 0 ) {
    // Change the text of the TextView
    textView.text = "You Have a"+ "Streak"
}
        return view

        // Inflate the layout for this fragment




    }
    val streak=0
    val timeList2 = mutableListOf<Float>()
    val timeList = mutableListOf<Int>()
    val CategoryList= mutableListOf(0F,0F,0F,0F)
    val WeekList= mutableListOf(0F, 0F, 0F, 0F, 0F, 0F, 0F)
    val Past7days= mutableListOf<String>()
    // this is a comment to teach about git
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Get the current date
        val currentDate = LocalDate.now()

        // Create a list to store past dates
        val pastDates = mutableListOf<LocalDate>()

        // Iterate over the last 7 days (including today)
        for (i in 0..6) {
            val pastDate = currentDate.minusDays(i.toLong())
            pastDates.add(pastDate)
        }

        // Print the list of past dates
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        pastDates.forEach {
            Past7days.add(it.format(formatter))
        }


        super.onViewCreated(view, savedInstanceState)
        dbHelper = MainDatabase(requireContext())
        val day7Stats = dbHelper.getTasksForLast7Days()
        val categoryInfo= dbHelper.calculateTotalDurationBySubject("2023-12-7")
       for( task in day7Stats){
           val taskID = task.id

           val name = task.task
           val totalTime= task.totalDuration
           if (totalTime != null) {
               timeList.add(totalTime)
           }
       }
        for(task in categoryInfo){

           val category = task.key
            val total7Time= task.value

            timeList2.add(total7Time.toFloat())
        }

        for(list in timeList){
        if ( list.toInt() == 0) {
            WeekList.add(0,0F)
            WeekList.removeAt(6)
        }
            if ( list.toDouble() == 0.25) {
                WeekList.add(0,0.25F)
                WeekList.removeAt(6)
            }
            if ( list.toDouble() == 0.5) {
                WeekList.add(0,0.5F)
                WeekList.removeAt(6)
            }
            if ( list.toInt() == 1) {
                WeekList.add(0,1F)
                WeekList.removeAt(6)
            }
            if ( list.toInt() == 2) {
                WeekList.add(0,2F)
                WeekList.removeAt(6)
            }
            if ( list.toInt() == 3) {
                WeekList.add(0,3F)
                WeekList.removeAt(6)
            }
            if ( list.toInt() == 4) {
                WeekList.add(0,4F)
                WeekList.removeAt(6)
            }
            if ( list.toInt() == 5) {
                WeekList.add(0,5F)
                WeekList.removeAt(6)
            }
            if ( list.toInt() == 6) {
                WeekList.add(0,6F)
                WeekList.removeAt(6)
            }
            if ( list.toInt() == 7) {
                WeekList.add(0,7F)
                WeekList.removeAt(6)
            }

        }
        var i=0
        for(list in timeList2){
            if (i<= 3){
                CategoryList.removeAt(i)
                if ( list.toInt() == 1) {
                    CategoryList.add(i,1F)
                }
                if ( list.toInt() == 2) {
                    CategoryList.add(i,2F)
                }
                if ( list.toInt() == 3) {
                    CategoryList.add(i,3F)
                }
                if ( list.toInt() == 4) {
                    CategoryList.add(i,4F)
                }
                if ( list.toInt() == 5) {
                    CategoryList.add(i,5F)
                }
                if ( list.toInt() == 6) {
                    CategoryList.add(i,6F)
                }
                if ( list.toInt() == 7) {
                    CategoryList.add(i,7F)
                }


            }
            i++
        }
        setupHorizontalChart(view)
        setupBarChart(view)
        setupBarPercentChart(view)
    }

    // Comment
    private fun setupHorizontalChart(view: View) {
        val data = horizontalChartData(
            requireContext(), HorizontalChartXLabels(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "Chemistry",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "Computing",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "History",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)!!,
                "Math"
            ),CategoryList
        )
        val horizontalBarChartViewBrushingDigit =
            view.findViewById<HorizontalBarChartView>(R.id.textView)
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
                Past7days.elementAt(6), Past7days.elementAt(5), Past7days.elementAt(4), Past7days.elementAt(3), Past7days.elementAt(2), Past7days.elementAt(1), Past7days.elementAt(0)
            ), WeekList
        )
        val barChartViewBrushing =
            view.findViewById<BarChartView>(R.id.barChartViewBrushing)
        barChartViewBrushing.animate(data)
    }
}
