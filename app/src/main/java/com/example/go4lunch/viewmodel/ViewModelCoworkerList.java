package com.example.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.UserRepository;

import java.util.List;

public class ViewModelCoworkerList extends AndroidViewModel {
    private final UserRepository userRepository;

    public ViewModelCoworkerList(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    @VisibleForTesting
    public ViewModelCoworkerList(Application application, UserRepository userRepository){
        super(application);
        this.userRepository = userRepository;
    }

    public MutableLiveData<List<User>> getAllCoworkers() {
        return userRepository.getAllCoworkers();
    }
}
