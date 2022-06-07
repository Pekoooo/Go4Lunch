package com.example.go4lunch.model.AppModel;

import android.location.Location;

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




    public Restaurant(String name, String address, @Nullable String photoReference, String placeId, boolean isOpen, float rating, float distance, LatLng latLng){
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.placeId = placeId;
        this.isOpen = isOpen;
        this.rating = rating;
        this.distance = distance;
        this.latLng = latLng;

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

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Nullable
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
}
