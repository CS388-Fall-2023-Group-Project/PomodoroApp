package com.example.pomodoro

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Random

class Adapter_HistoryRecyclerView : RecyclerView.Adapter<Adapter_HistoryRecyclerView.ViewHolder>() {
    private var taskList: ArrayList<TaskInfo> = ArrayList()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName = itemView.findViewById<TextView>(R.id.taskTextView)
        val subject = itemView.findViewById<TextView>(R.id.categoryTextView)
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
        val tracker = taskList[position]
        holder.taskName.text = tracker.task
        holder.subject.text = tracker.subject
        holder.timeRange.text = tracker.timerange
        tracker.duration?.let {
            holder.duration.text = "$it hours"
        }
    }

    override fun getItemCount() = taskList.size
    fun clearData() {
        taskList.clear()
        notifyDataSetChanged()
    }

    // Add this method to update the dataset
    fun updateData(newData: List<TaskInfo>) {
        taskList.clear()
        taskList.addAll(newData)
        notifyDataSetChanged()
    }
}
data class TaskInfo (
    var id: Int = 1,
    var task: String = "",
    var subject: String = "",
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