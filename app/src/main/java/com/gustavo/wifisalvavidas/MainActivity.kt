package com.gustavo.wifisalvavidas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gustavo.wifisalvavidas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wifiManager: WifiManager
    private lateinit var locationManager: LocationManager
    private var googleMap: GoogleMap? = null
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val LOCATION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            googleMap = map
            getLocationAndUpdateMap()
        }

        binding.btnScan.setOnClickListener { scanWiFiNetworks() }

        checkPermissions()
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), LOCATION_REQUEST_CODE)
        } else {
            scanWiFiNetworks()
        }
    }

    private fun getLocationAndUpdateMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            var location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                googleMap?.addMarker(MarkerOptions().position(latLng).title("Tu ubicación"))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
    }

    private fun getSignalLevel(level: Int): String {
        return when {
            level >= -50 -> "📶 Excelente"
            level >= -65 -> "📶 Buena"
            level >= -75 -> "📶 Estable"
            else -> "📶 Débil"
        }
    }

    private fun scanWiFiNetworks() {
        binding.tvNoNetworks.visibility = View.GONE
        wifiManager.startScan()
        handler.postDelayed({
            val scanResults = wifiManager.scanResults
            val openNetworks = scanResults.filter {
                it.capabilities.isEmpty() && it.SSID.isNotBlank()
            }
            binding.recyclerView.adapter = WiFiAdapter(openNetworks) { level ->
                getSignalLevel(level)
            }
            binding.tvNoNetworks.visibility = if (openNetworks.isEmpty()) View.VISIBLE else View.GONE
            getLocationAndUpdateMap()
        }, 2000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanWiFiNetworks()
        } else {
            Toast.makeText(this, "Permiso de ubicación necesario", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}

class WiFiAdapter(
    private val networks: List<ScanResult>,
    private val getSignalLabel: (Int) -> String
) : androidx.recyclerview.widget.RecyclerView.Adapter<WiFiViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): WiFiViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_wifi, parent, false)
        return WiFiViewHolder(view)
    }

    override fun onBindViewHolder(holder: WiFiViewHolder, position: Int) {
        val network = networks[position]
        holder.txtSsid.text = network.SSID
        holder.txtSignal.text = getSignalLabel(network.level)
        holder.btnConnect.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = networks.size
}

class WiFiViewHolder(itemView: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    val txtSsid: TextView = itemView.findViewById(R.id.txtSsid)
    val txtSignal: TextView = itemView.findViewById(R.id.txtSignal)
    val btnConnect: Button = itemView.findViewById(R.id.btnConnect)
}
