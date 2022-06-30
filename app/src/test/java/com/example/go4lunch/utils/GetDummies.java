package com.example.go4lunch.utils;

import android.provider.Contacts;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.model.GooglePlacesModel.Photo;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;

import java.util.ArrayList;
import java.util.List;

public class GetDummies {


    public static List<User> getDummyUserList() {

        List<User> dummyUserList = new ArrayList<>();
        dummyUserList.add(new User("uid0", "name0", "avatarUrl0", "email0"));
        dummyUserList.add(new User("uid1", "name1", "avatarUrl1", "email1"));
        dummyUserList.add(new User("uid2", "name2", "avatarUrl2", "email2"));

        User user = dummyUserList.get(0);
        user.setRestaurantChoiceId("123");

        User user2 = dummyUserList.get(1);
        user2.setRestaurantChoiceId("1234");

        return dummyUserList;
    }

    public static List<Restaurant> getDummyRestaurantList() {
        List<Restaurant> dummyRestaurants = new ArrayList<>();
        dummyRestaurants.add(new Restaurant("name", "address", "photoRef","placeId", false, 1, "phoneNumber", "Website"));
        dummyRestaurants.add(new Restaurant("name2", "address2", "photoRef2","1234", false, 2, "phoneNumber2", "Website2"));
        dummyRestaurants.add(new Restaurant("name3", "address3", "photoRef3","123", false, 3, "phoneNumber3", "Website3"));

        return dummyRestaurants;
    }


    public static Restaurant getDummyRestaurant() {
        return new Restaurant(
                "Name",
                "Address",
                "PhotoRef",
                "PlaceId",
                false,
                4,
                "PhoneNumber",
                "Website"
        );
    }

    public static PlaceModel getDummyPlaceModel(){

        PlaceModel placemodel = new PlaceModel(
                "Name",
                "PlaceId");

        placemodel.setAddress("address");
        placemodel.setPhotos(null);
        placemodel.getOpeningHours().setOpenNow(false);
        placemodel.setRating(4);
        placemodel.setPhone("phone number");
        placemodel.setWebsite("website");

       return placemodel;
    }
}
