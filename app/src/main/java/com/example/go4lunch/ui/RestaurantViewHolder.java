package com.example.go4lunch.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.go4lunch.databinding.RestaurantListRowBinding;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    RestaurantListRowBinding rowBinding;

    public RestaurantViewHolder(@NonNull RestaurantListRowBinding itemBinding) {
        super(itemBinding.getRoot());

        rowBinding = itemBinding;

        //todo : Implements interface for handling click on item (cf Ma Reu)
    }
}
