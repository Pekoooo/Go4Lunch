package com.example.go4lunch.model.GooglePlacesModel;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleResponseModel {

    @SerializedName("results")
    @Expose
    private List<Restaurant> results = null;

    @SerializedName("error_message")
    @Expose
    private String error;

    public String getError() {
        return error;
    }

    public List<Restaurant> getResults() {
        return results;
    }

    public void setResults(List<Restaurant> results) {
        this.results = results;
    }


}
