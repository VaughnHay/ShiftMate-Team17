package com.example.shiftmateOPSC

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Chat : AppCompatActivity() {

    private lateinit var chatMessages: MutableList<ChatMessage>
    private lateinit var adapter: ChatAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_layout)

        chatMessages = mutableListOf()
        adapter = ChatAdapter(chatMessages)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("messages")

        // Load messages from Firebase
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                chatMessage?.let {
                    chatMessages.add(it)
                    adapter.notifyItemInserted(chatMessages.size - 1)
                    recyclerView.scrollToPosition(chatMessages.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        val editTextMessage = findViewById<EditText>(R.id.editTextMessage)
        val imageViewSend = findViewById<ImageView>(R.id.imageViewSend)
        imageViewSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            val user = auth.currentUser
            if (message.isNotEmpty() && user != null) {
                val chatMessage = ChatMessage(user.uid, user.displayName ?: "User", message, System.currentTimeMillis())
                database.push().setValue(chatMessage)
                editTextMessage.text.clear()
            }
        }

        val backButton = findViewById<Button>(R.id.chatBackBut)
        backButton.setOnClickListener {
            finish()
        }
    }
}
