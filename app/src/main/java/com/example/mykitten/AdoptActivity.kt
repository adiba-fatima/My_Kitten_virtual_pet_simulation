package com.example.mykitten

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdoptActivity : AppCompatActivity() {

    private var coins = 100
    private lateinit var txtAdoptCoins: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopt)

        val petName = intent.getStringExtra("PET_NAME") ?: "Kitten"

        txtAdoptCoins = findViewById(R.id.txtAdoptCoins)
        val btnAdoptBack = findViewById<ImageButton>(R.id.btnAdoptBack)
        val btnGoToFoodShop = findViewById<Button>(R.id.btnGoToFoodShop)

        val btnAdopt1 = findViewById<Button>(R.id.btnAdopt1)
        val btnAdopt2 = findViewById<Button>(R.id.btnAdopt2)
        val btnAdopt3 = findViewById<Button>(R.id.btnAdopt3)

        loadCoins()

        btnAdoptBack.setOnClickListener {
            finish()
        }

        btnGoToFoodShop.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java).apply {
                putExtra("PET_NAME", petName)
            }
            startActivity(intent)
            finish()
        }

        btnAdopt1.setOnClickListener { adoptCat(petName, "Ginger Tabby", cost = 0) }
        btnAdopt2.setOnClickListener { adoptCat(petName, "Tuxedo Cat", cost = 50) }
        btnAdopt3.setOnClickListener { adoptCat(petName, "Calico Cat", cost = 50) }
    }

    private fun adoptCat(petName: String, catType: String, cost: Int) {
        if (coins >= cost) {
            coins -= cost
            saveCoins()

            Toast.makeText(this, "🎉 Adopted $catType as your companion!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("PET_NAME", petName)
                putExtra("CAT_TYPE", catType)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Not enough coins! You need $cost coins to adopt $catType.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCoins() {
        val sharedPrefs = getSharedPreferences("KittenGamePrefs", Context.MODE_PRIVATE)
        coins = sharedPrefs.getInt("coins", 100)
        txtAdoptCoins.text = "💰 $coins"
    }

    private fun saveCoins() {
        val sharedPrefs = getSharedPreferences("KittenGamePrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt("coins", coins)
            apply()
        }
    }
}
