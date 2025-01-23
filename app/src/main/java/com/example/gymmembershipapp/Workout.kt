package com.example.gymmembershipapp

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exercise: String,
    val repetitions: Int,
    val weight: Int,
    val sets: Int,
    val userId: String
)
