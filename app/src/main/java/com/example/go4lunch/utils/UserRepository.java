package com.example.go4lunch.utils;

import android.util.Log;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.usecase.GetCurrentUserFromAuthUseCase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "userName";
    private static final String RESTAURANT_CHOICE_ID = "restaurantChoiceId";

    private static final String TAG = "MyUserRepository";
    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();

        } else {
            return null;
        }
    }

    public String getCurrentUserUID() {
        FirebaseUser user = GetCurrentUserFromAuthUseCase.invoke();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }


    public void createUser() {
        Log.d(TAG, "createUser: is called");
        FirebaseUser user = GetCurrentUserFromAuthUseCase.invoke();
        if (user != null) {
            Log.d(TAG, "createUser: user not null");
            String urlPicture;
            if (user.getPhotoUrl() != null) {
                urlPicture = user.getPhotoUrl().toString();
            } else urlPicture = null;
            String username = user.getDisplayName();
            String uid = user.getUid();
            String email = user.getEmail();

            User userToCreate = new User(uid, username, urlPicture, email);

            Task<DocumentSnapshot> userData = getUserData();

            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(RESTAURANT_CHOICE_ID)) {
                    userToCreate.setRestaurantChoiceId((String) documentSnapshot.get(RESTAURANT_CHOICE_ID));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }


    public Boolean isCurrentUserLogged() {
        Log.d(TAG, "isCurrentUserLogged: is called");
        return (GetCurrentUserFromAuthUseCase.invoke() != null);
    }

    public void updateUserRestaurantChoice(String placeId, User currentUser) {
        currentUser.setRestaurantChoiceId(placeId);
        this.getUsersCollection().document(currentUser.getUid()).set(currentUser);

    }
}
