package com.example.go4lunch.usecase;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.UserRepository;
import com.google.android.gms.tasks.Task;

public class GetCurrentUserFromDBUseCase {

    public static Task<User> invoke() {
        return UserRepository.getInstance().getUserData().continueWith(task ->
                task.getResult().toObject(User.class));
    }
}
