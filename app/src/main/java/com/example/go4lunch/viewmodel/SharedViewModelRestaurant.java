package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
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
    private final PlaceDetailRepository mPlaceDetailRepository;
    private final MainRepository mainRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<List<Restaurant>> listOfRestaurants;
    private final MutableLiveData<List<User>> listOfUsers;
    private MutableLiveData<Location> location = new MutableLiveData<>();

    public SharedViewModelRestaurant(@NonNull Application application) {
        super(application);

        mPlaceDetailRepository  = PlaceDetailRepository.getInstance();
        mainRepository          = MainRepository.getInstance();
        userRepository          = UserRepository.getInstance();

        listOfRestaurants  = mainRepository.getListOfRestaurants();
        location           = mainRepository.getLocation();
        listOfUsers        = userRepository.getAllCoworkers();


    }

    public MutableLiveData<Location> getLocation() {
        return location;
    }

    public MutableLiveData<List<Restaurant>> getListOfRestaurants() {
        return listOfRestaurants;
    }

    public void fetchCoworkers(){
        userRepository.fetchAllCoworkers();
    }

    public MutableLiveData<List<User>> getAllCoworkers(){
        return listOfUsers;
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
