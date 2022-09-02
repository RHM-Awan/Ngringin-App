package com.rahmawan.ngringinapk.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var bind: ActivityMapBinding
    private lateinit var maps: GoogleMap
    private lateinit var latLng:LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMapBinding.inflate(layoutInflater)
        setContentView(bind.root)
        latLng=intent.extras?.getParcelable("latLng")!!
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fc_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        maps.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Lokasi Disini")
        )
    }
}