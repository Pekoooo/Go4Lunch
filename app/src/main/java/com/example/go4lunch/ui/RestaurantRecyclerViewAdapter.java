package com.example.go4lunch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.databinding.RestaurantListRowBinding;
import com.example.go4lunch.model.GooglePlacesAPI.Place;

import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private final List<Place> places;

    public RestaurantRecyclerViewAdapter(List<Place> places){

        this.places = places;

    }



    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RestaurantListRowBinding binding = RestaurantListRowBinding.inflate(layoutInflater, parent, false);
        return new RestaurantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
       // holder.rowBinding.restaurantName.setText(this.restaurants.get(position));

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
