package com.example.go4lunch;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment {
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        if(supportMapFragment != null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    //When map is loaded
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            //When clicked on map
                            //Init marker options :

                            MarkerOptions markerOptions = new MarkerOptions();
                            //Set pos of marker
                            markerOptions.position(latLng);
                            //Set title of marker
                            markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                            //Remove all marker
                            googleMap.clear();
                            //Animating to soom the marker
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng, 10
                            ));
                            //Add marker on map
                            googleMap.addMarker(markerOptions);
                        }
                    });

                }
            });
        }
        return view;
    }
}