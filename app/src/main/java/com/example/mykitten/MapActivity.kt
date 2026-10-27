package com.example.mykitten

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val btnMapBack = findViewById<ImageButton>(R.id.btnMapBack)
        btnMapBack.setOnClickListener { finish() }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableUserLocation()
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 13f))
                    addNearbySheltersAndVets(userLatLng)
                } else {
                    val defaultLocation = LatLng(40.7128, -74.0060)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 13f))
                    addNearbySheltersAndVets(defaultLocation)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun addNearbySheltersAndVets(center: LatLng) {
        val lat = center.latitude
        val lng = center.longitude

        val locations = listOf(
            Triple(LatLng(lat + 0.01, lng + 0.012), "Happy Paws Animal Shelter 🐾", "Shelter • Open until 7 PM\nPhone: (555) 123-4567"),
            Triple(LatLng(lat - 0.015, lng + 0.008), "City Vet & Animal Hospital 🩺", "Veterinarian • 24/7 Emergency\nPhone: (555) 987-6543"),
            Triple(LatLng(lat + 0.008, lng - 0.014), "Second Chance Kitty Rescue 🐱", "Shelter • Adoption Center\nPhone: (555) 456-7890"),
            Triple(LatLng(lat - 0.009, lng - 0.011), "Paws & Claws Veterinary Clinic 🏥", "Veterinarian • Open until 6 PM\nPhone: (555) 321-0987")
        )

        for ((latLng, title, snippet) in locations) {
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .snippet(snippet)
            )
        }

        Toast.makeText(this, "Found 4 nearby shelters and vets! 🗺️", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableUserLocation()
            } else {
                val defaultLocation = LatLng(40.7128, -74.0060)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 13f))
                addNearbySheltersAndVets(defaultLocation)
                Toast.makeText(this, "Location permission denied. Showing default area.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
