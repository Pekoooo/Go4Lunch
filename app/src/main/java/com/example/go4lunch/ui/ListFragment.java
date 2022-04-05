package com.example.go4lunch.ui;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.model.GooglePlacesModel.GoogleResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.viewmodel.ViewModelRestaurant;


import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private static final String TAG = "MyListFragment";
    private ViewModelRestaurant viewModel;
    private RestaurantRecyclerViewAdapter adapter;
    private Location currentLocation = new Location("ViewModelRestaurant");
    private List<PlaceModel> places = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: is Called");
        viewModel = new ViewModelProvider(this).get(ViewModelRestaurant.class);
        viewModel.init();


        viewModel.location.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                Log.d(TAG, "onChanged: Location is called");

                currentLocation = location;
            }
        });

        viewModel.getListOfRestaurants().observe(this, new Observer<GoogleResponseModel>() {
            @Override
            public void onChanged(GoogleResponseModel googleResponseModel) {
                if (googleResponseModel != null) {
                    Log.d(TAG, "onChanged: googleResponseModel is not null, setting the places in adapter");

                    places = googleResponseModel.getResults();
                    adapter.setRestaurants(places);

                } else {
                    Log.d(TAG, "onChanged: googleResponseModel null, cannot set the places in adapter");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: is Called");
        View v = inflater.inflate(R.layout.fragment_list_restaurant, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchRestaurants();

        return v;
    }

    private void searchRestaurants() {
        Log.d(TAG, "searchRestaurants: is Called");
        String latlng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        viewModel.searchRestaurants(latlng);
        setRecyclerView();

    }

    public void setRecyclerView() {
        Log.d(TAG, "setRecyclerView: is called");
        adapter = new RestaurantRecyclerViewAdapter(places, currentLocation);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

    }

}

   //private void searchRestaurants() {
   //    Log.d(TAG, "searchRestaurants: is Called");
   //    //TODO : Move the location data to repository
   //    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

   //    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
   //            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
   //        return;
   //    }

   //    final Task location = fusedLocationProviderClient.getLastLocation();
   //    location.addOnCompleteListener(new OnCompleteListener() {
   //        @Override
   //        public void onComplete(@NonNull Task task) {
   //            if (task.isSuccessful()) {
   //                Log.d(TAG, "onComplete: Found location ");
   //                currentLocation = (Location) task.getResult();
   //                String currentLatLng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();

   //                viewModel.searchRestaurants(currentLatLng);

   //                setRecyclerView();
   //            }
   //        }
   //    });

   //}


