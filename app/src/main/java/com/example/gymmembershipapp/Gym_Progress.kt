package com.example.gymmembershipapp

import android.content.Intent
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
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display workouts
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                workouts.take(3).forEach { workout ->
                    ProgressStat(
                        label = "Exercise: ${workout.exercise}",
                        value = "Reps: ${workout.repetitions}, Sets: ${workout.sets}, Weight: ${workout.weight}kg"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = navigateToWorkoutEntry) {
                    Text("Add Workout")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = navigateToHome,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Home")
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
