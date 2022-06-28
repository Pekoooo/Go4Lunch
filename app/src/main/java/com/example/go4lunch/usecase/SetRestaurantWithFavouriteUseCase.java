package com.example.go4lunch.usecase;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;

import java.util.List;

public class SetRestaurantWithFavouriteUseCase {

    public static List<Restaurant> invoke(List<User> userList, List<Restaurant> restaurantList) {

        for (int i = 0; i < restaurantList.size(); i++) {
            Restaurant currentRestaurant = restaurantList.get(i);

            for (int j = 0; j < userList.size(); j++) {
                User currentUser = userList.get(j);
                if(currentUser.getRestaurantChoiceId() != null){
                    if (currentUser.getRestaurantChoiceId().equals(currentRestaurant.getPlaceId())) {
                        currentRestaurant.setFavourite(true);
                        break;
                    } else {
                        currentRestaurant.setFavourite(false);
                    }
                }
            }
        }
        return restaurantList;
    }
}
