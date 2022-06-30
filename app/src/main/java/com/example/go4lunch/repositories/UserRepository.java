package com.example.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.usecase.GetCurrentUserFromAuthUseCase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static UserRepository userRepository;
    private static final String COLLECTION_NAME = "users";
    private static final String TAG = "MyUserRepository";
    private static final String RESTAURANT_ID_FIELD_NAME = "restaurantChoiceId";
    private static final String RESTAURANT_ADDRESS_FIELD_NAME = "restaurantChoiceAddress";
    private static final String RESTAURANT_NAME_FIELD_NAME = "restaurantChoiceName";
    private final MutableLiveData<List<User>> coworkersComing = new MutableLiveData<>();
    private final MutableLiveData<List<User>> allCoworkers = new MutableLiveData<>();
    private final MutableLiveData<String> userUid = new MutableLiveData<>();

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    public Task<DocumentSnapshot> getUserData() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            return getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    public void fetchCoworkersComing(String placeId) {
        getUsersCollection()
                .whereEqualTo(RESTAURANT_ID_FIELD_NAME, placeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> currentCoworkers = new ArrayList<>();

                        for (QueryDocumentSnapshot documents : task.getResult()) {
                            currentCoworkers.add(documents.toObject(User.class));
                        }

                        coworkersComing.postValue(currentCoworkers);

                    } else {
                        Log.d(TAG, "onComplete: error getting documents" + task.getException());
                    }
                });
    }

    public void fetchAllCoworkers() {
        getUsersCollection()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> allCoworkers = new ArrayList<>();

                        for (QueryDocumentSnapshot documents : task.getResult()) {
                            allCoworkers.add(documents.toObject(User.class));
                        }

                        this.allCoworkers.postValue(allCoworkers);

                    } else {
                        Log.d(TAG, "onComplete: error getting documents" + task.getException());
                    }
                });
    }

    public void createUser() {
        FirebaseUser user = GetCurrentUserFromAuthUseCase.invoke();
        String urlPicture;

        if (user != null) {

            if (user.getPhotoUrl() != null) {
                urlPicture = user.getPhotoUrl().toString();
            } else urlPicture = null;

            String username = user.getDisplayName();
            String uid = user.getUid();
            String email = user.getEmail();

            Task<DocumentSnapshot> userData = getUserData();
            userData.addOnSuccessListener(documentSnapshot -> {

                User userToCreate = new User(uid, username, urlPicture, email);

                if (documentSnapshot.contains(RESTAURANT_ID_FIELD_NAME)) {
                    userToCreate.setRestaurantChoiceId((String) documentSnapshot.get(RESTAURANT_ID_FIELD_NAME));
                    userToCreate.setRestaurantChoiceName((String) documentSnapshot.get(RESTAURANT_NAME_FIELD_NAME));
                    userToCreate.setRestaurantChoiceAddress((String) documentSnapshot.get(RESTAURANT_ADDRESS_FIELD_NAME));
                }

                getUsersCollection().document(uid).set(userToCreate);
                userUid.setValue(user.getUid());

            });
        }
    }

    public String getCurrentUserUID() {
        FirebaseUser user = GetCurrentUserFromAuthUseCase.invoke();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public Task<DocumentSnapshot> getUserWithUid(String uid){
        return getUsersCollection().document(uid).get();
    }

    public MutableLiveData<String> getUserUid(){
        return userUid;
    }

    public MutableLiveData<List<User>> getCoworkersComing() {
        return coworkersComing;
    }

    public MutableLiveData<List<User>> getAllCoworkers() {
        return allCoworkers;
    }

    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public Boolean isCurrentUserLogged() {
        return GetCurrentUserFromAuthUseCase.invoke() != null;
    }

    public void updateUserRestaurantChoice(String placeId, String restaurantName, User currentUser, String restaurantAddress) {
        currentUser.setRestaurantChoiceId(placeId);
        currentUser.setRestaurantChoiceName(restaurantName);
        currentUser.setRestaurantChoiceAddress(restaurantAddress);
        getUsersCollection().document(currentUser.getUid()).set(currentUser);
    }

    public void addFavouritePlace(String placeId, User currentUser) {
        currentUser.likedRestaurants.add(placeId);
        getUsersCollection().document(currentUser.getUid()).set(currentUser);
    }

    public void removeFavouritePlace(String placeId, User currentUser) {
        currentUser.likedRestaurants.remove(placeId);
        getUsersCollection().document(currentUser.getUid()).set(currentUser);
    }

    public void deleteUser(String uid){
        getUsersCollection().document(uid).delete();
    }


}
