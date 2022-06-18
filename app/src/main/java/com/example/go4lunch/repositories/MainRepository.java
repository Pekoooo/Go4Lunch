package com.example.go4lunch.repositories;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.NearbyResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.service.GooglePlacesService;
import com.example.go4lunch.usecase.PreparePlaceModelForViewUseCase;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRepository {
    private static MainRepository mainRepository;
    private static final String TAG = "MyMainRepository";
    private static final String DEFAULT_TYPE_SEARCH = "restaurant";
    private static final int DEFAULT_RADIUS_SEARCH = 1000;
    private final GooglePlacesService googlePlacesService;
    private final MutableLiveData<List<Restaurant>> listOfRestaurants = new MutableLiveData<>();
    private final MutableLiveData<Location> location = new MutableLiveData<>();

    public MainRepository() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client =
                new OkHttpClient.Builder()
                        .addInterceptor(
                                loggingInterceptor
                        )
                        .build();

        googlePlacesService = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(GooglePlacesService.class);
    }

    public static MainRepository getInstance() {
        if (mainRepository == null) {
            mainRepository = new MainRepository();
        }
        return mainRepository;
    }

    public void searchRestaurants(Location currentLocation) {
        Log.d(TAG, "searchRestaurants: is called");
        location.setValue(currentLocation);
        String latlng = getLatLngToString(currentLocation);
        googlePlacesService.searchRestaurants(latlng, DEFAULT_TYPE_SEARCH, DEFAULT_RADIUS_SEARCH, BuildConfig.API_KEY)
                .enqueue(new Callback<NearbyResponseModel>() {

                    @Override
                    public void onResponse(@NonNull Call<NearbyResponseModel> call, @NonNull Response<NearbyResponseModel> response) {
                        if (response.body() != null) {
                            List<PlaceModel> result = response.body().getResults();
                            List<Restaurant> restaurants = PreparePlaceModelForViewUseCase.invoke(result, currentLocation);
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

    private String getLatLngToString(Location location) {
        return location.getLatitude() + "," + location.getLongitude();
    }

    public MutableLiveData<Location> getLocation() {
        return location;
    }
}
