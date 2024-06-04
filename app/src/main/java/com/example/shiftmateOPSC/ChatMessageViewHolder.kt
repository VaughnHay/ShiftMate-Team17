package com.example.shiftmateOPSC

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
    val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
}