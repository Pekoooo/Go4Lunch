package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.PlaceDetailRepository;
import com.example.go4lunch.repositories.UserRepository;

import java.util.List;

public class ViewModelDetailedView extends AndroidViewModel {

    private static final String TAG = "MyDetailRestaurant";
    private final PlaceDetailRepository placeDetailRepository;
    private final UserRepository userRepository;

    public ViewModelDetailedView(@NonNull Application application) {
        super(application);
        placeDetailRepository = PlaceDetailRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    @VisibleForTesting
    public ViewModelDetailedView(UserRepository userRepository, PlaceDetailRepository placeDetailRepository, Application application) {
        super(application);
        this.userRepository = userRepository;
        this.placeDetailRepository = placeDetailRepository;
    }

    public void searchPlaceDetail(String placeId) {
        placeDetailRepository.searchPlaceDetail(placeId);
    }

    public MutableLiveData<Restaurant> getPlaceDetails() {
        return placeDetailRepository.getPlaceDetails();
    }

    public MutableLiveData<List<User>> getCoworkers() {
        return userRepository.getCoworkersComing();
    }

    public void fetchCoworkersComing(String placeId) {
        userRepository.fetchCoworkersComing(placeId);
    }

    public void addFavouritePlace(String placeId, User currentUser) {
        userRepository.addFavouritePlace(placeId, currentUser);
    }

    public void removeFavouritePlace(String placeId, User currentUser) {
        userRepository.removeFavouritePlace(placeId, currentUser);
    }

    public void updateUserRestaurantChoice(String placeId, String restaurantName, User currentUser, String restaurantAddress) {
        userRepository.updateUserRestaurantChoice(placeId, restaurantName, currentUser, restaurantAddress);
    }
}
