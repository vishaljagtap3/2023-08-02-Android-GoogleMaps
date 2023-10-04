package com.bitcodetech.googlemapsdemo

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.bitcodetech.googlemapsdemo.databinding.ActivityMapsBinding
import com.bitcodetech.googlemapsdemo.databinding.InfoWindowBinding
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var puneMarker: Marker
    private lateinit var mumMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        initSettings()
        addMarkers()
        addListeners()
        addInfoWindowAdapter()
        addShapes()
    }

    fun addShapes() {
        val circleOptions = CircleOptions()
        circleOptions.center(puneMarker.position)
        circleOptions.radius(5000.0)
            .fillColor(Color.argb(80, 255, 60, 50))
            .strokeColor(Color.RED)
        val circle = map.addCircle(circleOptions)
        //circle.remove()

        map.addPolygon(
            PolygonOptions()
                .add(LatLng(28.7040, 77.1025))
                .add(LatLng(22.5726, 88.3669))
                .add(LatLng(17.3850, 78.4875))
                .add(LatLng(23.2599, 77.4126))
                .strokeColor(Color.BLACK)
                .fillColor(Color.argb(90, 24, 23, 255))
        )

    }

    private fun addInfoWindowAdapter() {
        val infoWindowAdapter = object : InfoWindowAdapter {

            override fun getInfoWindow(marker: Marker): View? {
                /*val binding = InfoWindowBinding.inflate(layoutInflater)
                binding.txt.text = marker.title + "\n" + marker.snippet
                binding.img.setImageResource(R.drawable.location)
                return binding.root*/
                return null
            }

            override fun getInfoContents(marker: Marker): View? {
                val binding = InfoWindowBinding.inflate(layoutInflater)
                binding.txt.text = marker.title + "\n" + marker.snippet
                binding.img.setImageResource(R.drawable.location)
                return binding.root
            }
        }

        map.setInfoWindowAdapter(infoWindowAdapter)
    }

    private fun addListeners() {
        map.setOnMapClickListener(
            object : GoogleMap.OnMapClickListener {
                override fun onMapClick(loc: LatLng) {
                    map.addMarker(
                        MarkerOptions()
                            .title("Some place on earth!")
                            .position(loc)
                    )
                    animateCamera()
                }
            }
        )

        map.setOnMarkerClickListener(
            object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    mt("Marker clicked: ${marker.title}")
                    return false
                }
            }
        )

        map.setOnInfoWindowClickListener {
            mt("Info window clicked: ${it.title}")
        }

        map.setOnMarkerDragListener(
            object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {
                    mt("Drag started: ${marker.title}")
                }

                override fun onMarkerDrag(marker: Marker) {
                    Log.e(
                        "tag",
                        "Marker drag: ${marker.position.latitude} , ${marker.position.longitude}"
                    )
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    mt("Drag ended: ${marker.title}")
                }
            }
        )
    }

    private fun animateCamera() {

        val cameraPosition = CameraPosition.builder()
            .target(puneMarker.position)
            .zoom(21.toFloat())
            .tilt(90.toFloat())
            .bearing(65.toFloat())
            .build()

        /*val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            puneMarker.position,
            18.toFloat()
        )*/
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(
            cameraPosition
        )


        //map.moveCamera(cameraUpdate)
        map.animateCamera(
            cameraUpdate,
            10000,
            object : GoogleMap.CancelableCallback {
                override fun onCancel() {
                    mt("interrupted")
                }

                override fun onFinish() {
                    mt("finished")
                }

            }
        )

    }

    private fun addMarkers() {
        val puneMarkerOptions =
            MarkerOptions()
        puneMarkerOptions.title("Pune")
        puneMarkerOptions.position(LatLng(18.5204, 73.8567))
        puneMarkerOptions.draggable(true)
        puneMarkerOptions.rotation(45.0F)
        puneMarkerOptions.snippet("This is Pune!")

        /* val puneIcon = BitmapDescriptorFactory
             .fromResource(R.drawable.location)
         puneMarkerOptions.icon(puneIcon)*/

        puneMarker = map.addMarker(puneMarkerOptions)!!
        //puneMarker.remove()
        //puneMarker.isVisible = false

        val icon = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)

        map.addMarker(
            MarkerOptions()
                .title("Mumbai")
                .position(LatLng(19.0760, 72.8777))
                .snippet("This is Mumbai")
                .icon(icon)

        )
    }

    @SuppressLint("MissingPermission")
    private fun initSettings() {

        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this,
                R.raw.map_style
            )
        )

        map.isMyLocationEnabled = true

        map.uiSettings.isMapToolbarEnabled = true
        map.uiSettings.isTiltGesturesEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isRotateGesturesEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true

        //map.mapType = GoogleMap.MAP_TYPE_SATELLITE
    }

    private fun mt(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}