package com.example.go4lunch.model.AppModel;

import androidx.annotation.Nullable;

import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Restaurant {
    private String restaurantId;
    private String restaurantName;
    private String address;
    private double latitude;
    private double longitude;
    private double distanceUser;
    private int joiningNumber=0;
    @Nullable
    private String photoReference;

    @Nullable
    private Boolean openNow;
    @Nullable
    private List<String> favoriteUserList = new ArrayList<>();
    @Nullable
    private String phone="";
    @Nullable
    private String website="";


    public Restaurant(String restaurantId, String restaurantName, String address, double latitude, double longitude){
        this.restaurantId=restaurantId;
        this.restaurantName=restaurantName;
        this.address=address;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Restaurant(PlaceModel result) {
        this.restaurantId = result.getPlaceId();
        this.restaurantName = result.getName();
        this.address = result.getVicinity();
        this.latitude = result.getGeometry().getLocation().getLat();
        this.longitude = result.getGeometry().getLocation().getLng();
        if(result.getPhotos()!=null && !result.getPhotos().isEmpty())
        this.photoReference = result.getPhotos().get(0).getPhotoReference();
        if(result.getOpeningHours()!=null)
        {
            if(result.getOpeningHours().getOpenNow()!=null)
            {
                this.openNow=result.getOpeningHours().getOpenNow();
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(restaurantId, that.restaurantId);
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    @Nullable
    public String getWebsite() {
        return website;
    }

    public void setWebsite(@Nullable String website) {
        this.website = website;
    }

    @Nullable
    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(@Nullable Boolean openNow) {
        this.openNow = openNow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId);
    }


    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public Double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void resetUserToJoin()
    {
        this.joiningNumber=0;
    }

    public int getJoiningNumber() {
        return joiningNumber;
    }

    public void setJoiningNumber(int joiningNumber) {
        this.joiningNumber = joiningNumber;
    }

    public void setDistanceUser(double distanceUser) {
        this.distanceUser = distanceUser;
    }

    public double getDistanceUser() {
        return distanceUser;
    }

    public void setFavoriteUserList(@Nullable List<String> favoriteUserList) {
        this.favoriteUserList = favoriteUserList;
    }
    public void updateFavoriteUserList(String currentUserId)
    {
        if(isAlreadyFavorite(currentUserId))
        {
            Objects.requireNonNull(favoriteUserList).remove(currentUserId);
        }
        else
        {
            Objects.requireNonNull(favoriteUserList).add(currentUserId);
        }
    }
    public Boolean isAlreadyFavorite(String currentUserId)
    {
        return Objects.requireNonNull(favoriteUserList).contains(currentUserId);
    }

    public void addFavoriteUser(String userId)
    {
        Objects.requireNonNull(favoriteUserList).add(userId);
    }
    public void removeFavoriteUser(String userId)
    {
        Objects.requireNonNull(favoriteUserList).remove(userId);
    }

    @Nullable
    public List<String> getFavoriteUserList() {
        return favoriteUserList;
    }

    public String getPlaceId() {
        return restaurantId;
    }

    public void setPlaceId(String placeId) {
        this.restaurantId = placeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getVicinity() {
        return address;
    }

    public void setVicinity(String vicinity) {
        this.address = vicinity;
    }


}
