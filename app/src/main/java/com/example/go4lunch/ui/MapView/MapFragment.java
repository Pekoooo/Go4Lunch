package com.example.go4lunch.ui.MapView;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
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
import com.example.go4lunch.ui.DetailedView.DetailedActivity;
import com.example.go4lunch.utils.BitmapFromVectorUtil;
import com.example.go4lunch.utils.GetBoundsUtil;
import com.example.go4lunch.viewmodel.SharedViewModelRestaurant;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MyMapFragment";
    private FragmentMapBinding binding;
    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap gMap;
    private Location currentLocation;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<User> allUsers = new ArrayList<>();
    private SharedViewModelRestaurant viewModel;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private final List<Place.Field> fields = Arrays.asList(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ID);

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

        setButtons();
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModelRestaurant.class);
        viewModel.fetchCoworkers();
        viewModel.getAllCoworkers().observe(requireActivity(), users ->
                allUsers = users);

        viewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            currentLocation = location;
            initMap();
        });

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
                                currentLocation.getLongitude())
                );
            }

        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);

        if(viewModel.getLocation().getValue() != null){
            LatLng latLng = new LatLng(
                    viewModel.getLocation().getValue().getLatitude(),
                    viewModel.getLocation().getValue().getLongitude());

            moveCamera(latLng);
        }
    }

    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(MapFragment.this);
        }
    }

    private void moveCamera(LatLng latLng) {
        Log.d(TAG, "moveCamera: is called");
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapFragment.DEFAULT_ZOOM));
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d(TAG, "onActivityResult: " + place.getId());

                openDetailedActivity(place.getId());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(requireContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openDetailedActivity(String placeId){
        Intent intent = new Intent(requireContext(), DetailedActivity.class);
        intent.putExtra("placeDetails", placeId);
        startActivity(intent);
    }

}