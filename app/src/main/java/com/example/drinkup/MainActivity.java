package com.example.drinkup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Date;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    public static double mLongitude = 20;
    public static double mLatitude = 30;
    private GoogleMap mGoogleMap;
    private String mProviderId = LocationManager.GPS_PROVIDER;
    private Geocoder mGeo = null;
    private LocationManager mLocationManager = null;
    private static final int MIN_DIST = 20;
    private static final int MIN_PERIOD = 30000;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            // attivo GPS su dispositivo
            // updateText(R.id.enabled, "TRUE");
        }

        @Override
        public void onProviderDisabled(String provider) {
            // disattivo GPS su dispositivo
            //updateText(R.id.enabled, "FALSE");
        }

        @Override
        public void onLocationChanged(Location location) {
            updateGUI(location);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_preferiti, R.id.navigation_maps, R.id.navigation_profilo, R.id.activityDrink)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGeo = new Geocoder(this, Locale.getDefault());
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);



            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null)
            updateGUI(location);
        if (mLocationManager != null && mLocationManager.isProviderEnabled(mProviderId))
            mLocationManager.requestLocationUpdates(mProviderId, MIN_PERIOD, MIN_DIST, mLocationListener);
    }

    private void updateGUI(Location location) {
        Date timestamp = new Date(location.getTime());

        mLatitude = location.getLatitude();

        mLongitude = location.getLongitude();

    }
}

