package com.example.shiftmateOPSC

data class TaskHrs (
    val category: String = "", // Default values to avoid uninitialized properties
    val totalHours: Int = 0
) {
    // No-argument constructor
    constructor() : this("", 0)
}
