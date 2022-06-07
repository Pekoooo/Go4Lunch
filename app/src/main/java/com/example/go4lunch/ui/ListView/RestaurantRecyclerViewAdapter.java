package com.example.go4lunch.ui.ListView;

import static android.graphics.Color.RED;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.AppModel.Restaurant;

import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.RestaurantResultHolder> {

    private List<Restaurant> place;
    private static final String TAG = "MyRestoRecyclerView";
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RestaurantRecyclerViewAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RestaurantResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list_row, parent, false);

        return new RestaurantResultHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantResultHolder holder, int position) {

        Restaurant currentRestaurant = place.get(position);
        String distanceToDisplay = Math.round(currentRestaurant.getDistance()) + " m";

        String placePhotoApiCall;
        if(currentRestaurant.getPhotoReference() == null){
            placePhotoApiCall = "https://media.istockphoto.com/photos/variety-food-on-a-table-cloth-restaurant-directly-above-picture-id1235685137?k=20&m=1235685137&s=612x612&w=0&h=ddicngEKM9UbIKflROe3tMj7DnC8VVT6F_m_jct2afs=";
        } else {
            placePhotoApiCall = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photo_reference="
                    + currentRestaurant.getPhotoReference()
                    + "&key=" + BuildConfig.API_KEY;
        }

        holder.restaurantName.setText(currentRestaurant.getName());
        holder.restaurantInfo.setText(currentRestaurant.getAddress());
        holder.restaurantStars.setRating(currentRestaurant.getRating());
        holder.restaurantDistance.setText(distanceToDisplay);
        //holder.coworkersGoing.setText(firebase database...);

        if(currentRestaurant.isOpen()){
                holder.openOrClosed.setText(R.string.open);
            } else if(!currentRestaurant.isOpen()) {
                holder.openOrClosed.setText(R.string.close);
                holder.openOrClosed.setTextColor(RED);
            }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(holder.restaurantImage.getContext())
                .load(placePhotoApiCall)
                .apply(requestOptions)
                .into(holder.restaurantImage);
    }

    @Override
    public int getItemCount() {
        return place.size();
    }

    public void setRestaurants(List<Restaurant> placeModels) {
        this.place = placeModels;
        notifyDataSetChanged();
    }

    static class RestaurantResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView restaurantName;
        private final TextView restaurantInfo;
        private final TextView openOrClosed;
        private final TextView restaurantDistance;
        private final TextView coworkersGoing;
        private final RatingBar restaurantStars;
        private final ImageView restaurantImage;

        OnItemClickListener onItemClickListener;

        public RestaurantResultHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;

            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantInfo = itemView.findViewById(R.id.restaurant_info);
            openOrClosed = itemView.findViewById(R.id.open_text);
            restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
            coworkersGoing = itemView.findViewById(R.id.number_coworkers_going);
            restaurantStars = itemView.findViewById(R.id.rating_bar);
            restaurantImage = itemView.findViewById(R.id.restaurant_image);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: is called");
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
