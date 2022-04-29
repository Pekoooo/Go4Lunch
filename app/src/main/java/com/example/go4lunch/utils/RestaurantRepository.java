package com.example.go4lunch.utils;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.GooglePlacesModel.NearbyResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.service.GoogleDetailsService;
import com.example.go4lunch.service.GooglePlacesService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private static final int DEFAULT_RADIUS_SEARCH = 500;
    private static final int DEFAULT_MAX_WIDTH = 600;
    private static final String FIELDS = "place_id,international_phone_number,opening_hours,website";
    private static RestaurantRepository restaurantRepository;
    private final GooglePlacesService googlePlacesService;
    private final GoogleDetailsService googleDetailsService;
    private static final String TAG = "MyRestaurantRepository";
    private final MutableLiveData<NearbyResponseModel> listOfRestaurants;
    private final MutableLiveData<PlaceDetailResponseModel> placeDetails;


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
        Log.d(TAG, "getInstance: Getting PlaceModel Repo instance");
        if (restaurantRepository == null) {
            Log.d(TAG, "getInstance: Initializing repository");
            restaurantRepository = new RestaurantRepository();
        }
        return restaurantRepository;
    }



    public void searchRestaurants(String latlng){
        Log.d(TAG, "searchRestaurants: is Called");

        String DEFAULT_TYPE_SEARCH = "restaurant";
        googlePlacesService.searchRestaurants(latlng, DEFAULT_TYPE_SEARCH, DEFAULT_RADIUS_SEARCH, BuildConfig.API_KEY)
                .enqueue(new Callback<NearbyResponseModel>() {
                    @Override
                    public void onResponse(Call<NearbyResponseModel> call, Response<NearbyResponseModel> response) {
                        if(response.body() != null){
                            Log.d(TAG, "onResponse: Google places API call succeeded");
                            listOfRestaurants.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<NearbyResponseModel> call, Throwable t) {
                        Log.d(TAG, "onFailure: Google places API call failed" + t.getCause());
                        listOfRestaurants.postValue(null);

                    }
                });
    }


    public void searchPlaceDetail(String placeId){
        Log.d(TAG, "searchPlaceDetail: is Called");
        // TODO : Move key to BuildConfig. ...
        
        googleDetailsService.getDetails(placeId, BuildConfig.API_KEY)
                .enqueue(new Callback<PlaceDetailResponseModel>() {
                    @Override
                    public void onResponse(Call<PlaceDetailResponseModel> call, Response<PlaceDetailResponseModel> response) {
                        if(response.body() != null){
                            Log.d(TAG, "onResponse: Google places detail API call succeeded");
                            placeDetails.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaceDetailResponseModel> call, Throwable t) {
                        Log.d(TAG, "onFailure: Google places detail API call failed");
                        placeDetails.postValue(null);
                    }
                });
    }

    public MutableLiveData<NearbyResponseModel> getListOfRestaurants() {
        return listOfRestaurants;
    }

    public MutableLiveData<PlaceDetailResponseModel> getPlaceDetails() {
        return placeDetails;
    }

}



