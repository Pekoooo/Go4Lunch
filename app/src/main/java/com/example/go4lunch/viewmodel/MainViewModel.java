package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.repositories.MainRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainViewModel extends AndroidViewModel {

    private final MainRepository mainRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<String> userUid;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mainRepository = MainRepository.getInstance();
        userRepository = UserRepository.getInstance();
        userUid = UserRepository.getInstance().getUserUid();

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

    public Task<DocumentSnapshot> getUserWithUid(String uid){
        return userRepository.getUserWithUid(uid);
    }

    public MutableLiveData<String> getUserUid(){
        return userUid;
    }
}