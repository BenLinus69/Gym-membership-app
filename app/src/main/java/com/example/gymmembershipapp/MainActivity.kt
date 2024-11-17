package com.example.gymmembershipapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    //get the fragment
    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null) // can go back to main screen
            .commit()
    }

}



@Composable
fun HomeScreen(onNavigateToMap: () -> Unit, onNavigateToProgress: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Gym Membership App",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // main
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to my gym app",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateToMap) {//button to see gyms
                    Text("See gyms on the map")
                }
                Spacer(modifier = Modifier.height(16.dp))//button to see progress
                Button(onClick = onNavigateToProgress) {
                    Text("View Gym Progress")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GymMembershipAppTheme {
        HomeScreen(onNavigateToMap = {},onNavigateToProgress = {})
    }
}
