package com.example.go4lunch.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GetCurrentUserRestaurantChoiceIDUseCase {

    public static String invoke() {
        return GetCurrentUserFromDBUseCase.invoke().getResult().getRestaurantChoiceId();
    }
}
