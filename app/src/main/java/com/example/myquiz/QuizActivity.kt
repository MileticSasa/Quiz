package com.example.myquiz

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myquiz.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {

    lateinit var binding : ActivityQuizBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val dbReference = database.reference.child("questions")
    val dbScoreReference = database.reference.child("scores")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    //var questionNumberr = 1 menjam ovo u nulu jer sad uzimam podatke iz seta, a u njemu je prvi element nulti element
    var questionNumberr = 0

    var userAnswer = ""
    var userCorrectAnsers = 0
    var userWrongAnswers = 0

    lateinit var timer : CountDownTimer
    private val totalTime = 25000L //time in milliseconds
    private val timeInterval = 1000L //1 second
    var timerRunning = false
    var timeLeft = totalTime

    val questions = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        do {
            val number = Random.nextInt(1, 6)
            questions.add(number)
        } while (questions.size < 3)

        gameSetup()

        binding.btnFinish.setOnClickListener {
            saveScore()
        }

        binding.btnNext.setOnClickListener {

            resetTimer()
            gameSetup()
        }

        binding.tvAnswer1.setOnClickListener {

            pauseTimer()

            userAnswer = "a"

            if(userAnswer == correctAnswer){
                binding.tvAnswer1.setBackgroundColor(Color.GREEN)
                userCorrectAnsers++
                binding.tvCorrect.text = userCorrectAnsers.toString()

            }
            else{
                binding.tvAnswer1.setBackgroundColor(Color.RED)
                userWrongAnswers++
                binding.tvWrong.text = userWrongAnswers.toString()
                findCorrectAnswer()
            }

            disableAnswers()
        }

        binding.tvAnswer2.setOnClickListener {

            pauseTimer()

            userAnswer = "b"

            if(userAnswer == correctAnswer){
                binding.tvAnswer2.setBackgroundColor(Color.GREEN)
                userCorrectAnsers++
                binding.tvCorrect.text = userCorrectAnsers.toString()

            }
            else{
                binding.tvAnswer2.setBackgroundColor(Color.RED)
                userWrongAnswers++
                binding.tvWrong.text = userWrongAnswers.toString()
                findCorrectAnswer()
            }

            disableAnswers()
        }

        binding.tvAnswer3.setOnClickListener {

            pauseTimer()

            userAnswer = "c"

            if(userAnswer == correctAnswer){
                binding.tvAnswer3.setBackgroundColor(Color.GREEN)
                userCorrectAnsers++
                binding.tvCorrect.text = userCorrectAnsers.toString()

            }
            else{
                binding.tvAnswer3.setBackgroundColor(Color.RED)
                userWrongAnswers++
                binding.tvWrong.text = userWrongAnswers.toString()
                findCorrectAnswer()
            }

            disableAnswers()
        }

        binding.tvAnswer4.setOnClickListener {

            pauseTimer()

            userAnswer = "d"

            if(userAnswer == correctAnswer){
                binding.tvAnswer4.setBackgroundColor(Color.GREEN)
                userCorrectAnsers++
                binding.tvCorrect.text = userCorrectAnsers.toString()

            }
            else{
                binding.tvAnswer4.setBackgroundColor(Color.RED)
                userWrongAnswers++
                binding.tvWrong.text = userWrongAnswers.toString()
                findCorrectAnswer()
            }

            disableAnswers()
        }
    }

    private fun gameSetup(){

        resetOptions()

        dbReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if(questionNumberr < questions.size){ //obrisao sam qustionNumber <= qustionCount
                    //ovo sam iskomentarisao jer je ovo za uzimanje svih pitanja iz baze redom
//                    question = snapshot.child(questionNumberr.toString()).child("q").value.toString()
//                    answerA = snapshot.child(questionNumberr.toString()).child("a").value.toString()
//                    answerB = snapshot.child(questionNumberr.toString()).child("b").value.toString()
//                    answerC = snapshot.child(questionNumberr.toString()).child("c").value.toString()
//                    answerD = snapshot.child(questionNumberr.toString()).child("d").value.toString()
//                    correctAnswer = snapshot.child(questionNumberr.toString()).child("answer").value.toString()

                    //ovo je uzimanje pitanja random kako su formirani u setu
                    question = snapshot.child(questions.elementAt(questionNumberr).toString()).child("q").value.toString()
                    answerA = snapshot.child(questions.elementAt(questionNumberr).toString()).child("a").value.toString()
                    answerB = snapshot.child(questions.elementAt(questionNumberr).toString()).child("b").value.toString()
                    answerC = snapshot.child(questions.elementAt(questionNumberr).toString()).child("c").value.toString()
                    answerD = snapshot.child(questions.elementAt(questionNumberr).toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questions.elementAt(questionNumberr).toString()).child("answer").value.toString()

                    binding.tvQuestion.text = question
                    binding.tvAnswer1.text = answerA
                    binding.tvAnswer2.text = answerB
                    binding.tvAnswer3.text = answerC
                    binding.tvAnswer4.text = answerD

                    binding.pb.visibility = View.INVISIBLE
                    binding.linearLayout.visibility = View.VISIBLE
                    binding.linearLayout2.visibility = View.VISIBLE
                    binding.linearLayout3.visibility = View.VISIBLE

                    startTimer()
                }
                else{
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulations!\nYou have answered all the questions. Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See results"){ dialog, which ->
                        saveScore()
                    }
                    dialogMessage.setNegativeButton("Play again?"){dialog, which ->
                        startActivity(Intent(this@QuizActivity, MainActivity::class.java))
                        finish()
                    }

                    dialogMessage.create().show()
                }

                questionNumberr++
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()

            }
        })
    }

    fun findCorrectAnswer(){
        when(correctAnswer){
            "a" -> binding.tvAnswer1.setBackgroundColor(Color.GREEN)
            "b" -> binding.tvAnswer2.setBackgroundColor(Color.GREEN)
            "c" -> binding.tvAnswer3.setBackgroundColor(Color.GREEN)
            "d" -> binding.tvAnswer4.setBackgroundColor(Color.GREEN)
        }
    }

    fun disableAnswers(){
        binding.tvAnswer1.isClickable = false
        binding.tvAnswer2.isClickable = false
        binding.tvAnswer3.isClickable = false
        binding.tvAnswer4.isClickable = false
    }

    fun resetOptions(){
        binding.tvAnswer1.setBackgroundColor(Color.WHITE)
        binding.tvAnswer2.setBackgroundColor(Color.WHITE)
        binding.tvAnswer3.setBackgroundColor(Color.WHITE)
        binding.tvAnswer4.setBackgroundColor(Color.WHITE)

        binding.tvAnswer1.isClickable = true
        binding.tvAnswer2.isClickable = true
        binding.tvAnswer3.isClickable = true
        binding.tvAnswer4.isClickable = true
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeft, timeInterval){
            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {

                resetTimer()
                updateCountDownText()
                binding.tvQuestion.text = "Sorry, time is up. Continue with the next question."
                disableAnswers()
                timerRunning = false
            }

        }.start()

        timerRunning = true
    }

    fun updateCountDownText() {
        val remainingTime : Int = (timeLeft / 1000).toInt()
        binding.tvTime.text = remainingTime.toString()
    }

    fun pauseTimer(){
        timer.cancel()
        timerRunning = false
    }

    fun resetTimer(){
        pauseTimer()
        timeLeft = totalTime
        updateCountDownText()
    }

    fun saveScore(){
        auth.currentUser?.let {
            val userId = it.uid

            dbScoreReference.child(userId).child("correct").setValue(userCorrectAnsers)
            dbScoreReference.child(userId).child("wrong").setValue(userWrongAnswers)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Your score has been saved!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@QuizActivity, ScoresActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }

    }
}