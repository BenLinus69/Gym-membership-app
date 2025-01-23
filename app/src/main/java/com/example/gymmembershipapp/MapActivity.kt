package com.example.gymmembershipapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.example.gymmembershipapp.ui.theme.GymMembershipAppTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MapActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Request location permissions
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setContent {
                GymMembershipAppTheme {
                    MapScreen(fusedLocationClient)
                }
            }
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // check location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            setContent {
                GymMembershipAppTheme {
                    MapScreen(fusedLocationClient)
                }
            }
        }
    }
}

@Composable
fun MapScreen(fusedLocationClient: FusedLocationProviderClient) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Available Gyms",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                GoogleMapView(fusedLocationClient)
            }
        }
    }
}

@Composable
fun GoogleMapView(fusedLocationClient: FusedLocationProviderClient) {
    val context = LocalContext.current
    val mapView = MapView(context)

    val bucharestBounds = Pair(
        LatLng(44.3653, 25.9699), // Southwest corner
        LatLng(44.5448, 26.2341)  // Northeast corner
    )

    // gen 10 random locations
    val gymLocations = generateRandomLocations(bucharestBounds, 10)

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize(),
        update = { map ->
            map.onCreate(null)
            map.getMapAsync { googleMap ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    googleMap.isMyLocationEnabled = true

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            val currentLatLng = LatLng(location.latitude, location.longitude)
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                        }
                    }
                    //add markers
                    gymLocations.forEachIndexed { index, latLng ->
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Gym #${index + 1}")
                        )
                    }
                }
            }
            map.onResume()
        }
    )
}
fun generateRandomLocations(bounds: Pair<LatLng, LatLng>, count: Int): List<LatLng> {
    val (southwest, northeast) = bounds
    val randomLocations = mutableListOf<LatLng>()

    repeat(count) {
        val lat = southwest.latitude + Math.random() * (northeast.latitude - southwest.latitude)
        val lng = southwest.longitude + Math.random() * (northeast.longitude - southwest.longitude)
        randomLocations.add(LatLng(lat, lng))
    }

    return randomLocations
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    GymMembershipAppTheme {
        MapScreen(LocationServices.getFusedLocationProviderClient(LocalContext.current))
    }
}