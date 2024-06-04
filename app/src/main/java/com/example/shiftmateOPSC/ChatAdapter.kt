package com.example.shiftmateOPSC

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatMessages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatMessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        holder.userNameTextView.text = chatMessage.userName
        holder.messageTextView.text = chatMessage.message
        holder.timestampTextView.text = android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.timestamp)
    }

    override fun getItemCount(): Int = chatMessages.size
}