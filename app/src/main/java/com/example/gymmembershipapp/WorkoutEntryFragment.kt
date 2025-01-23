package com.example.gymmembershipapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val workoutWithUserId = workout.copy(userId = userId)
            val database = AppDatabase.getDatabase(requireContext())
            lifecycleScope.launch {
                try {
                    database.workoutDao().insert(workoutWithUserId)
                    Log.d("WorkoutEntryFragment", "Workout saved for user $userId")
                    onComplete()
                } catch (e: Exception) {
                    Log.e("WorkoutEntryFragment", "Error: ${e.message}", e)
                }
            }
        } else {
            Log.e("WorkoutEntryFragment", "User not authenticated")
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
            Text(
                text = "Enter Workout Details",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFFFB8C00)),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = exercise,
                onValueChange = { exercise = it },
                label = { Text("Exercise") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = repetitions,
                onValueChange = { repetitions = it },
                label = { Text("Repetitions") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = sets,
                onValueChange = { sets = it },
                label = { Text("Sets") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (!userId.isNullOrEmpty()) {
                        val workout = Workout(
                            exercise = exercise,
                            repetitions = repetitions.toIntOrNull() ?: 0,
                            weight = weight.toIntOrNull() ?: 0,
                            sets = sets.toIntOrNull() ?: 0,
                            userId = userId
                        )
                        onSaveWorkout(workout)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFB8C00))
            ) {
                Text("Save Workout", color = Color.White)
            }
        }
    }
}
