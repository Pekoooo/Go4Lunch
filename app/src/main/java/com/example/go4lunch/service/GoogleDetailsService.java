package com.example.go4lunch.service;

import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleDetailsService {

    @GET("maps/api/place/details/json")
    Call<PlaceDetailResponseModel> getDetails(@Query("place_id") String place_id,
                                              @Query("key") String key);
}
