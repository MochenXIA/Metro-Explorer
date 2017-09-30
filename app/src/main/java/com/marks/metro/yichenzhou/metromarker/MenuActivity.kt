package com.marks.metro.yichenzhou.metromarker


import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.marks.metro.yichenzhou.metromarker.helper.AppHelper
import com.marks.metro.yichenzhou.metromarker.model.MetroStation
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_menu.*
import kotlin.properties.Delegates

class MenuActivity : AppCompatActivity() {
    private val TAG = "MenuActivity"
    private var realm: Realm by Delegates.notNull()
    private var locationManager: LocationManager? = null

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d(TAG, "(Long:Lang): " + location.longitude + " : " + location.latitude)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Properties Initialization
        Realm.init(applicationContext)
        this.realm = Realm.getDefaultInstance()
        this.locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationTestButton.setOnClickListener {
            try {
                this.locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            } catch (e: SecurityException) {
                Log.e(TAG, e.message)
            }
        }

        val stationCount = this.realm.where(MetroStation::class.java).findAll().count()
        if (stationCount== 0) {
            Log.d(TAG, "No metro station data, working on it now")
            AppHelper.loadStationsData("Stations.csv", applicationContext)
            Log.d(TAG, "Done")
        } else {
            Log.d(TAG, "Metro station data exists, count: ${stationCount.toString()}")
        }
    }
}
