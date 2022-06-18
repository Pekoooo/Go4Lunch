package com.example.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.go4lunch.repositories.UserRepository;

public class SettingsViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        userRepository = UserRepository.getInstance();
    }

    public void deleteUser(String uid){
        userRepository.deleteUser(uid);
    }
}
