package com.example.shiftmateOPSC

class Task {

    val name: CharSequence?
        get() {
            TODO()
        }

    val duration: CharSequence?
        get(){
            TODO()
        }


    data class Task(val name: String,
                    val duration: String,
                    val isComplete: Boolean)

}