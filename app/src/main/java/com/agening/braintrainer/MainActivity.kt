package com.agening.braintrainer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agening.braintrainer.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val options: ArrayList<TextView> = ArrayList()
    private var question: String? = null
    private var rightAnswer = 0
    private var rightAnswerPosition = 0
    private var isPositive = false
    private val min = 5
    private val max = 30
    private var countOfQuestions = 0
    private var countOfRightAnswers = 0
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        options.add(binding.textViewOpinion0)
        options.add(binding.textViewOpinion1)
        options.add(binding.textViewOpinion2)
        options.add(binding.textViewOpinion3)
        binding.textViewOpinion0.setOnClickListener(this)
        binding.textViewOpinion1.setOnClickListener(this)
        binding.textViewOpinion2.setOnClickListener(this)
        binding.textViewOpinion3.setOnClickListener(this)
        playNext()

        val timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textViewTimer.text = getTime(millisUntilFinished);
                if (millisUntilFinished < 10000) {
                    binding.textViewTimer.setTextColor(resources.getColor(android.R.color.holo_red_light))
                }
            }

            override fun onFinish() {
                gameOver = true
                val preferences = getSharedPreferences("preference", Context.MODE_PRIVATE)
                val max = preferences.getInt("max", 0)
                if (countOfRightAnswers >= max) {
                    preferences.edit().putInt("max", countOfRightAnswers).apply()
                }
                val intent = Intent(this@MainActivity, ScoreActivity::class.java)
                intent.putExtra("result", countOfRightAnswers)
                startActivity(intent)
            }
        }.start()

    }

    private fun getTime(millis: Long): String {
        var seconds = (millis / 1000).toInt()
        val minutes = seconds / 60
        seconds %= 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun playNext() {
        generateQuestion()
        for (i in options.indices) {
            if (i == rightAnswerPosition) {
                options[i].text = rightAnswer.toString()
            } else {
                options[i].text = generateWrongAnswer().toString()
            }
        }
        val score = String.format("%s / %s", countOfRightAnswers, countOfQuestions)
        binding.textViewScore.text = score

    }

    private fun generateWrongAnswer(): Int {
        var result: Int
        do {
            result = (Math.random() * max * 2 + 1).toInt() - (max - min)
        } while (result == rightAnswer)
        return result
    }

    private fun generateQuestion() {
        val a = (Math.random() * (max - min + 1) + min).toInt()
        val b = (Math.random() * (max - min + 1) + min).toInt()
        val mark = (Math.random() * 2).toInt()
        isPositive = mark == 1
        if (isPositive) {
            rightAnswer = a + b
            question = String.format("%s + %s", a, b)
        } else {
            rightAnswer = a - b
            question = String.format("%s - %s", a, b)
        }
        binding.textViewQuestion.text = question
        rightAnswerPosition = (Math.random() * 4).toInt()
    }

    override fun onClick(view: View?) {
        if (!gameOver) {
            val textView = view as TextView
            val chosenAnswer = textView.text.toString().toInt()
            if (chosenAnswer == rightAnswer) {
                countOfRightAnswers++
                Toast.makeText(this, "Верно", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Неверно", Toast.LENGTH_SHORT).show()
            }
            countOfQuestions++
            playNext()
        }
    }
}