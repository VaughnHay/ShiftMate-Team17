package com.example.shiftmateOPSC

/*class TaskDetails : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private var minGoal: Int = 0
    private var maxGoal: Int = 0
    private var userHours: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_details_layout)

       // progressBar = findViewById(R.id.taskDetprogressBar1)

        fetchUserGoalsAndData()
    }

    private fun fetchUserGoalsAndData() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        currentUserID?.let { uid ->
            val userGoalsRef = FirebaseDatabase.getInstance().getReference("UserGoals").child(uid)
            userGoalsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    minGoal = snapshot.child("minGoal").getValue(Int::class.java) ?: 0
                    maxGoal = snapshot.child("maxGoal").getValue(Int::class.java) ?: 0
                    fetchUserHours(uid)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    private fun fetchUserHours(uid: String) {
        val userHoursRef = FirebaseDatabase.getInstance().getReference("UserHours").child(uid)
        userHoursRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalHours = 0
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDateString = dateFormat.format(currentDate)

                for (data in snapshot.children) {
                    val date = data.key
                    val hours = data.getValue(Int::class.java) ?: 0

                    if (isWithinLastMonth(date, currentDateString)) {
                        totalHours += hours
                    }
                }

                userHours = totalHours
                updateProgressBar()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun isWithinLastMonth(dateString: String?, currentDateString: String): Boolean {
        if (dateString == null) return false
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString) ?: return false
        val currentDate = dateFormat.parse(currentDateString) ?: return false

        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.MONTH, -1)
        val lastMonthDate = calendar.time

        return date.after(lastMonthDate) && date.before(currentDate)
    }

    private fun updateProgressBar() {
        val progress = ((userHours - minGoal).toFloat() / (maxGoal - minGoal).toFloat() * 100).toInt()
        progressBar.progress = progress
    }
}
*///