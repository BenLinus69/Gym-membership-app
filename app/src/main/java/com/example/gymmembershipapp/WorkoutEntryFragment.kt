package com.example.gymmembershipapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class WorkoutEntryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WorkoutEntryScreen(
                    onSaveWorkout = { workout ->
                        saveWorkoutToDatabase(workout) {
                            navigateToGymProgress()
                        }
                    }
                )
            }
        }
    }

    private fun saveWorkoutToDatabase(workout: Workout, onComplete: () -> Unit) {
        val database = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch {
            try {


                database.workoutDao().insert(workout)
                Log.d("WorkoutEntryFragment", "Workout salvat")
                onComplete()
            } catch (e: Exception) {
                Log.e("WorkoutEntryFragment", "Eroare: ${e.message}", e)
            }
        }
    }


    private fun navigateToGymProgress() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, GymProgressFragment())
            .addToBackStack(null)
            .commit()
    }
}

@Composable
fun WorkoutEntryScreen(onSaveWorkout: (Workout) -> Unit) {
    var exercise by remember { mutableStateOf("") }
    var repetitions by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = exercise,
                onValueChange = { exercise = it },
                label = { Text("Exercise") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = repetitions,
                onValueChange = { repetitions = it },
                label = { Text("Repetitions") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = sets,
                onValueChange = { sets = it },
                label = { Text("Sets") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val workout = Workout(
                    exercise = exercise,
                    repetitions = repetitions.toIntOrNull() ?: 0,
                    weight = weight.toIntOrNull() ?: 0,
                    sets = sets.toIntOrNull() ?: 0
                )
                onSaveWorkout(workout)
            }) {
                Text("Save Workout")
            }
        }
    }
}
