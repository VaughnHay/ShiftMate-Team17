package com.example.shiftmateOPSC

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter2 (private val tasks: List<Task22>) :
    RecyclerView.Adapter<TaskAdapter2.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val hoursTextView: TextView = itemView.findViewById(R.id.hoursTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.categoryTextView.text = currentTask.category
        holder.hoursTextView.text = "Total Hours: ${currentTask.totalHours}"
    }

    override fun getItemCount() = tasks.size


}