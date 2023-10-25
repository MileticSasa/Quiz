package com.example.myquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myquiz.databinding.ActivityScoresBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ScoresActivity : AppCompatActivity() {

    lateinit var binding : ActivityScoresBinding

    val database = FirebaseDatabase.getInstance()
    val dbReference = database.reference.child("scores")
    val currentUser = FirebaseAuth.getInstance().currentUser

    var userCorrect = ""
    var userWrong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getting results
        dbReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                currentUser?.let {
                    val userId = it.uid

                    userCorrect = snapshot.child(userId).child("correct").value.toString()
                    userWrong = snapshot.child(userId).child("wrong").value.toString()

                    binding.tvCorrect.text = userCorrect
                    binding.tvWrong.text = userWrong
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.btnPlayAgain.setOnClickListener {
            val intent = Intent(this@ScoresActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnExit.setOnClickListener {
           finish()
        }
    }
}