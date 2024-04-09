package com.example.shiftmateOPSC

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter {
    class TaskAdapter(private val tasks: List<Task>) :
        RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
            return TaskViewHolder(view)
        }

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val taskNameTextView: TextView = itemView.findViewById(R.id.taskNameTextView)
            private val taskDurationTextView: TextView = itemView.findViewById(R.id.taskDurationTextView)

            fun bind(task: Task) {
                taskNameTextView.text = task.name
                taskDurationTextView.text = task.duration

            }
        }
    }

}