package com.example.go4lunch.model.AppModel;

import com.google.android.gms.maps.model.LatLng;

import javax.annotation.Nullable;

public class Restaurant {

    private String name;
    private String address;
    @Nullable
    private String photoReference;
    private final String placeId;
    private int coworkersGoing;
    private boolean isOpen;
    private boolean isFavourite;
    private float rating;
    private float distance;
    private LatLng latLng;
    private int participants;
    private String phoneNumber;
    private String website;

    //Constructor for list view
    public Restaurant(String name, String address, @Nullable String photoReference, String placeId, boolean isOpen, float rating, float distance, LatLng latLng, int participants){
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.placeId = placeId;
        this.isOpen = isOpen;
        this.rating = rating;
        this.distance = distance;
        this.latLng = latLng;
        this.participants = participants;

    }

    //Constructor for detailed view
    public Restaurant(String name, String address, @Nullable String photoReference, String placeId, boolean isOpen, float rating, String phoneNumber, String website){
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.placeId = placeId;
        this.isOpen = isOpen;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Nullable
    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(@Nullable String photoReference) {
        this.photoReference = photoReference;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getPlaceId() {
        return placeId;
    }

    public int getCoworkersGoing() {
        return coworkersGoing;
    }

    public void setCoworkersGoing(int coworkersGoing) {
        this.coworkersGoing = coworkersGoing;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
