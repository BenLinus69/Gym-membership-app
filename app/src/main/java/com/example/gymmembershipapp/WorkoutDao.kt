package com.example.gymmembershipapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workout WHERE userId = :userId ORDER BY id DESC")
    fun getWorkoutsForUser(userId: String): Flow<List<Workout>>

    @Query("SELECT * FROM workout ORDER BY id DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("DELETE FROM workout")
    suspend fun deleteAll()
}
