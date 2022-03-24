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

    private static final String TAG = "ViewModelRestaurant";
    private final RestaurantRepository restaurantRepository;
    private MutableLiveData<PlacesNearbySearchResponse> listOfRestaurants;

    
    public ViewModelRestaurant(@NonNull Application application) {
        super(application);
        restaurantRepository = RestaurantRepository.getInstance();
        listOfRestaurants = restaurantRepository.getListOfRestaurants();

    }


    public void searchRestaurants(String latlng, String type, int radius) {
        Log.d(TAG, "searchRestaurants: is Called");
        restaurantRepository.searchRestaurants(latlng, type, radius, "AIzaSyDwt4HaFs_pyttzXrf9lEZF5IMgyDkVcN4");
    }

    public MutableLiveData<PlacesNearbySearchResponse> getListOfRestaurants(){
        return listOfRestaurants;
    }




}
