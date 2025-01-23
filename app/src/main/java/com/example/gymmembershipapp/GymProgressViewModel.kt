package com.example.gymmembershipapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GymProgressViewModel : ViewModel() {
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts




    private lateinit var database: AppDatabase

    fun initializeDatabase(context: Context) {
        if (!::database.isInitialized) {
            database = AppDatabase.getDatabase(context)
            fetchWorkouts()
        } else {
            Log.d("GymProgressViewModel", "Database already initialized")
        }
    }
    fun debugClearWorkouts() {
        viewModelScope.launch {
            if (::database.isInitialized) {
                database.workoutDao().deleteAll()
                _workouts.value = emptyList()
            }
        }
    }

    fun loadUserWorkouts(userId: String) {
        viewModelScope.launch {
            database.workoutDao().getWorkoutsForUser(userId).collect { userWorkouts ->
                _workouts.value = userWorkouts
            }

        }
    }

    private fun fetchWorkouts() {
        viewModelScope.launch {
            database.workoutDao().getAllWorkouts().collect { workouts ->
                _workouts.value = workouts
            }
        }
    }



}
