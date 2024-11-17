package com.example.gymmembershipapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.gymmembershipapp.ui.theme.GymMembershipAppTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymMembershipAppTheme {
                HomeScreen(
                    onNavigateToMap = {
                        startActivity(Intent(this, MapActivity::class.java))
                    },
                    onNavigateToProgress = {
                        navigateToFragment(GymProgressFragment())
                    }
                )
            }
        }
    }

    // Get the fragment
    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null) // can go back to home screen
            .commit()
    }
}

@Composable
fun HomeScreen(onNavigateToMap: () -> Unit, onNavigateToProgress: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF1F3A61),
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFF1F3A61)) // background color
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Gym Membership App",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                    )
                )
            }

            // Main
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to your gym journey!",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontSize = 20.dp.value.sp
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Button to see gyms on the map
                Button(
                    onClick = onNavigateToMap,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76C7C0))
                ) {
                    Text(
                        text = "See Gyms on the Map",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to view gym progress
                Button(
                    onClick = onNavigateToProgress,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFB8C00))
                ) {
                    Text(
                        text = "View Gym Progress",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GymMembershipAppTheme {
        HomeScreen(onNavigateToMap = {}, onNavigateToProgress = {})
    }
}
