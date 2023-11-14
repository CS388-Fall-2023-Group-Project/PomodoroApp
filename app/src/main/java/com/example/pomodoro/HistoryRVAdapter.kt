package com.example.pomodoro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Random

class HistoryRVAdapter (private val foodList: ArrayList<TaskInfo>) :
    RecyclerView.Adapter<HistoryRVAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var taskList: ArrayList<TaskInfo> = ArrayList()
        val taskName = itemView.findViewById<TextView>(R.id.taskTextView)
        val taskCat = itemView.findViewById<TextView>(R.id.categoryTextView)
        val duration = itemView.findViewById<TextView>(R.id.durationTextView)
        val timeRange = itemView.findViewById<TextView>(R.id.timeRangeTextView)
    }
    // For recyclerView to create a new view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    // For RV to populate the views within a ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tracker = foodList[position]
        holder.taskName.text = tracker.task
        holder.taskCat.text = tracker.category
        holder.timeRange.text = tracker.timerange
        holder.duration.text = tracker.duration.toString()
    }

    override fun getItemCount() = foodList.size
    fun clearData() {
        foodList.clear()
        notifyDataSetChanged()
    }

    // Add this method to update the dataset
    fun updateData(newData: ArrayList<TaskInfo>) {
        foodList.clear()
        foodList.addAll(newData)
        notifyDataSetChanged()
    }
}
data class TaskInfo (
    var id: Int = 1,
    var task: String = "",
    var category: String = "",
    var timerange: String = "",
    var duration: Int? = null
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}

