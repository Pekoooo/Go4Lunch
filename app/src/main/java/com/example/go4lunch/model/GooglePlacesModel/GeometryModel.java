package com.example.go4lunch.model.GooglePlacesModel;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryModel {

    /**
     * return the location of a place
     */


    @SerializedName("location")
    @Expose
    private LocationModel location;

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }
}
