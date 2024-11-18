package com.example.gymmembershipapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment

class GymProgressFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GymProgressScreen()
            }
        }
    }
}

@Composable
fun GymProgressScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SpinningGearAnimation(modifier = Modifier.fillMaxSize())

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

                ProgressStat(label = "Workouts Completed", value = "12")
                ProgressStat(label = "Entries in the last month:", value = "4")
                ProgressStat(label = "Current Weight", value = "80")
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

@Composable
fun SpinningGearAnimation(modifier: Modifier = Modifier) {
    val rotation = remember { Animatable(0f) }

    // animation
    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter // place at the bottom of the screen
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val canvasSize = size
            val gearSize = canvasSize.minDimension

            rotate(rotation.value) {
                // drawing the shape
                drawRoundRect(
                    color = Color.Blue,
                    topLeft = center.copy(x = center.x - gearSize / 4, y = center.y - gearSize / 4),
                    size = Size(gearSize / 2, gearSize / 2),
                    cornerRadius = CornerRadius(gearSize / 8, gearSize / 8)
                )
            }
        }
    }
}

