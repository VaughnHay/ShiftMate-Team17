package com.example.shiftmateOPSC

import java.util.Date

class Task {

    data class Task(
        val name: String,
        val deadline: Date,
        val duration: String,
        val isCompleted: Boolean
    )
}