package com.example.shiftmateOPSC

import android.graphics.Bitmap
import com.google.firebase.database.Exclude
    data class Task(
        val name: String = "",
        val duration: String = "",
        val isComplete: Boolean = false,
        val date: String = "",
        @Exclude val imageBitmap: Bitmap? = null) {
    }
