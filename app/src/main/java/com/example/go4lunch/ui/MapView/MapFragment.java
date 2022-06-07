package com.example.go4lunch.ui.MapView;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentMapBinding;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.utils.BitmapFromVectorUtil;
import com.example.go4lunch.utils.GetBoundsUtil;
import com.example.go4lunch.viewmodel.SharedViewModelRestaurant;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MapFragment extends Fragment implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private static final String TAG = "MyMapFragment";
    private FragmentMapBinding binding;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private GoogleMap gMap;
    private Location currentLocation;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<User> allUsers = new ArrayList<>();
    private SharedViewModelRestaurant viewModel;
    private final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private final List<Place.Field> fields = Arrays.asList(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG);

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: is called");
        super.onAttach(context);
        //On attach because guideline requires one call per app start
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModelRestaurant.class);
        viewModel.fetchCoworkers();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: is called");

        binding = FragmentMapBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        Places.initialize(requireContext(), BuildConfig.API_KEY);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        double radius = 2.0;
        double[] boundsFromLatLng = GetBoundsUtil.getBoundsFromLatLng(radius, currentLocation.getLatitude(), currentLocation.getLongitude());
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setLocationRestriction(RectangularBounds.newInstance(
                        new LatLng(boundsFromLatLng[0], boundsFromLatLng[1]),
                        new LatLng(boundsFromLatLng[2], boundsFromLatLng[3])
                ))
                .build(MapFragment.this.requireContext());
        MapFragment.this.startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        zoomOnLocation();
        initMap();
        setButtons();
        viewModel.getAllCoworkers().observe(requireActivity(), users ->
                allUsers = users);

        viewModel.getListOfRestaurants().observe(requireActivity(), restaurants -> {
            restaurantList = restaurants;
            List<Restaurant> restaurantWithFavourite = viewModel.setRestaurantWithFavourite(allUsers, restaurantList);
            setRestaurantMarker(restaurantWithFavourite);
        });
    }

    private void setButtons() {
        binding.gpsCenterOnUser.setOnClickListener(v -> {
            if (currentLocation != null) {
                moveCamera(new LatLng(
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude()),
                        DEFAULT_ZOOM
                );
            }
        });
    }

    @AfterPermissionGranted(LOCATION_PERMISSION_REQUEST_CODE)
    private void zoomOnLocation() {
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            getDeviceLocation();

        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale),
                    LOCATION_PERMISSION_REQUEST_CODE,
                    perms);
        }
    }

    private void getNearbyRestaurants() {
        viewModel.searchRestaurants(currentLocation);
        viewModel.sendLocation(currentLocation);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(MapFragment.this);
        }
    }

    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        try {
            final Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentLocation = task.getResult();
                    getNearbyRestaurants();

                    moveCamera(new LatLng(
                                    currentLocation.getLatitude(),
                                    currentLocation.getLongitude()),
                            DEFAULT_ZOOM
                    );
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void setRestaurantMarker(List<Restaurant> restaurants) {
        if (gMap != null) {
            gMap.clear();
            for (int i = 0; i < restaurants.size(); i++) {

                Restaurant currentRestaurant = restaurants.get(i);
                if (currentRestaurant.isFavourite()) {
                    gMap.addMarker(new MarkerOptions()
                            .position(currentRestaurant.getLatLng())
                            .title(currentRestaurant.getName())
                            .icon(BitmapFromVectorUtil.BitmapFromVector(getContext(), R.drawable.ic_restaurant_corowker_going)));
                } else {

                    gMap.addMarker(new MarkerOptions()
                            .position(currentRestaurant.getLatLng())
                            .title(currentRestaurant.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker()));
                }
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initMap();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale),
                    LOCATION_PERMISSION_REQUEST_CODE, this.perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            initMap();
            zoomOnLocation();
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                moveCamera(place.getLatLng(), 20);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(requireContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}