package com.example.go4lunch.usecase;

import android.location.Location;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.repositories.UserRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PrepareListPlaceModelForViewUseCase {

    public UserRepository userRepository;

    public PrepareListPlaceModelForViewUseCase(){
        userRepository = UserRepository.getInstance();
    }

    public static List<Restaurant> invoke(List<PlaceModel> placeModelList, Location currentLocation) {
        List<Restaurant> listToDisplay = new ArrayList<>();

        for (int i = 0; i < placeModelList.size(); i++) {

            PlaceModel currentPlace = placeModelList.get(i);

            String name = currentPlace.getName();
            String address = currentPlace.getVicinity();

            float distance = getDistance(currentPlace, currentLocation);
            String placeId = currentPlace.getPlaceId();

            LatLng latLng = new LatLng(
                    currentPlace.getGeometry().getLocation().getLat(),
                    currentPlace.getGeometry().getLocation().getLng());

            String photoReference;
            if (currentPlace.getPhotos() == null || currentPlace.getPhotos().isEmpty()) {
                photoReference = null;
            } else {
                photoReference = currentPlace.getPhotos().get(0).getPhotoReference();
            }

            float rating;
            if (currentPlace.getRating() == null) {
                rating = 3.5f;
            } else {
                rating = currentPlace.getRating();
            }

            boolean isOpen;
            if (currentPlace.getOpeningHours() == null) {
                isOpen = false;
            } else {
                isOpen = currentPlace.getOpeningHours().getOpenNow();
            }

            GetNumberOfParticipantsUseCase useCase = new GetNumberOfParticipantsUseCase(placeId);

            int participants = useCase.invoke();

            Restaurant restaurantToCreate =
                    new Restaurant(
                    name,
                    address,
                    photoReference,
                    placeId,
                    isOpen,
                    rating,
                    distance,
                    latLng,
                    participants);

            listToDisplay.add(restaurantToCreate);
        }
        return listToDisplay;
    }

    private static float getDistance(PlaceModel currentPlace, Location currentLocation) {
        float distance;

        double currentRestaurantLat = currentPlace.getGeometry().getLocation().getLat();
        double currentRestaurantLng = currentPlace.getGeometry().getLocation().getLng();

        Location currentRestaurantLocation = new Location("Current PlaceModel");
        currentRestaurantLocation.setLatitude(currentRestaurantLat);
        currentRestaurantLocation.setLongitude(currentRestaurantLng);

        distance = currentLocation.distanceTo(currentRestaurantLocation);

        return distance;
    }
}
