package com.example.go4lunch.ui;

import static android.graphics.Color.RED;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;

import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.RestaurantResultHolder> {

    private List<PlaceModel> place;
    private final Location currentLocation;
    private static final String TAG = "MyRestoRecyclerView";
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public RestaurantRecyclerViewAdapter(List<PlaceModel> placeModels, Location currentLocation, OnItemClickListener onItemClickListener) {
        Log.d(TAG, "RestaurantRecyclerViewAdapter: constructor is being called");
        this.place = placeModels;
        this.currentLocation = currentLocation;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RestaurantRecyclerViewAdapter.RestaurantResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list_row, parent, false);

        return new RestaurantRecyclerViewAdapter.RestaurantResultHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerViewAdapter.RestaurantResultHolder holder, int position) {

        //Gets the current restaurant
        PlaceModel currentPlaceModel = place.get(position);
        String PlacePhotoApiCall = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                + currentPlaceModel.getPhotos().get(0).getPhotoReference()
                + "&key=AIzaSyDwt4HaFs_pyttzXrf9lEZF5IMgyDkVcN4";



        //Gets the current restaurant's lat lng
        double currentRestaurantLatitude  =  currentPlaceModel.getGeometry().getLocation().getLat();
        double currentRestaurantLongitude =  currentPlaceModel.getGeometry().getLocation().getLng();

        //Creates a Location with current restaurant's lat lnt
        Location currentRestaurantLocation = new Location("Current PlaceModel");
        currentRestaurantLocation.setLatitude(currentRestaurantLatitude);
        currentRestaurantLocation.setLongitude(currentRestaurantLongitude);

        //Calculates the distance from the user's current location to the destination
        float distance = currentLocation.distanceTo(currentRestaurantLocation);

        holder.restaurantName.setText(currentPlaceModel.getName());
        holder.restaurantInfo.setText(currentPlaceModel.getVicinity());
        holder.restaurantStars.setText(String.valueOf(currentPlaceModel.getRating()));
        holder.restaurantDistance.setText(Math.round(distance) + "m");
        //holder.coworkersGoing.setText(firebase database...);

        if (currentPlaceModel.getOpeningHours().getOpenNow()){
            holder.openOrClosed.setText(R.string.open);
        } else {
            holder.openOrClosed.setText(R.string.close);
            holder.openOrClosed.setTextColor(RED);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

       Glide.with(holder.restaurantImage.getContext())
               .load(PlacePhotoApiCall)
               .apply(requestOptions)
               .into(holder.restaurantImage);

    }

    @Override
    public int getItemCount() {
        return place.size();
    }

    public void setRestaurants(List<PlaceModel> placeModels){
        Log.d(TAG, "setRestaurants: is Called");
        this.place = placeModels;
        notifyDataSetChanged();
    }

    static class RestaurantResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private final TextView restaurantName;
       private final TextView restaurantInfo;
       private final TextView openOrClosed;
       private final TextView restaurantDistance;
       private final TextView coworkersGoing;
       private final TextView restaurantStars;
       private final ImageView restaurantImage;

       OnItemClickListener onItemClickListener;

        public RestaurantResultHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;

           restaurantName     = itemView.findViewById(R.id.restaurant_name);
           restaurantInfo     = itemView.findViewById(R.id.restaurant_info);
           openOrClosed       = itemView.findViewById(R.id.open_text);
           restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
           coworkersGoing     = itemView.findViewById(R.id.number_coworkers_going);
           restaurantStars    = itemView.findViewById(R.id.restaurant_stars);
           restaurantImage    = itemView.findViewById(R.id.restaurant_image);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: is called");
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
