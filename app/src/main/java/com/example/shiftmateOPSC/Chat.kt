package com.example.shiftmateOPSC

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Chat : AppCompatActivity() {

    private lateinit var chatMessages: MutableList<ChatMessage>
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_layout)

        chatMessages = mutableListOf(
            ChatMessage("user1", "User One", "Hello, World!", System.currentTimeMillis()),
            ChatMessage("user2", "User Two", "Hi, User One!", System.currentTimeMillis())
        )

        adapter = ChatAdapter(chatMessages)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val editTextMessage = findViewById<EditText>(R.id.editTextMessage)
        val imageViewSend = findViewById<ImageView>(R.id.imageViewSend)
        imageViewSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            if (message.isNotEmpty()) {
                chatMessages.add(ChatMessage("user1", "User One", message, System.currentTimeMillis()))
                adapter.notifyItemInserted(chatMessages.size - 1)
                recyclerView.scrollToPosition(chatMessages.size - 1)
                editTextMessage.text.clear()
            }
        }

        val backButton = findViewById<Button>(R.id.chatBackBut)
        backButton.setOnClickListener {
            finish()
        }
    }
}
