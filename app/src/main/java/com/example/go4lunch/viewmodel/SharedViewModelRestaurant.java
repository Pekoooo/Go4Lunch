package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.RestaurantRepository;
import com.example.go4lunch.repositories.UserRepository;

import java.util.List;

public class SharedViewModelRestaurant extends AndroidViewModel {

    private static final String TAG = "MyViewModelRestaurant";
    private final UserRepository userRepository;
    private final RestaurantRepository mRestaurantRepository;

    public SharedViewModelRestaurant(@NonNull Application application) {
        super(application);

        mRestaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();

    }

    @VisibleForTesting
    public SharedViewModelRestaurant(Application application, RestaurantRepository restaurantRepository, UserRepository userRepository ){
        super(application);
        this.userRepository = userRepository;
        this.mRestaurantRepository = restaurantRepository;
    }

    public MutableLiveData<Location> getLocation() {
        return mRestaurantRepository.getLocation();
    }

    public MutableLiveData<List<Restaurant>> getListOfRestaurants() {
        return mRestaurantRepository.getListOfRestaurants();
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
