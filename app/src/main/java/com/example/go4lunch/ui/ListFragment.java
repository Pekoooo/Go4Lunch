package com.example.go4lunch.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.viewmodel.ViewModelRestaurant;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment implements RestaurantRecyclerViewAdapter.OnItemClickListener {

    private static final String TAG = "MyListFragment";
    private ViewModelRestaurant viewModel;
    private RestaurantRecyclerViewAdapter adapter;
    private Location currentLocation;
    private List<PlaceModel> places = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlaceModel placeDetailResult;
    private Restaurant restaurantToDisplay;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: is called");
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelRestaurant.class);
        viewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: is called");
        View v = inflater.inflate(R.layout.fragment_list_restaurant, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getListOfRestaurants().observe(requireActivity(), nearbyResponseModel -> {
            if (nearbyResponseModel != null) {
                Log.d(TAG, "onChanged: nearbyResponseModel is not null, setting the places in adapter");

                places = nearbyResponseModel.getResults();
                adapter.setRestaurants(places);

            } else {
                Log.d(TAG, "onChanged: nearbyResponseModel null, cannot set the places in adapter");
            }
        });

        viewModel.location.observe(requireActivity(), location -> {
            Log.d(TAG, "onChanged: location passed in onChanged is: " + location.getLatitude());
            currentLocation = location;
        });



        searchRestaurants();
    }

    private void searchRestaurants() {
        Log.d(TAG, "searchRestaurants: is called");
        String latlng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        viewModel.searchRestaurants(latlng);
        setRecyclerView();
    }

    public void setRecyclerView() {
        Log.d(TAG, "setRecyclerView: is called");
        adapter = new RestaurantRecyclerViewAdapter(places, currentLocation, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: is called");

        String placeId = places.get(position).getPlaceId();


        Intent intent = new Intent(requireContext(), DetailedActivity.class);
        intent.putExtra("placeDetails", placeId);

        startActivity(intent);

    }


}




