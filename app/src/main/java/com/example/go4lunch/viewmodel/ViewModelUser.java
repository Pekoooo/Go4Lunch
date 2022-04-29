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
    private  UserRepository userRepository;

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

    public Task<User> getUserData() {
        // Get the user from Firestore and cast it to a User model Object
        //Continue with transforms the document snapshot to the desired object type
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class));
    }
}
