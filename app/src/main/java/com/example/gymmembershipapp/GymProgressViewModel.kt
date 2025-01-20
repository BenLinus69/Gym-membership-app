package com.example.gymmembershipapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

class GymProgressViewModel : ViewModel() {
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    private val _milestoneEvents = MutableSharedFlow<Int>()
    val milestoneEvents: SharedFlow<Int> = _milestoneEvents.asSharedFlow()

    private lateinit var database: AppDatabase

    fun initializeDatabase(context: Context) {
        if (!::database.isInitialized) {
            database = AppDatabase.getDatabase(context)
            fetchWorkouts()
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

    private fun fetchWorkouts() {
        viewModelScope.launch {
            _workouts.value = database.workoutDao().getAllWorkouts()
        }
    }


}
