package com.example.go4lunch.usecase;

import androidx.annotation.NonNull;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.UserRepository;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class GetCurrentUserFromDBUseCase {

    public static Task<User> invoke() {
        // Get the user from Firestore and cast it to a User model Object
        //Continue with transforms the document snapshot to the desired object type
        return UserRepository.getInstance().getUserData().continueWith(task ->
                task.getResult().toObject(User.class));
    }
}
