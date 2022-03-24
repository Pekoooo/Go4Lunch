package com.example.go4lunch.utils;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlacesAPI.PlacesNearbySearchResponse;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.service.GooglePlacesService;
import com.google.firebase.BuildConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private static RestaurantRepository restaurantRepository;
    private static final String TAG = "RestaurantRepository";
    private final MutableLiveData<PlacesNearbySearchResponse> listOfRestaurants = new MutableLiveData<>();


    public static RestaurantRepository getInstance() {
        Log.d(TAG, "getInstance: Getting Restaurant Repo instance");
        if (restaurantRepository == null) {
            Log.d(TAG, "getInstance: Initializing repository");
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }


    public  MutableLiveData<PlacesNearbySearchResponse> fetchNearbyRestaurants (String latlng, String type, int radius) {
        //TODO Implements API REQUEST HERE
        //TODO definir radius et type ici
        GooglePlacesService googlePlacesService = GooglePlacesService.retrofit.create(GooglePlacesService.class);
        Call<PlacesNearbySearchResponse> listOfRestaurantOut = googlePlacesService.getNearbyResponse(latlng, type, radius, "AIzaSyDwt4HaFs_pyttzXrf9lEZF5IMgyDkVcN4");

        listOfRestaurantOut.enqueue(new Callback<PlacesNearbySearchResponse>() {
            @Override
            public void onResponse(Call<PlacesNearbySearchResponse> call, Response<PlacesNearbySearchResponse> response) {
                Log.d(TAG, "onResponse: API Call succeeded");
                listOfRestaurants.setValue(response.body());
            }

            @Override
            public void onFailure(Call<PlacesNearbySearchResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: API Call failed");

                listOfRestaurants.postValue(null);
            }
        });

        return listOfRestaurants;
    }
}



