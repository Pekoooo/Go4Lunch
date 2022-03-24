package com.example.go4lunch.service;

import com.example.go4lunch.model.GooglePlacesAPI.PlacesNearbySearchResponse;
import com.example.go4lunch.model.Restaurant;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("maps/api/place/nearbysearch/json")
    Call<PlacesNearbySearchResponse> getNearbyResponse(@Query(value = "location", encoded = true) String location,
                                                       @Query("type") String type,
                                                       @Query("radius") int radius,
                                                       @Query("key") String key);
}
