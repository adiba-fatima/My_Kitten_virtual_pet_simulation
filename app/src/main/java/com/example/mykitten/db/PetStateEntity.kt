package com.example.mykitten.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_state")
data class PetStateEntity(
    @PrimaryKey val id: Int = 1,
    val petName: String = "Kitten",
    val catType: String = "Kitten",
    val hunger: Int = 50,
    val happiness: Int = 70,
    val energy: Int = 30,
    val level: Int = 1,
    val exp: Int = 0,
    val coins: Int = 100,
    val fishCount: Int = 2,
    val treatsCount: Int = 2,
    val kibbleCount: Int = 1,
    val milkCount: Int = 2,
    val boxOwned: Int = 1,
    val curtainOwned: Int = 1,
    val cushionOwned: Int = 1,
    val plantOwned: Int = 1,
    val currentRoomIndex: Int = 0,
    val isCatHidden: Boolean = true,
    val hiddenRoomIndex: Int = 0,
    val hiddenSpotIndex: Int = 0,
    val searchTries: Int = 1,
    val isHintUsed: Boolean = false,
    val isFastPace: Boolean = false,
    val isSleeping: Boolean = false,
    val sleepDurationCounter: Int = 0
)
