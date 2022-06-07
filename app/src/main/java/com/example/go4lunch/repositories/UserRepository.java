package com.example.go4lunch.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.usecase.GetCurrentUserFromAuthUseCase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "userName";
    private static final String RESTAURANT_CHOICE_ID = "restaurantChoiceId";
    private static final String RESTAURANT_CHOICE_NAME = "restaurantChoiceName";
    private final MutableLiveData<List<User>> coworkersComing = new MutableLiveData<>();
    private final MutableLiveData<List<User>> allCoworkers = new MutableLiveData<>();

    private static final String TAG = "MyUserRepository";
    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    private CollectionReference getUsersCollection() {
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

    public void fetchCoworkersComing(String placeId) {
        Log.d(TAG, "fetchCoworkersComing: is called");
        getUsersCollection()
                .whereEqualTo("restaurantChoiceId", placeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> currentCoworkers = new ArrayList<>();
                        for (QueryDocumentSnapshot documents : task.getResult()) {
                            currentCoworkers.add(documents.toObject(User.class));
                            Log.d(TAG, "onComplete: adding: " + documents.toObject(User.class).getUserName() + " to the list");

                        }

                        coworkersComing.postValue(currentCoworkers);

                    } else {
                        Log.d(TAG, "onComplete: error getting documents" + task.getException());
                    }
                });
    }

    public void fetchAllCoworkers() {
        Log.d(TAG, "fetchCoworkersComing: is called");
        getUsersCollection()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> allCoworkers = new ArrayList<>();
                        for (QueryDocumentSnapshot documents : task.getResult()) {
                            allCoworkers.add(documents.toObject(User.class));
                            Log.d(TAG, "onComplete: adding: " + documents.toObject(User.class).getUserName() + " to the list");
                        }
                        this.allCoworkers.postValue(allCoworkers);
                    } else {
                        Log.d(TAG, "onComplete: error getting documents" + task.getException());
                    }
                });
    }

    public MutableLiveData<List<User>> getCoworkersComing() {
        return coworkersComing;
    }

    public MutableLiveData<List<User>> getAllCoworkers() {
        return allCoworkers;
    }

    public String getCurrentUserUID() {
        FirebaseUser user = GetCurrentUserFromAuthUseCase.invoke();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public void createUser() {
        FirebaseUser user = GetCurrentUserFromAuthUseCase.invoke();
        if (user != null) {
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

    public void updateUserRestaurantChoice(String placeId, String restaurantName, User currentUser) {
        currentUser.setRestaurantChoiceId(placeId);
        currentUser.setRestaurantChoiceName(restaurantName);
        this.getUsersCollection().document(currentUser.getUid()).set(currentUser);
    }

    public void addFavouritePlace(String placeId, User currentUser) {
        currentUser.likedRestaurants.add(placeId);
        getUsersCollection().document(currentUser.getUid()).set(currentUser);
    }

    public void removeFavouritePlace(String placeId, User currentUser) {
        currentUser.likedRestaurants.remove(placeId);
        getUsersCollection().document(currentUser.getUid()).set(currentUser);
    }
}
