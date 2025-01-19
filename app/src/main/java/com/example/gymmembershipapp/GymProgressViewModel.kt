package com.example.gymmembershipapp

import android.content.Context
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
        }
    }

    private fun fetchWorkouts() {
        viewModelScope.launch {
            _workouts.value = database.workoutDao().getAllWorkouts()
        }
    }
}
