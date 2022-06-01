package com.example.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.utils.UserRepository;

import java.util.List;

public class ViewModelCoworkerList extends AndroidViewModel {
    private UserRepository userRepository;

    public ViewModelCoworkerList(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    public MutableLiveData<List<User>> getAllCoworkers() {
        return userRepository.getAllCoworkers();
    }

    public void fetchAllCoworkers() {
        userRepository.fetchAllCoworkers();
    }
}
