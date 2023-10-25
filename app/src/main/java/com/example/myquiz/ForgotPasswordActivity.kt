package com.example.myquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myquiz.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding : ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRecover.setOnClickListener {
            val email = binding.etEmail.text.toString()

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext, "We sent a password reset email to your email address", Toast.LENGTH_LONG).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}