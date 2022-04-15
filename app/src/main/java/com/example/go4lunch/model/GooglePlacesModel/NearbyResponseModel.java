package com.example.go4lunch.model.GooglePlacesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyResponseModel {

    @SerializedName("results")
    @Expose
    private List<PlaceModel> results = null;

    @SerializedName("error_message")
    @Expose
    private String error;

    public String getError() {
        return error;
    }

    public List<PlaceModel> getResults() {
        return results;
    }

    public void setResults(List<PlaceModel> results) {
        this.results = results;
    }


}
