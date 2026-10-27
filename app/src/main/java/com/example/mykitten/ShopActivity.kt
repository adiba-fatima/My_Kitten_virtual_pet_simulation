package com.example.mykitten

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val ownerName = intent.getStringExtra("OWNER_NAME") ?: "Owner"

        val cat1 = findViewById<Button>(R.id.selectCat1)
        val cat2 = findViewById<Button>(R.id.selectCat2)
        val cat3 = findViewById<Button>(R.id.selectCat3)

        val selectAction = { catType: String ->
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("OWNER_NAME", ownerName)
                putExtra("CAT_TYPE", catType)
            }
            startActivity(intent)
            finish()
        }

        cat1.setOnClickListener { selectAction("Ginger Tabby") }
        cat2.setOnClickListener { selectAction("Tuxedo Cat") }
        cat3.setOnClickListener { selectAction("Calico Cat") }
    }
}
