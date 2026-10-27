package com.example.mykitten

import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min

data class PetState(
    var hunger: Double = 50.0,
    var rest: Double = 70.0,
    var energy: Double = 30.0,
    var play: Double = 70.0,
    var happinessProgress: Double = 0.0, // 0 to 100% towards age up
    var accumulatedOptimalHours: Double = 0.0, // real hours of optimal care
    var age: Int = 0,
    var lastUpdateTimestamp: Long = System.currentTimeMillis(),
    var lastPlayTimestamp: Long = 0L,
    var isSleeping: Boolean = false,
    var isFastPace: Boolean = false
) {
    fun update(currentTime: Long, fastPace: Boolean) {
        val elapsedRealMillis = currentTime - lastUpdateTimestamp
        if (elapsedRealMillis <= 0) return

        val elapsedRealHours = elapsedRealMillis / 3600000.0
        val scale = if (fastPace) 5.0 else 1.0
        val elapsedInGameHours = elapsedRealHours * scale

        // 1. Hunger decay: 100 to 0 over 8 in-game hours -> 12.5 units/hr
        val hungerDecay = 12.5 * elapsedInGameHours
        hunger = max(0.0, hunger - hungerDecay)

        // 2. Rest decay or recovery
        if (isSleeping) {
            // Sleep recovery: 0 to 100 in 4 in-game hours -> 25 units/hr
            val restRecovery = 25.0 * elapsedInGameHours
            rest = min(100.0, rest + restRecovery)
            if (rest >= 100.0) {
                isSleeping = false
            }
        } else {
            // Rest decay: 100 to 0 over 12 in-game hours -> 8.333 units/hr
            val restDecay = (100.0 / 12.0) * elapsedInGameHours
            rest = max(0.0, rest - restDecay)
        }

        // 3. Play decay: 100 to 0 over 6 in-game hours -> 16.667 units/hr
        val playDecay = (100.0 / 6.0) * elapsedInGameHours
        play = max(0.0, play - playDecay)

        // 4. Closed-form Energy drift with lambda = 0.5 towards target
        val targetEnergy = 0.40 * hunger + 0.60 * rest
        val lambda = 0.5
        val driftedEnergy = targetEnergy + (energy - targetEnergy) * exp(-lambda * elapsedInGameHours)
        energy = min(100.0, max(0.0, driftedEnergy))

        // 5. Optimal care & Age progression (time-independent real hours)
        if (hunger >= 100.0 && rest >= 100.0 && energy >= 100.0 && play > 50.0) {
            accumulatedOptimalHours += elapsedRealHours
            if (accumulatedOptimalHours >= 24.0) {
                age += 1
                accumulatedOptimalHours -= 24.0
            }
        }
        happinessProgress = min(100.0, (accumulatedOptimalHours / 24.0) * 100.0)

        lastUpdateTimestamp = currentTime
        this.isFastPace = fastPace
    }

    fun applyAction(actionType: String, currentTime: Long): Boolean {
        when (actionType) {
            "FEED" -> {
                if (hunger > 75.0) return false // Too full
                hunger = min(100.0, hunger + 25.0)
                energy = min(100.0, energy + 5.0)
            }
            "PLAY" -> {
                if (currentTime - lastPlayTimestamp < 900000L) return false // 15 min real cooldown
                play = min(100.0, play + 30.0)
                energy = max(0.0, energy - 15.0)
                lastPlayTimestamp = currentTime
            }
            "SLEEP" -> {
                isSleeping = true
            }
            "WAKE" -> {
                isSleeping = false
            }
        }
        return true
    }
}
