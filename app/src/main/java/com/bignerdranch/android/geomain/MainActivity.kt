package com.bignerdranch.android.geomain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.math.roundToLong

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, true),
        Question(R.string.question_africa, true),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0
    private var trueAnswer = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        currentIndex=savedInstanceState?.getInt("cur_key")?:0
        trueAnswer=savedInstanceState?.getDouble("result")?:0.0

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            falseButton.isEnabled = false
            trueButton.isEnabled = false
            checkCurInd()
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            falseButton.isEnabled = false
            trueButton.isEnabled = false
            checkCurInd()
        }
        nextButton.setOnClickListener {
            falseButton.isEnabled = true
            trueButton.isEnabled = true
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            nextButton.visibility = View.INVISIBLE
            prevButton.visibility = View.INVISIBLE
        }
        prevButton.setOnClickListener {
            falseButton.isEnabled = true
            trueButton.isEnabled = true
            currentIndex = (currentIndex - 1) % questionBank.size
            updateQuestion()
            prevButton.visibility = View.INVISIBLE
            nextButton.visibility = View.INVISIBLE
        }
        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        updateQuestion()
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.run {
            outState.putInt("cur_key", currentIndex)
            outState.putDouble("result", trueAnswer)
        }
        updateQuestion()
    }
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }
    private fun checkCurInd(){
        if (currentIndex == questionBank.size - 1){
            prevButton.visibility = View.VISIBLE
            val percentTrue = ((trueAnswer / questionBank.size) * 100.0).roundToLong()
            val result = "$percentTrue%"
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
        else if (currentIndex == 0){
            nextButton.visibility = View.VISIBLE
        }
        else{
            nextButton.visibility = View.VISIBLE
            prevButton.visibility = View.VISIBLE
        }
    }
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else{
            R.string.incorrect_toast
        }
        if (userAnswer == correctAnswer){
            trueAnswer +=  1
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).apply {setGravity(Gravity.TOP, 0, 0); show() }
    }
}