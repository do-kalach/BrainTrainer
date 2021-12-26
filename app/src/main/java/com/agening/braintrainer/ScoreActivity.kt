package com.agening.braintrainer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agening.braintrainer.databinding.ActivityScoreBinding


class ScoreActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityScoreBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val intent = intent
        if (intent != null && intent.hasExtra("result")) {
            val result = intent.getIntExtra("result", 0)
            val preferences = getSharedPreferences("preference", Context.MODE_PRIVATE)
            val max = preferences.getInt("max", 0)
            val score = String.format("Ваш результат: %s\nМаксимальный результат: %s", result, max)
            binding.textViewResult.text = score
        }
        binding.buttonStartNewGame.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}