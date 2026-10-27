package com.example.mykitten

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.Toast

class AntiDoomscrollSettingsDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_anti_doomscroll_settings)

        val prefs = context.getSharedPreferences("MyKittenPrefs", Context.MODE_PRIVATE)
        val switchShield = findViewById<Switch>(R.id.switchShield)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupLimits)
        val btnSave = findViewById<Button>(R.id.btnSaveShield)

        val isEnabled = prefs.getBoolean("anti_doomscroll_enabled", false)
        val limitMinutes = prefs.getInt("anti_doomscroll_limit_mins", 30)

        switchShield.isChecked = isEnabled

        when (limitMinutes) {
            5 -> findViewById<RadioButton>(R.id.rb5m).isChecked = true
            10 -> findViewById<RadioButton>(R.id.rb10m).isChecked = true
            15 -> findViewById<RadioButton>(R.id.rb15m).isChecked = true
            30 -> findViewById<RadioButton>(R.id.rb30m).isChecked = true
            60 -> findViewById<RadioButton>(R.id.rb60m).isChecked = true
            else -> findViewById<RadioButton>(R.id.rb30m).isChecked = true
        }

        btnSave.setOnClickListener {
            val enabled = switchShield.isChecked
            val minutes = when (radioGroup.checkedRadioButtonId) {
                R.id.rb5m -> 5
                R.id.rb10m -> 10
                R.id.rb15m -> 15
                R.id.rb30m -> 30
                R.id.rb60m -> 60
                else -> 30
            }

            prefs.edit().apply {
                putBoolean("anti_doomscroll_enabled", enabled)
                putInt("anti_doomscroll_limit_mins", minutes)
                apply()
            }

            Toast.makeText(context, "Anti-Doomscrolling Shield saved! ($minutes mins limit)", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}
