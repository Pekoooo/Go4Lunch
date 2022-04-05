package com.example.go4lunch.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.viewmodel.ViewModelRestaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MyMapFragment";
    private static final float DEFAULT_ZOOM = 15f;
    private LatLng cameraPosition;
    private GoogleMap gMap;
    private Location currentLocation;
    private ViewModelRestaurant viewModelRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: is called");
        return inflater.inflate(R.layout.fragment_map, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: is called");
        super.onViewCreated(view, savedInstanceState);
        viewModelRestaurant = new ViewModelProvider(this).get(ViewModelRestaurant.class);

        initMap();
        getDeviceLocation();


        FloatingActionButton centerUserBtn = view.findViewById(R.id.gps_center_on_user);
        centerUserBtn.setOnClickListener(v -> {

            if(currentLocation != null){
                moveCamera(new LatLng(
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude()),
                        DEFAULT_ZOOM);
                getNearbyRestaurants();
            }

        });
    }

    @Override
    public void onStop() {
        super.onStop();
         cameraPosition = gMap.getCameraPosition().target;
    }

    private void getNearbyRestaurants() {
        String latlng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        Log.d(TAG, "getNearbyRestaurants: current location = " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
        viewModelRestaurant.searchRestaurants(latlng);
        viewModelRestaurant.location.postValue(currentLocation);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map is ready");
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);

    }

    private void initMap() {
        Log.d(TAG, "initMap: Initializing map");
        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        if (supportMapFragment != null) {
            Log.d(TAG, "initMap: getting map async");
            supportMapFragment.getMapAsync(MapFragment.this);
        }
    }

    void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        try {
            final Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Found location");
                        currentLocation = (Location) task.getResult();

                        if(cameraPosition == null){
                            moveCamera(new LatLng(
                                            currentLocation.getLatitude(),
                                            currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }
                    }
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: Moving the camera to:" + latLng.latitude + ", lng:" + latLng.longitude);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
}