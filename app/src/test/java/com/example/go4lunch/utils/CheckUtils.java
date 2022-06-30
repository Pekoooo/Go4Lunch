package com.example.go4lunch.utils;

import com.example.go4lunch.model.AppModel.Restaurant;

import java.util.List;

public class CheckUtils {

    public static int checkForFavourite(List<Restaurant> restaurantWithFavourite) {
        int numberOfRestaurantWithFavourite = 0;

        for (int i = 0; i < restaurantWithFavourite.size(); i++) {
            Restaurant currentRestaurant = restaurantWithFavourite.get(i);

            if(currentRestaurant.isFavourite()){
                numberOfRestaurantWithFavourite ++;
            }
        }
        return numberOfRestaurantWithFavourite;
    }
}
