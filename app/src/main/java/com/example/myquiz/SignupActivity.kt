package com.example.myquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.myquiz.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if(password.length > 3){
                signUpWithFirebase(email, password)
            }

        }

    }

    fun signUpWithFirebase(email : String, password : String){

        binding.pb.visibility = View.VISIBLE
        binding.btnSignup.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_LONG).show()
                binding.pb.visibility = View.INVISIBLE
                binding.btnSignup.isClickable = true
                finish()
            }
            else{
                binding.pb.visibility = View.INVISIBLE
                binding.btnSignup.isClickable = true
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG).show()
            }

        }

    }
}