package com.example.shiftmateOPSC

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shiftmateOPSC.TaskHrs

class TaskAdapterHrs(private val tasks: List<TaskHrs>) : RecyclerView.Adapter<TaskAdapterHrs.TaskViewHolder>() {
    // ViewHolder class for each item in the RecyclerView
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val totalHoursTextView: TextView = itemView.findViewById(R.id.totalHoursTextView)

        fun bind(task: TaskHrs) {
            categoryTextView.text = task.category
            totalHoursTextView.text = task.totalHours.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
