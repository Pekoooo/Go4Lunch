package com.example.go4lunch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;

    private GoogleMap gMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    gMap = googleMap;

                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                            gMap.clear();
                            gMap.addMarker(new MarkerOptions().position(userLatLong));
                            gMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));

                        }
                    };


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
                            gMap.clear();
                            //Animating to zoom the marker
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng, 10
                            ));
                            //Add marker on map
                            gMap.addMarker(markerOptions);
                        }
                    });
                }
            });
        }
        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}