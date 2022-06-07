package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.repositories.RestaurantRepository;
import com.example.go4lunch.repositories.UserRepository;

import java.util.List;

public class ViewModelDetailedView extends AndroidViewModel {

    private static final String TAG = "MyDetailRestaurant";
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public ViewModelDetailedView(@NonNull Application application) {
        super(application);
        restaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public void searchPlaceDetail(String placeId) {
        restaurantRepository.searchPlaceDetail(placeId);
    }

    public MutableLiveData<PlaceDetailResponseModel> getPlaceDetails() {
        return restaurantRepository.getPlaceDetails();
    }

    public void updateUserRestaurantChoice(String placeId, String restaurantName, User currentUser) {
        userRepository.updateUserRestaurantChoice(placeId, restaurantName, currentUser);
    }

    public MutableLiveData<List<User>> getCoworkers() {
        return userRepository.getCoworkersComing();
    }

    public void fetchCoworkersComing(String placeId) {
        Log.d(TAG, "fetchCoworkersComing: is called");
        userRepository.fetchCoworkersComing(placeId);
    }

    public void addFavouritePlace(String placeId, User currentUser) {
        userRepository.addFavouritePlace(placeId, currentUser);
    }

    public void removeFavouritePlace(String placeId, User currentUser) {
        userRepository.removeFavouritePlace(placeId, currentUser);
    }
}
