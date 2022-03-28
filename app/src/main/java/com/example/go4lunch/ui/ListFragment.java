package com.example.go4lunch.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.model.GooglePlacesModel.GoogleResponseModel;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.viewmodel.ViewModelRestaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private static final String TAG = "MyListFragment";
    private ViewModelRestaurant viewModel;
    private RestaurantRecyclerViewAdapter adapter;
    private Location currentLocation;
    private List<Restaurant>  restaurantsTest = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: is Called");
        adapter = new RestaurantRecyclerViewAdapter();
        viewModel = new ViewModelProvider(this).get(ViewModelRestaurant.class);
        viewModel.init();
        viewModel.getListOfRestaurants().observe(this, new Observer<GoogleResponseModel>() {
            @Override
            public void onChanged(GoogleResponseModel googleResponseModel) {
                Log.d(TAG, "onChanged: is Called");
                if (googleResponseModel != null) {
                    Log.d(TAG, "onChanged: googleResponseModel is not null, setting the restaurants in adapter");

                    restaurantsTest = googleResponseModel.getResults();
                    adapter.setRestaurants(restaurantsTest);
                    //adapter.setRestaurants(restaurantsTest);
                    Log.d(TAG, adapter.getItemCount() + " places in the list ");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: is Called");
        View v = inflater.inflate(R.layout.fragment_list_restaurant, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        getRestaurantsNearby();

        return v;
    }

    private void getRestaurantsNearby() {
        Log.d(TAG, "getRestaurantsNearby: is Called");
        //TODO : Move the location data to repository
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        final Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Found location ");
                        currentLocation = (Location) task.getResult();
                        String currentLatLng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();

                        viewModel.searchRestaurants(currentLatLng);
                    }
                }
            });

    }

    public double distanceToLocation(Restaurant restaurant) {

        double latitude = restaurant.getGeometry().getLocation().getLongitude();
        double longitude = restaurant.getGeometry().getLocation().getLongitude();

        Location startPoint = new Location("start");
        startPoint.setLatitude(latitude);
        startPoint.setLongitude(longitude);

        return currentLocation.distanceTo(startPoint);



    }
}