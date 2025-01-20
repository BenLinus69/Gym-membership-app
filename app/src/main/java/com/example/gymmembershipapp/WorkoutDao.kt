package com.example.gymmembershipapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workout ORDER BY id DESC")
    suspend fun getAllWorkouts(): List<Workout>

    @Query("DELETE FROM workout")
    suspend fun deleteAll()
}
