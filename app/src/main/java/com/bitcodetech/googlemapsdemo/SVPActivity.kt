package com.bitcodetech.googlemapsdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitcodetech.googlemapsdemo.databinding.SvpActivityBinding
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng

class SVPActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SvpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val svpFragment = (supportFragmentManager.findFragmentById(R.id.svpFragment)
        as SupportStreetViewPanoramaFragment)

        svpFragment.getStreetViewPanoramaAsync(
            object : OnStreetViewPanoramaReadyCallback {
                override fun onStreetViewPanoramaReady(svp : StreetViewPanorama) {
                    svp.setPosition(LatLng(40.7128, 74.0060))
                }
            }
        )

    }

}