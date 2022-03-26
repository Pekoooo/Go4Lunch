package com.example.go4lunch.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.AppModel.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.RestaurantResultHolder> {

    private List<Restaurant> restaurants = new ArrayList<>();
    private static final String TAG = "MyRestoRecyclerView";


    @NonNull
    @Override
    public RestaurantRecyclerViewAdapter.RestaurantResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Creating ViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list_row, parent, false);

        return new RestaurantResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerViewAdapter.RestaurantResultHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: is Called");
        Restaurant currentRestaurant = restaurants.get(position);
        holder.restaurantName.setText(currentRestaurant.getName());





    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: is Called");
        return restaurants.size();
    }

    public void setRestaurants(List<Restaurant> restaurants){
        Log.d(TAG, "setRestaurants: is Called");
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    static class RestaurantResultHolder extends RecyclerView.ViewHolder {

       private final TextView restaurantName;
      // private final TextView restaurantInfo;
      // private final TextView openOrClosed;
      // private final TextView restaurantDistance;
      // private final TextView coworkersGoing;
      // private final TextView restaurantStars;

        public RestaurantResultHolder(@NonNull View itemView) {
            super(itemView);

           restaurantName     = itemView.findViewById(R.id.restaurant_name);
          // restaurantInfo     = itemView.findViewById(R.id.restaurant_info);
          // openOrClosed       = itemView.findViewById(R.id.open_text);
          // restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
          // coworkersGoing     = itemView.findViewById(R.id.number_coworkers_going);
          // restaurantStars    = itemView.findViewById(R.id.restaurant_stars);


        }
    }
}
