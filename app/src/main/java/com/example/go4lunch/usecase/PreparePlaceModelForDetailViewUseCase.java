package com.example.go4lunch.usecase;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;

public class PreparePlaceModelForDetailViewUseCase {

    public static Restaurant invoke(PlaceModel placeModel) {

        String name = placeModel.getName();
        String address = placeModel.getVicinity();
        String placeId = placeModel.getPlaceId();

        String photoReference;
        if (placeModel.getPhotos() == null || placeModel.getPhotos().isEmpty()) {
            photoReference = null;
        } else {
            photoReference = placeModel.getPhotos().get(0).getPhotoReference();
        }

        boolean isOpen;
        if (placeModel.getOpeningHours() == null) {
            isOpen = false;
        } else {
            isOpen = placeModel.getOpeningHours().getOpenNow();
        }

        float rating;
        if (placeModel.getRating() == null) {
            rating = 3.5f;
        } else {
            rating = placeModel.getRating();
        }

        String phoneNumber;
        if(placeModel.getPhone() == null){
            phoneNumber = "No Phone Number";
        } else {
            phoneNumber = placeModel.getPhone();
        }

        String website;
        if(placeModel.getWebsite() == null){
            website = "No Website";
        } else {
            website = placeModel.getWebsite();
        }

        return new Restaurant(
                name,
                address,
                photoReference,
                placeId,
                isOpen,
                rating,
                phoneNumber,
                website);
    }
}
