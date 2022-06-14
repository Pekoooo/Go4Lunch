package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.repositories.MainRepository;
import com.example.go4lunch.repositories.UserRepository;

public class MainViewModel extends AndroidViewModel {

    private final MainRepository mainRepository;
    private final UserRepository userRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mainRepository = MainRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public void searchRestaurants(Location currentLocation) {
        mainRepository.searchRestaurants(currentLocation);
    }

    public void createUser() {
        userRepository.createUser();
    }

    public Boolean isCurrentUserLogged() {
        return userRepository.isCurrentUserLogged();
    }
}