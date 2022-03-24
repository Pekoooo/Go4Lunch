package com.example.go4lunch.model;

import com.google.gson.annotations.SerializedName;





public class Restaurant {


    @SerializedName("name")
    private String restaurantName;

    @SerializedName("place_id")
    private String restaurantId;

    //TODO : Where to get the address ?
   // @SerializedName("place_id")
   // private String restaurantAddress;





}
