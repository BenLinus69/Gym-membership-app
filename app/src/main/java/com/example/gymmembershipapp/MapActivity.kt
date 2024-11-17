package com.example.gymmembershipapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.google.android.gms.maps.*
import com.example.gymmembershipapp.ui.theme.GymMembershipAppTheme
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymMembershipAppTheme {
                MapScreen()
            }
        }
    }
}

@Composable
fun MapScreen() {
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

            // to add map
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                GoogleMapView()
            }
        }
    }
}
@Composable
fun GoogleMapView() {
    val context = LocalContext.current
    val mapView = MapView(context)

    // initialize mapView
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize(),
        update = { map ->
            map.onCreate(null)
            map.getMapAsync { googleMap ->
                setupGoogleMap(googleMap)
            }
            map.onResume()
        }
    )
}
fun setupGoogleMap(googleMap: GoogleMap) {
    // coordinates for Bucharest
    val bucharest = LatLng(44.4268, 26.1025)

    // Bucharest centre
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bucharest, 12f))

    googleMap.addMarker(
        MarkerOptions()
            .position(bucharest)
            .title("Bucharest")
    )
}
@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    GymMembershipAppTheme {
        MapScreen()
    }
}