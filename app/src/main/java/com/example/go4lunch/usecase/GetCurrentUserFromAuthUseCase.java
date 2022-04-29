package com.example.go4lunch.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GetCurrentUserFromAuthUseCase {

    public static FirebaseUser invoke() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
