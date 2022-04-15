package com.example.go4lunch.model.AppModel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Restaurant implements Parcelable {
    private String photo;
    private String restaurantId;
    private String restaurantName;
    private String address;
    private double latitude;
    private double longitude;
    private double distanceUser;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

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

    public Restaurant(){

    }


    public Restaurant(String restaurantId, String restaurantName, String address, double latitude, double longitude){
        this.restaurantId=restaurantId;
        this.restaurantName=restaurantName;
        this.address=address;
        this.latitude=latitude;
        this.longitude=longitude;
    }

   public Restaurant(PlaceModel resultFromPlace, PlaceModel resultFromDetail) {
       this.website = resultFromDetail.getWebsite();
       this.phone = resultFromDetail.getPhone();
       this.photo = resultFromPlace.getPhotos().get(0).getPhotoReference();
       this.restaurantId = resultFromPlace.getPlaceId();
       this.restaurantName = resultFromPlace.getName();
       this.address = resultFromPlace.getVicinity();
       this.latitude = resultFromPlace.getGeometry().getLocation().getLat();
       this.longitude = resultFromPlace.getGeometry().getLocation().getLng();
       if(resultFromPlace.getPhotos()!=null && !resultFromPlace.getPhotos().isEmpty())
       this.photoReference = resultFromPlace.getPhotos().get(0).getPhotoReference();
       if(resultFromPlace.getOpeningHours()!=null)
       {
           if(resultFromPlace.getOpeningHours().getOpenNow()!=null)
           {
               this.openNow=resultFromPlace.getOpeningHours().getOpenNow();
           }
       }

   }
    public Restaurant(PlaceModel resultFromPlace) {
        this.restaurantId = resultFromPlace.getPlaceId();
        this.restaurantName = resultFromPlace.getName();
        this.address = resultFromPlace.getVicinity();
        this.latitude = resultFromPlace.getGeometry().getLocation().getLat();
        this.longitude = resultFromPlace.getGeometry().getLocation().getLng();
        if(resultFromPlace.getPhotos()!=null && !resultFromPlace.getPhotos().isEmpty())
            this.photoReference = resultFromPlace.getPhotos().get(0).getPhotoReference();
        if(resultFromPlace.getOpeningHours()!=null)
        {
            if(resultFromPlace.getOpeningHours().getOpenNow()!=null)
            {
                this.openNow=resultFromPlace.getOpeningHours().getOpenNow();
            }
        }

    }


    protected Restaurant(Parcel in) {
        photoReference = in.readString();
        restaurantId = in.readString();
        restaurantName = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distanceUser = in.readDouble();
        joiningNumber = in.readInt();
        photoReference = in.readString();
        byte tmpOpenNow = in.readByte();
        openNow = tmpOpenNow == 0 ? null : tmpOpenNow == 1;
        favoriteUserList = in.createStringArrayList();
        phone = in.readString();
        website = in.readString();

    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(restaurantId);
        dest.writeString(restaurantName);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(distanceUser);
        dest.writeInt(joiningNumber);
        dest.writeString(photoReference);
        dest.writeByte((byte) (openNow == null ? 0 : openNow ? 1 : 2));
        dest.writeStringList(favoriteUserList);
        dest.writeString(phone);
        dest.writeString(website);
    }
}
