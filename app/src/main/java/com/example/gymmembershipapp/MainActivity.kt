package com.example.gymmembershipapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.gymmembershipapp.ui.theme.GymMembershipAppTheme
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val icon: ImageVector
)

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

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContent {
            GymMembershipAppTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val context = LocalContext.current

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = true,
                    drawerContent = {
                        DrawerContent(
                            items = listOf(
                                NavigationItem("About", Icons.Filled.Info),  // Use Filled icons
                                NavigationItem("Contact", Icons.Filled.Email)
                            ),
                            onItemClick = { item ->
                                when (item.title) {
                                    "About" -> {/* Handle about */ }
                                    "Contact" -> {/* Handle contact */ }
                                }
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                ) {
                    HomeScreen(
                        onNavigateToMap = {
                            startActivity(Intent(context, MapActivity::class.java))
                        },
                        onNavigateToProgress = {
                            navigateToFragment(GymProgressFragment())
                        },
                        onLogout = {
                            auth.signOut()
                            redirectToLogin()
                        },
                        openDrawer = { scope.launch { drawerState.open() } }
                    )
                }
            }
        }
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }
}

@Composable
fun HomeScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onLogout: () -> Unit,
    openDrawer: () -> Unit
) {
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
        Box(modifier = Modifier.fillMaxSize()) {

            // Main content
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color(0xFF1F3A61))
            ) {
                Spacer(modifier = Modifier.height(64.dp))

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

                    Button(
                        onClick = onNavigateToMap,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(0.dp),
                        shape = MaterialTheme.shapes.medium,
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 12.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
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
                        Box(modifier = Modifier.fillMaxSize()) {
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

            IconButton(
                onClick = openDrawer,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open menu",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun DrawerContent(
    items: List<NavigationItem>,
    onItemClick: (NavigationItem) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.66f

    Column(
        modifier = Modifier
            .width(drawerWidth)
            .fillMaxHeight()
            .background(Color.White)
    ) {
        DrawerHeader()

        Spacer(modifier = Modifier.height(16.dp))

        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                selected = false,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = Color(0xFF1F3A61)
                    )
                },
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = Color(0xFF1F3A61),
                    unselectedIconColor = Color(0xFF1F3A61)
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFF1F3A61)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.dumbbell),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "Gym Tracker",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GymMembershipAppTheme {
        HomeScreen(
            onNavigateToMap = {},
            onNavigateToProgress = {},
            onLogout = {},
            openDrawer = {}
        )
    }
}