package com.example.myquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.myquiz.databinding.ActivityWellcomeBinding

class WellcomeActivity : AppCompatActivity() {

    lateinit var binding : ActivityWellcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWellcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        alphaAnimation.duration = 4000

        binding.textView.startAnimation(alphaAnimation)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable{
            override fun run() {
                val intent = Intent(this@WellcomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 5000)
    }
}