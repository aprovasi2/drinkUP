package com.example.drinkup.ui.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.drinkup.MainActivity;
import com.example.drinkup.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

public class MapsFragment extends Fragment {

    MapView mMapView;

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                LatLng milan = new LatLng(45.464664, 9.188540);
                LatLng mia = new LatLng(MainActivity.mLatitude, MainActivity.mLongitude);
                //toast per debug
                //Toast.makeText(getContext(), " "+MainActivity.mLatitude + MainActivity.mLongitude, Toast.LENGTH_LONG).show();

                googleMap.addMarker(new MarkerOptions().position(mia).title("Marker Title").snippet("Marker Description"));
                googleMap.getUiSettings().isScrollGesturesEnabled();
                googleMap.getUiSettings().isZoomControlsEnabled();
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                googleMap.getUiSettings().isMyLocationButtonEnabled();
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

               // googleMap.setOnPoiClickListener(GoogleMap.OnPoiClickListener listener);
                googleMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
                    @Override
                    public void onPoiClick(PointOfInterest pointOfInterest) {
                        Toast.makeText(getContext(), "Hai cliccato: " +
                                        pointOfInterest.name,
                                Toast.LENGTH_SHORT).show();

                    }
                });


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mia).zoom(20).build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();

        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}