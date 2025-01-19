package com.example.gymmembershipapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.ui.platform.ComposeView

class GymProgressFragment : Fragment() {
    private val gymProgressViewModel: GymProgressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gymProgressViewModel.initializeDatabase(requireContext())  // init baza de date
        return ComposeView(requireContext()).apply {
            setContent {
                GymProgressScreen(
                    navigateToWorkoutEntry = {
                        navigateToFragment(WorkoutEntryFragment())
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
    workouts: List<Workout>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Gym Progress",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // afisez workouturile
                workouts.take(3).forEach { workout ->
                    ProgressStat(label = "Exercise: ${workout.exercise}", value = "Reps: ${workout.repetitions}, Sets: ${workout.sets}, Weight: ${workout.weight}kg")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = navigateToWorkoutEntry) {
                    Text("Add Workout")
                }
            }
        }
    }
}

@Composable
fun ProgressStat(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
