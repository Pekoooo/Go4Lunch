package com.example.go4lunch.utils;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlacesAPI.PlacesNearbySearchResponse;
import com.example.go4lunch.service.GooglePlacesService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private static RestaurantRepository restaurantRepository;
    private final GooglePlacesService googlePlacesService;
    private static final String TAG = "RestaurantRepository";
    private final MutableLiveData<PlacesNearbySearchResponse> listOfRestaurants;

    public RestaurantRepository() {

        listOfRestaurants = new MutableLiveData<>();

        googlePlacesService = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GooglePlacesService.class);
    }

    public static RestaurantRepository getInstance() {
        Log.d(TAG, "getInstance: Getting Restaurant Repo instance");
        if (restaurantRepository == null) {
            Log.d(TAG, "getInstance: Initializing repository");
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }

    public void searchRestaurants(String latlng, String type, int radius, String apiKey){
        Log.d(TAG, "searchRestaurants: is Called");
        googlePlacesService.searchRestaurants(latlng, type, radius, "AIzaSyDwt4HaFs_pyttzXrf9lEZF5IMgyDkVcN4")
                .enqueue(new Callback<PlacesNearbySearchResponse>() {
                    @Override
                    public void onResponse(Call<PlacesNearbySearchResponse> call, Response<PlacesNearbySearchResponse> response) {
                        if(response.body() == null){
                            Log.d(TAG, "onResponse: body is null");
                        } else {
                            Log.d(TAG, "onResponse: API Call succeeded");
                            listOfRestaurants.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<PlacesNearbySearchResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: API Call failed");
                        listOfRestaurants.postValue(null);

                    }
                });
    }

    public MutableLiveData<PlacesNearbySearchResponse> getListOfRestaurants() {
        return listOfRestaurants;
    }
}



