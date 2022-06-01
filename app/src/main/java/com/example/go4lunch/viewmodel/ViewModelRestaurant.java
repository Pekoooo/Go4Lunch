package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.GooglePlacesModel.NearbyResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.utils.RestaurantRepository;

public class ViewModelRestaurant extends AndroidViewModel {

    private static final String TAG = "MyViewModelRestaurant";
    private final RestaurantRepository restaurantRepository;
    private final MutableLiveData<NearbyResponseModel> listOfRestaurants;
    public MutableLiveData<Location> location = new MutableLiveData<>();

    public ViewModelRestaurant(@NonNull Application application) {
        super(application);
        restaurantRepository = RestaurantRepository.getInstance();
        listOfRestaurants = restaurantRepository.getListOfRestaurants();

    }

    public void sendLocation(Location location) {
        this.location.postValue(location);
    }

    public void searchRestaurants(String latlng) {
        restaurantRepository.searchRestaurants(latlng);
    }

    public MutableLiveData<NearbyResponseModel> getListOfRestaurants() {
        return listOfRestaurants;
    }


}
