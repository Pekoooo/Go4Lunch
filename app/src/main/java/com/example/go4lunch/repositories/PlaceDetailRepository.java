package com.example.go4lunch.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.AppModel.GoogleDetailApiHolder;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.service.GoogleDetailsService;
import com.example.go4lunch.usecase.PreparePlaceModelForDetailViewUseCase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailRepository {

    private static PlaceDetailRepository sPlaceDetailRepository;
    private final GoogleDetailsService googleDetailsService;
    private static final String TAG = "MyRestaurantRepository";

    private final MutableLiveData<Restaurant> placeDetails = new MutableLiveData<>();

    public PlaceDetailRepository() {
        googleDetailsService = GoogleDetailApiHolder.getInstance();
    }

    public static PlaceDetailRepository getInstance() {
        if (sPlaceDetailRepository == null) {
            sPlaceDetailRepository = new PlaceDetailRepository();
        }
        return sPlaceDetailRepository;
    }

    public void searchPlaceDetail(String placeId) {
        googleDetailsService.getDetails(placeId, BuildConfig.API_KEY)
                .enqueue(new Callback<PlaceDetailResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<PlaceDetailResponseModel> call, @NonNull Response<PlaceDetailResponseModel> response) {
                        if (response.body() != null) {
                           Restaurant restaurant = PreparePlaceModelForDetailViewUseCase.invoke(response.body().getResult());
                            placeDetails.setValue(restaurant);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlaceDetailResponseModel> call, @NonNull Throwable t) {
                        placeDetails.setValue(null);
                    }
                });
    }

    public MutableLiveData<Restaurant> getPlaceDetails() {
        return placeDetails;
    }


}



