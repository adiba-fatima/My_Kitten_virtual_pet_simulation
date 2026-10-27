package com.example.mykitten

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val ownerName = nameInput.text.toString().trim()

            if (ownerName.isEmpty()) {
                Toast.makeText(this, "Please enter a name first!", Toast.LENGTH_SHORT).show()
            } else {
                // For Milestone 1, we jump to MainActivity. 
                // We will redirect to ShopActivity during Milestone 2!
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("OWNER_NAME", ownerName)
                }
                startActivity(intent)
                finish() // Close login screen so user can't click back to it
            }
        }
    }
}
