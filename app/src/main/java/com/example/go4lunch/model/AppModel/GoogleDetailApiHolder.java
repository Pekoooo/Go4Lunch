package com.example.go4lunch.model.AppModel;

import androidx.annotation.VisibleForTesting;

import com.example.go4lunch.service.GoogleDetailsService;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleDetailApiHolder {

    public static final String BASE_URL = "https://maps.googleapis.com/";

    public static GoogleDetailsService getInstance(){
        return getInstance(HttpUrl.get(BASE_URL));
    }


    @VisibleForTesting
    public static GoogleDetailsService getInstance(HttpUrl baseUrl){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client =
                new OkHttpClient.Builder()
                        .addInterceptor(
                                loggingInterceptor
                        )
                        .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(GoogleDetailsService.class);
    }
}
