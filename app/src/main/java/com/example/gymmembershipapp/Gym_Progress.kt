package com.example.gymmembershipapp

import NotificationHelper
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.auth.FirebaseAuth


class GymProgressFragment : Fragment() {
    private val gymProgressViewModel: GymProgressViewModel by viewModels()
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationHelper = NotificationHelper(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("GymProgressPrefs", Context.MODE_PRIVATE)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            gymProgressViewModel.initializeDatabase(requireContext())

        } else {
            navigateToLogin()
        }
        //gymProgressViewModel.debugClearWorkouts() //for debugging
        return ComposeView(requireContext()).apply {
            setContent {
                LaunchedEffect(userId) {
                    if (userId != null) {
                        gymProgressViewModel.loadUserWorkouts(userId)
                    }
                }
                val workouts by gymProgressViewModel.workouts.collectAsState()

                val userWorkouts = workouts.filter { it.userId == userId }


                checkMilestones(userWorkouts.size,userId)

                GymProgressScreen(
                    navigateToWorkoutEntry = {
                        navigateToFragment(WorkoutEntryFragment())
                    },
                    navigateToHome = {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    },
                    workouts = userWorkouts

                )
            }
        }
    }

    private fun checkMilestones(workoutsCount: Int, userId: String?) {
        val milestones = listOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 50)

        if (userId != null && workoutsCount in milestones) {
            val milestoneKey = "milestone_${userId}_$workoutsCount"
            val milestoneTriggered = sharedPreferences.getBoolean(milestoneKey, false)

            if (!milestoneTriggered) {
                notificationHelper.showMilestoneNotification(workoutsCount)
                sharedPreferences.edit().putBoolean(milestoneKey, true).apply()
            }
        }
    }


    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
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

            // workout number
            Text(
                text = "Workouts: ${workouts.size}",
                style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF1F3A61)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(workouts) { workout ->
                    WorkoutCard(workout = workout)

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = navigateToWorkoutEntry, modifier = Modifier.padding(16.dp)) {
                Text("Add Workout")
            }

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
            .background(Color(0xFFE1F5FE))
            ,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
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

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Weight: ${workout.weight}kg",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF388E3C)),
                )
            }

            Image(
                painter = painterResource(id = R.drawable.dumbbell),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}

