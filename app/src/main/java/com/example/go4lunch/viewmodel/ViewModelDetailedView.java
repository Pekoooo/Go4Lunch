package com.example.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.utils.RestaurantRepository;
import com.example.go4lunch.utils.UserRepository;
import com.google.android.gms.tasks.Task;

public class ViewModelDetailedView extends AndroidViewModel {

    private static final String TAG = "MyDetailRestaurant";
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public ViewModelDetailedView(@NonNull Application application) {
        super(application);
        restaurantRepository = RestaurantRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public void searchPlaceDetail(String placeId){
        restaurantRepository.searchPlaceDetail(placeId);
    }

    public MutableLiveData<PlaceDetailResponseModel> getPlaceDetails(){
        return restaurantRepository.getPlaceDetails();
    }

    public Task<User> getUserData() {
        // Get the user from Firestore and cast it to a User model Object
        //Continue with transforms the document snapshot to the desired object type
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public void updateUserRestaurantChoice(String placeId, User currentUser) {
        userRepository.updateUserRestaurantChoice(placeId, currentUser);
    }
}
