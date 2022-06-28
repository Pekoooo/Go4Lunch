package com.example.go4lunch.service;

import com.example.go4lunch.model.GooglePlacesModel.NearbyResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesService {

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbyResponseModel> searchRestaurants(@Query(value = "location", encoded = true) String location,
                                                @Query("type") String type,
                                                @Query("radius") int radius,
                                                @Query("key") String key);
}
