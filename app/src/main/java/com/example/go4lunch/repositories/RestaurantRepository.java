package com.example.go4lunch.repositories;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.AppModel.GooglePlacesApiHolder;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.NearbyResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.service.GooglePlacesService;
import com.example.go4lunch.usecase.PrepareListPlaceModelForViewUseCase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private static RestaurantRepository sRestaurantRepository;
    private final GooglePlacesService googlePlacesService;

    private static final String TAG = "MyMainRepository";
    private static final String DEFAULT_TYPE_SEARCH = "restaurant";
    private static final int DEFAULT_RADIUS_SEARCH = 1000;

    private final MutableLiveData<List<Restaurant>> listOfRestaurants = new MutableLiveData<>();
    private final MutableLiveData<Location> location = new MutableLiveData<>();

    public RestaurantRepository() {
        googlePlacesService = GooglePlacesApiHolder.getInstance();
    }

    public static RestaurantRepository getInstance() {
        if (sRestaurantRepository == null) {
            sRestaurantRepository = new RestaurantRepository();
        }
        return sRestaurantRepository;
    }

    public void searchRestaurants(Location currentLocation) {
        String latlng = getLatLngToString(currentLocation);
        location.setValue(currentLocation);
        googlePlacesService.searchRestaurants(latlng, DEFAULT_TYPE_SEARCH, DEFAULT_RADIUS_SEARCH, BuildConfig.API_KEY)
                .enqueue(new Callback<NearbyResponseModel>() {

                    @Override
                    public void onResponse(@NonNull Call<NearbyResponseModel> call, @NonNull Response<NearbyResponseModel> response) {
                        if (response.body() != null) {
                            List<PlaceModel> result = response.body().getResults();
                            List<Restaurant> restaurants = PrepareListPlaceModelForViewUseCase.invoke(result, currentLocation);
                            listOfRestaurants.setValue(restaurants);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NearbyResponseModel> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: Google places API call failed" + t.getCause());
                        listOfRestaurants.setValue(null);
                    }
                });
    }

    public MutableLiveData<List<Restaurant>> getListOfRestaurants() {
        return listOfRestaurants;
    }

    public MutableLiveData<Location> getLocation() {
        return location;
    }

    private String getLatLngToString(Location location) {
        return location.getLatitude() + "," + location.getLongitude();
    }
}
