package com.example.shiftmateOPSC

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class View : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var hourAdapter: HourAdapter
    private var list: ArrayList<Users2> = ArrayList()
   // private lateinit var backcButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view)

        recyclerView = findViewById(R.id.HtimeRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("View");
        recyclerView.setHasFixedSize(true);
        recyclerView.layoutManager = LinearLayoutManager(this)
        hourAdapter = HourAdapter(this, list)
        recyclerView.adapter = hourAdapter
       // backcButton = findViewById(R.id.Back2mainbtn)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val categoryHour: Users2? = dataSnapshot.getValue(Users2::class.java)
                    categoryHour?.let { list.add(it) }
                }
                hourAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

    }

    // Function to enable edge-to-edge display
    private fun enableEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(android.R.id.content)
        ) { _, insets ->
            insets?.consumeSystemWindowInsets()
            insets
        }
    }

   /*  backcButton.setOnClickListener{
        finish()
    } */
}