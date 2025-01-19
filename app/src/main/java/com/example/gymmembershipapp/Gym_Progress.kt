package com.example.gymmembershipapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign

class GymProgressFragment : Fragment() {
    private val gymProgressViewModel: GymProgressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gymProgressViewModel.initializeDatabase(requireContext())  // init db
        return ComposeView(requireContext()).apply {
            setContent {
                GymProgressScreen(
                    navigateToWorkoutEntry = {
                        navigateToFragment(WorkoutEntryFragment())
                    },
                    navigateToHome = {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    },
                    workouts = gymProgressViewModel.workouts.collectAsState().value
                )
            }
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }
}

@Composable
fun GymProgressScreen(
    navigateToWorkoutEntry: () -> Unit,
    navigateToHome: () -> Unit,
    workouts: List<Workout>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Text(
                text = "Gym Progress",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFFFB8C00)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                workouts.take(3).forEach { workout ->
                    WorkoutCard(workout = workout)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = navigateToWorkoutEntry) {
                    Text("Add Workout")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Home button
            Button(
                onClick = navigateToHome,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Home", color = Color.White)
            }
        }
    }
}

@Composable
fun WorkoutCard(workout: Workout) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFE1F5FE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Exercise: ${workout.exercise}",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF000000)),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Reps: ${workout.repetitions}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF388E3C)),
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sets: ${workout.sets}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF388E3C)),
            )

            // Display weight on the next line
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Weight: ${workout.weight}kg",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF388E3C)),
            )
        }
    }
}

