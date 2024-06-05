package com.example.shiftmateOPSC

data class ChatMessage(
    val userId: String = "",
    val userName: String = "",
    val message: String = "",
    val timestamp: Long = 0
)