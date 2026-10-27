package com.example.mykitten

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Pet stats variables
    private var hunger = 50
    private var happiness = 70
    private var energy = 30

    // UI elements
    private lateinit var hungerBar: ProgressBar
    private lateinit var happinessBar: ProgressBar
    private lateinit var energyBar: ProgressBar
    private lateinit var petImage: ImageView

    // Game loop
    private val handler = Handler(Looper.getMainLooper())
    private val gameRunnable = object : Runnable {
        override fun run() {
            gameTick()
            handler.postDelayed(this, 3000) // Tick every 3 seconds
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link UI elements
        hungerBar = findViewById(R.id.hungerBar)
        happinessBar = findViewById(R.id.happinessBar)
        energyBar = findViewById(R.id.energyBar)
        petImage = findViewById(R.id.petImage)

        val feedButton = findViewById<Button>(R.id.feedButton)
        val playButton = findViewById<Button>(R.id.playButton)
        val sleepButton = findViewById<Button>(R.id.sleepButton)

        // Button logic
        feedButton.setOnClickListener {
            if (hunger >= 100) {
                Toast.makeText(this, "Overfed!", Toast.LENGTH_SHORT).show()
            } else {
                hunger = (hunger + 10).coerceAtMost(100)
                updateUI()
            }
        }

        playButton.setOnClickListener {
            if (energy <= 5) {
                Toast.makeText(this, "Too tired to play!", Toast.LENGTH_SHORT).show()
            } else if (happiness >= 100) {
                Toast.makeText(this, "Too excited!", Toast.LENGTH_SHORT).show()
            } else {
                happiness = (happiness + 10).coerceAtMost(100)
                energy = (energy - 10).coerceAtLeast(0)
                updateUI()
            }
        }

        sleepButton.setOnClickListener {
            if (energy >= 100) {
                Toast.makeText(this, "Already fully rested!", Toast.LENGTH_SHORT).show()
            } else {
                energy = (energy + 15).coerceAtMost(100)
                hunger = (hunger - 5).coerceAtLeast(0)
                updateUI()
            }
        }

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        handler.post(gameRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(gameRunnable)
    }

    private fun gameTick() {
        // Stats decay over time
        hunger = (hunger - 2).coerceAtLeast(0)
        happiness = (happiness - 1).coerceAtLeast(0)
        energy = (energy - 1).coerceAtLeast(0)

        if (hunger == 0) {
            Toast.makeText(this, "Kitten is starving!", Toast.LENGTH_SHORT).show()
        }

        updateUI()
    }

    private fun updateUI() {
        hungerBar.progress = hunger
        happinessBar.progress = happiness
        energyBar.progress = energy

        // Change colors based on levels
        setBarColor(hungerBar, hunger)
        setBarColor(happinessBar, happiness)
        setBarColor(energyBar, energy)

        // Update pet image based on mood
        updateMood()
    }

    private fun updateMood() {
        when {
            energy <= 20 -> petImage.setImageResource(R.drawable.pet_sleepy)
            hunger <= 20 || happiness <= 20 -> petImage.setImageResource(R.drawable.pet_sad)
            else -> petImage.setImageResource(R.drawable.pet_happy)
        }
    }

    private fun setBarColor(bar: ProgressBar, value: Int) {
        when {
            value <= 20 -> bar.progressDrawable.setTint(Color.RED)
            value <= 50 -> bar.progressDrawable.setTint(Color.YELLOW)
            else -> bar.progressDrawable.setTint(Color.GREEN)
        }
    }
}
