package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.MainRepository;
import com.example.go4lunch.repositories.PlaceDetailRepository;
import com.example.go4lunch.repositories.UserRepository;

import java.util.List;

public class SharedViewModelRestaurant extends AndroidViewModel {

    private static final String TAG = "MyViewModelRestaurant";
    private final UserRepository userRepository;
    private final MainRepository mainRepository;

    public SharedViewModelRestaurant(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance();
        userRepository = UserRepository.getInstance();

    }

    @VisibleForTesting
    public SharedViewModelRestaurant(Application application, MainRepository mainRepository, UserRepository userRepository ){
        super(application);
        this.userRepository = userRepository;
        this.mainRepository = mainRepository;
    }

    public MutableLiveData<Location> getLocation() {
        return mainRepository.getLocation();
    }

    public MutableLiveData<List<Restaurant>> getListOfRestaurants() {
        return mainRepository.getListOfRestaurants();
    }

    public void fetchCoworkers(){
        userRepository.fetchAllCoworkers();
    }

    public MutableLiveData<List<User>> getAllCoworkers(){
        return userRepository.getAllCoworkers();
    }

    public List<Restaurant> setRestaurantWithFavourite(List<User> allUsers, List<Restaurant> restaurantList) {

        for (int i = 0; i < restaurantList.size(); i++) {
            Restaurant currentRestaurant = restaurantList.get(i);

            for (int j = 0; j < allUsers.size(); j++) {
                User currentUser = allUsers.get(j);
                if(currentUser.getRestaurantChoiceId() != null){
                    if (currentUser.getRestaurantChoiceId().equals(currentRestaurant.getPlaceId())) {
                        currentRestaurant.setFavourite(true);
                        break;
                    } else {
                        currentRestaurant.setFavourite(false);
                    }
                }
            }
        }
        return restaurantList;
    }
}
