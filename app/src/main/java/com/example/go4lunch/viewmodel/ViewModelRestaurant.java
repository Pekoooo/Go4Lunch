package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlacesAPI.PlacesNearbySearchResponse;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.ui.MapFragment;
import com.example.go4lunch.utils.RestaurantRepository;

import java.util.List;

public class ViewModelRestaurant extends AndroidViewModel {
    
    private final RestaurantRepository restaurantRepository;
    private static final String TAG = "ViewModelRestaurant";
    
    public ViewModelRestaurant(@NonNull Application application) {
        super(application);
        restaurantRepository = RestaurantRepository.getInstance();
    }


    public MutableLiveData<PlacesNearbySearchResponse>
    fetchNearByRestaurants(String latlng, String type, int radius) {
        return restaurantRepository.fetchNearbyRestaurants(latlng, type, radius);
    }
}
