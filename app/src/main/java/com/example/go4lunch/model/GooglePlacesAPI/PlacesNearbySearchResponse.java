package com.example.go4lunch.model.GooglePlacesAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesNearbySearchResponse {

    @SerializedName("results")
    @Expose
    private List<Place> results = null;

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }
}
