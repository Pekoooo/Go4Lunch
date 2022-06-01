package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.utils.UserRepository;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class ViewModelUser extends AndroidViewModel {
    private static final String TAG = "MyUserViewModel";
    private final UserRepository userRepository;

    public ViewModelUser(@NonNull @NotNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    public Boolean isCurrentUserLogged() {
        return userRepository.isCurrentUserLogged();
    }

    public void createUser() {
        userRepository.createUser();
    }


}
