package com.example.go4lunch.utils;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlacesModel.GoogleResponseModel;
import com.example.go4lunch.service.GooglePlacesService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private static final int DEFAULT_RADIUS_SEARCH = 200;
    private final String DEFAULT_TYPE_SEARCH = "restaurant";
    private static RestaurantRepository restaurantRepository;
    private final GooglePlacesService googlePlacesService;
    private static final String TAG = "MyRestaurantRepository";
    private final MutableLiveData<GoogleResponseModel> listOfRestaurants;


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

    public void searchRestaurants(String latlng){
        Log.d(TAG, "searchRestaurants: is Called");
        // TODO : Move key to BuildConfig. ...
        googlePlacesService.searchRestaurants(latlng, DEFAULT_TYPE_SEARCH, DEFAULT_RADIUS_SEARCH, "AIzaSyDwt4HaFs_pyttzXrf9lEZF5IMgyDkVcN4")
                .enqueue(new Callback<GoogleResponseModel>() {
                    @Override
                    public void onResponse(Call<GoogleResponseModel> call, Response<GoogleResponseModel> response) {
                        if(response.body() != null){
                            Log.d(TAG, "onResponse: API Call succeeded");
                            listOfRestaurants.postValue(response.body());
                        } else {

                            Log.d(TAG, "onResponse: body might be null");
                        }
                    }

                    @Override
                    public void onFailure(Call<GoogleResponseModel> call, Throwable t) {
                        Log.d(TAG, "onFailure: API Call failed");
                        listOfRestaurants.postValue(null);

                    }
                });
    }

    public MutableLiveData<GoogleResponseModel> getListOfRestaurants() {
        return listOfRestaurants;
    }
}



