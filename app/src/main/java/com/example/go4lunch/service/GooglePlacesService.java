package com.example.go4lunch.service;

import com.example.go4lunch.model.GooglePlacesModel.GoogleResponseModel;

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
    Call<GoogleResponseModel> searchRestaurants(@Query(value = "location", encoded = true) String location,
                                                @Query("type") String type,
                                                @Query("radius") int radius,
                                                @Query("key") String key);
}
