package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.utils.RestaurantRepository;

public class ViewModelDetailedView extends AndroidViewModel {

    private static final String TAG = "MyDetailRestaurant";
    private final RestaurantRepository restaurantRepository;

    public ViewModelDetailedView(@NonNull Application application) {
        super(application);
        restaurantRepository = RestaurantRepository.getInstance();
    }

    public void searchPlaceDetail(String placeId){
        Log.d(TAG, "searchPlaceDetail: is Called");
        restaurantRepository.searchPlaceDetail(placeId);
    }

    public MutableLiveData<PlaceDetailResponseModel> getPlaceDetails(){
        return restaurantRepository.getPlaceDetails();
    }
}
