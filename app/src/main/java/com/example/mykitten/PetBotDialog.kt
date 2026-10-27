package com.example.mykitten

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class PetBotDialog(context: Context) : Dialog(context) {

    private lateinit var chatContainer: LinearLayout
    private lateinit var chatScroll: ScrollView
    private lateinit var edtUserMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var btnQ1: Button
    private lateinit var btnQ2: Button
    private lateinit var btnQ3: Button
    private lateinit var btnQ4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pet_bot)

        chatContainer = findViewById(R.id.chatContainer)
        chatScroll = findViewById(R.id.chatScroll)
        edtUserMessage = findViewById(R.id.edtUserMessage)
        btnSend = findViewById(R.id.btnSend)
        btnQ1 = findViewById(R.id.btnQ1)
        btnQ2 = findViewById(R.id.btnQ2)
        btnQ3 = findViewById(R.id.btnQ3)
        btnQ4 = findViewById(R.id.btnQ4)

        btnQ1.setOnClickListener { handleUserQuery("What should I feed my cat?") }
        btnQ2.setOnClickListener { handleUserQuery("Why do cats sleep so much?") }
        btnQ3.setOnClickListener { handleUserQuery("Why do cats purr?") }
        btnQ4.setOnClickListener { handleUserQuery("Why do cats knead?") }

        btnSend.setOnClickListener {
            val query = edtUserMessage.text.toString().trim()
            if (query.isNotEmpty()) {
                handleUserQuery(query)
                edtUserMessage.setText("")
            }
        }
    }

    private fun handleUserQuery(question: String) {
        addUserBubble(question)
        val answer = getBotAnswer(question)
        addBotBubble(answer)
    }

    private fun addUserBubble(text: String) {
        val bubble = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(50, 8, 8, 8)
            }
            setBackgroundResource(R.drawable.custom_button_bg)
            setTextColor(android.graphics.Color.WHITE)
            setPadding(24, 16, 24, 16)
            textSize = 14f
            this.text = text
        }
        chatContainer.addView(bubble)
        scrollToBottom()
    }

    private fun addBotBubble(text: String) {
        val bubble = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 8, 50, 8)
            }
            setBackgroundResource(R.drawable.item_card_bg)
            setTextColor(android.graphics.Color.parseColor("#333333"))
            setPadding(24, 16, 24, 16)
            textSize = 14f
            this.text = text
        }
        chatContainer.addView(bubble)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        chatScroll.post {
            chatScroll.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun getBotAnswer(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("feed") || q.contains("food") || q.contains("eat") || q.contains("diet") ->
                "🐟 Cats are obligate carnivores! They need high-protein meat diets. Always provide fresh water and avoid chocolate, onions, garlic, and raw milk (many adult cats are lactose intolerant)."
            q.contains("sleep") || q.contains("tired") || q.contains("rest") ->
                "💤 Adult cats typically sleep 12 to 16 hours a day! It's completely natural for them to conserve energy for hunting and playing."
            q.contains("purr") || q.contains("sound") ->
                "❤️ Purring usually means your cat is happy, relaxed, or content. However, cats also purr when they are stressed, scared, or healing from minor injuries."
            q.contains("knead") || q.contains("biscuits") ->
                "🐾 Kneading (making biscuits) is an instinctive kitten behavior retained into adulthood. It shows contentment, comfort, and marks territory using scent glands in their paws."
            q.contains("water") || q.contains("drink") ->
                "🥛 Cats have a low thirst drive because their ancestors got water from prey. Providing a flowing pet water fountain or wet food helps keep their kidneys healthy!"
            q.contains("play") || q.contains("toy") || q.contains("bored") ->
                "🧶 Cats love interactive play! Use feather wands, laser pointers, or yarn balls for 15-20 minutes daily to keep their minds sharp and prevent destructive behavior."
            q.contains("hello") || q.contains("hi") || q.contains("hey") ->
                "Meow! How can I help you and your kitty today? 🐾"
            else ->
                "🐾 That's a great question about cat care! As Dr. Kitty, I recommend regular vet check-ups, fresh food and water, plenty of playtime, and lots of love!"
        }
    }
}
