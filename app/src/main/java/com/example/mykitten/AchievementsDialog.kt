package com.example.mykitten

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class AchievementsDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_achievements)

        val prefs = context.getSharedPreferences("MyKittenPrefs", Context.MODE_PRIVATE)
        val feeds = prefs.getInt("ach_feeds", 0)
        val sleeps = prefs.getInt("ach_sleeps", 0)
        val plays = prefs.getInt("ach_plays", 0)
        val botQ = prefs.getInt("ach_bot_questions", 0)
        val coins = prefs.getInt("coins", 100)
        val age = prefs.getInt("pet_age", 0)

        val container = findViewById<LinearLayout>(R.id.achievementsContainer)

        val achievements = listOf(
            Triple("🐟 Master Feeder", "Feed your cat 5 times (Progress: $feeds/5)", feeds >= 5),
            Triple("💤 Sleepyhead", "Put your cat to sleep 3 times (Progress: $sleeps/3)", sleeps >= 3),
            Triple("🎾 Playful Pal", "Play with your cat 3 times (Progress: $plays/3)", plays >= 3),
            Triple("🩺 Cat Whisperer", "Ask Dr. Kitty 3 questions (Progress: $botQ/3)", botQ >= 3),
            Triple("💰 Coin Tycoon", "Accumulate 150+ coins (Coins: $coins)", coins >= 150),
            Triple("🎂 Senior Companion", "Reach Age 1 or higher (Current Age: $age)", age >= 1)
        )

        for ((title, desc, unlocked) in achievements) {
            val card = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
                setBackgroundResource(R.drawable.item_card_bg)
                setPadding(24, 16, 24, 16)
                textSize = 14f
                setTextColor(android.graphics.Color.parseColor("#333333"))
                text = if (unlocked) "✅ $title\n$desc" else "🔒 $title (Locked)\n$desc"
            }
            container.addView(card)
        }
    }
}
