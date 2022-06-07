package com.example.go4lunch.repositories;

import android.location.Location;
import android.util.Log;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.NearbyResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.service.GoogleDetailsService;
import com.example.go4lunch.service.GooglePlacesService;
import com.example.go4lunch.usecase.PrepareResponseModelForViewUseCase;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private static final int DEFAULT_RADIUS_SEARCH = 1000;
    private static RestaurantRepository restaurantRepository;
    private final GooglePlacesService googlePlacesService;
    private final GoogleDetailsService googleDetailsService;
    private static final String TAG = "MyRestaurantRepository";
    private static final String DEFAULT_TYPE_SEARCH = "restaurant";
    private final MutableLiveData<List<Restaurant>> listOfRestaurants;
    private final MutableLiveData<PlaceDetailResponseModel> placeDetails;
    private final LruCache<String, NearbyResponseModel> cache = new LruCache<>(2_000);

    public RestaurantRepository() {
        listOfRestaurants = new MutableLiveData<>();
        placeDetails = new MutableLiveData<>();

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

        googleDetailsService = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(GoogleDetailsService.class);
    }

    public static RestaurantRepository getInstance() {
        if (restaurantRepository == null) {
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }

    public void searchRestaurants(Location currentLocation) {
        Log.d(TAG, "searchRestaurants: is called");
        String latlng = getLatLngToString(currentLocation);
        googlePlacesService.searchRestaurants(latlng, DEFAULT_TYPE_SEARCH, DEFAULT_RADIUS_SEARCH, BuildConfig.API_KEY)
                .enqueue(new Callback<NearbyResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<NearbyResponseModel> call, @NonNull Response<NearbyResponseModel> response) {
                        if (response.body() != null) {
                            List<PlaceModel> result = response.body().getResults();
                            List<Restaurant> restaurants = PrepareResponseModelForViewUseCase.invoke(result, currentLocation);
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

    public void searchPlaceDetail(String placeId) {
        googleDetailsService.getDetails(placeId, BuildConfig.API_KEY)
                .enqueue(new Callback<PlaceDetailResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<PlaceDetailResponseModel> call, @NonNull Response<PlaceDetailResponseModel> response) {
                        if (response.body() != null) {
                            placeDetails.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlaceDetailResponseModel> call, @NonNull Throwable t) {
                        placeDetails.setValue(null);
                    }
                });
    }

    public MutableLiveData<List<Restaurant>> getListOfRestaurants() {
        return listOfRestaurants;
    }

    public MutableLiveData<PlaceDetailResponseModel> getPlaceDetails() {
        return placeDetails;
    }

    private String getLatLngToString(Location location){
        return  location.getLatitude() + "," + location.getLongitude();
    }

}



