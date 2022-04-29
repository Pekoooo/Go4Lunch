package com.example.go4lunch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentMapBinding;
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

import java.util.List;

import kotlin.reflect.KFunction;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MapFragment extends Fragment implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private static final String TAG = "MyMapFragment";
    private FragmentMapBinding binding;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private LatLng cameraPosition;
    private GoogleMap gMap;
    private Location currentLocation;
    private ViewModelRestaurant viewModelRestaurant;
    private final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * From dev.android.com →
     * Note: Fragments outlive their views. Make sure you clean up any references
     * to the binding class instance in the fragment's onDestroyView() method.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModelRestaurant = new ViewModelProvider(requireActivity()).get(ViewModelRestaurant.class);
        zoomOnLocation();
        initMap();
        setButtons();
    }

    private void setButtons() {
        binding.gpsCenterOnUser.setOnClickListener(v -> {

            if (currentLocation != null) {
                moveCamera(new LatLng(
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude()),
                        DEFAULT_ZOOM);
                getNearbyRestaurants();
            }
        });
    }

    @AfterPermissionGranted(LOCATION_PERMISSION_REQUEST_CODE)
    private void zoomOnLocation() {
        Log.d(TAG, "zoomOnLocation: is called");

        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            Log.d(TAG, "zoomOnLocation: perms granted");
            getDeviceLocation();

        } else {
            Log.d(TAG, "zoomOnLocation: requesting perms");
            EasyPermissions.requestPermissions(this, getString(R.string.rationale), LOCATION_PERMISSION_REQUEST_CODE, perms);
        }
    }

    private void getNearbyRestaurants() {
        String latlng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        Log.d(TAG, "getNearbyRestaurants: current location = " + latlng);
        viewModelRestaurant.searchRestaurants(latlng);
        viewModelRestaurant.sendLocation(currentLocation);
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
        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        if (supportMapFragment != null) {
            Log.d(TAG, "initMap: Initializing map");
            supportMapFragment.getMapAsync(MapFragment.this);
        } else {
            Log.d(TAG, "initMap: Initializing map failed");
        }
    }


    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        try {
            final Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Found location");
                        currentLocation = (Location) task.getResult();
                        getNearbyRestaurants();

                        if (cameraPosition == null) {
                            moveCamera(new LatLng(
                                            currentLocation.getLatitude(),
                                            currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                        } else {
                            moveCamera(cameraPosition, DEFAULT_ZOOM);
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


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted: permission granted");
        initMap();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Log.d(TAG, "onPermissionsDenied: Some permissions permanently denied, asking user to change app settings");
            new AppSettingsDialog.Builder(this).build().show();

        } else {
            Log.d(TAG, "onPermissionsDenied: requesting permission with rationale");
            EasyPermissions.requestPermissions(this, getString(R.string.rationale),
                    LOCATION_PERMISSION_REQUEST_CODE, this.perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: is called");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            initMap();
            zoomOnLocation();
        }
    }
}