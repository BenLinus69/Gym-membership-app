package com.example.gymmembershipapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.gymmembershipapp.ui.theme.GymMembershipAppTheme
import androidx.compose.ui.layout.ContentScale
import com.google.firebase.auth.FirebaseAuth

class MainActivity : FragmentActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        //check if user is authenticated
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContent {
            GymMembershipAppTheme {
                HomeScreen(
                    onNavigateToMap = {
                        startActivity(Intent(this, MapActivity::class.java))
                    },
                    onNavigateToProgress = {
                        navigateToFragment(GymProgressFragment())
                    },
                    onLogout = {
                        auth.signOut()
                        redirectToLogin()
                    }
                )
            }
        }
    }
    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
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
fun HomeScreen(onNavigateToMap: () -> Unit, onNavigateToProgress: () -> Unit, onLogout: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF1F3A61),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onLogout,
                modifier = Modifier.padding(16.dp),
                containerColor = Color.Red,
                contentColor = Color.White
            ) {
                Text("Logout")
            }
        }
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
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White
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
                        fontSize = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // button for map
                Button(
                    onClick = onNavigateToMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(0.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.map),
                            contentDescription = "See Gyms on the Map",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNavigateToProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(0.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Image as button background
                        Image(
                            painter = painterResource(id = R.drawable.progress),
                            contentDescription = "View Gym Progress",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GymMembershipAppTheme {
        HomeScreen(onNavigateToMap = {}, onNavigateToProgress = {}, onLogout = {})
    }
}
