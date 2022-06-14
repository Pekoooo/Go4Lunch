package com.example.go4lunch.model.AppModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private String uid;
    private String userName;
    private String avatarURL;
    private String email;
    private String restaurantChoiceId;
    private String restaurantChoiceName;
    public List<String> likedRestaurants = new ArrayList<>();
    private String restaurantChoiceAddress;



    //Empty constructor for DB.
    public User(){

    }

    public User(String uid, String userName, @Nullable String avatarURL, String email) {
        this.uid = uid;
        this.userName = userName;
        this.avatarURL = avatarURL;
        this.email = email;
    }

    public String getRestaurantChoiceName() {
        return restaurantChoiceName;
    }

    public void setRestaurantChoiceName(String restaurantChoiceName) {
        this.restaurantChoiceName = restaurantChoiceName;
    }

    public String getRestaurantChoiceId() {
        return restaurantChoiceId;
    }

    public void setRestaurantChoiceId(String restaurantChoiceId) {
        this.restaurantChoiceId = restaurantChoiceId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRestaurantChoiceAddress() {
        return restaurantChoiceAddress;
    }

    public void setRestaurantChoiceAddress(String restaurantChoiceAddress) {
        this.restaurantChoiceAddress = restaurantChoiceAddress;
    }

    public List<String> getLikedRestaurants() {

        return likedRestaurants;
    }

    public void setLikedRestaurants(List<String> likedRestaurants) {
        this.likedRestaurants = likedRestaurants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User userModel = (User) o;
        return Objects.equals(uid, userModel.uid) &&
                Objects.equals(userName, userModel.userName) &&
                Objects.equals(avatarURL, userModel.avatarURL) &&
                Objects.equals(email, userModel.email);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserModel{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
