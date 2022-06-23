package com.example.go4lunch.viewmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.model.GooglePlacesModel.LocationModel;
import com.example.go4lunch.repositories.MainRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SharedViewModelRestaurantTest {

    private static final double EXPECTED_LATITUDE = 37.0;
    private static final double EXPECTED_LONGITUDE = 42.0;
    private static final int EXPECTED_RESTAURANT_AMOUNT = 2;

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final MainRepository mainRepository = Mockito.mock(MainRepository.class);
    private Location location = Mockito.mock(Location.class);
    private final Application application = Mockito.mock(Application.class);

    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    private SharedViewModelRestaurant viewModel;


    @Before
    public void setUp() throws Exception {
        Mockito.doReturn(EXPECTED_LATITUDE)
                .when(location)
                .getLatitude();

        Mockito.doReturn(EXPECTED_LONGITUDE)
                .when(location)
                .getLongitude();


        Mockito.doReturn(locationLiveData)
                .when(mainRepository)
                .getLocation();

        viewModel = new SharedViewModelRestaurant(application, mainRepository, userRepository);

        locationLiveData.setValue(location);
    }

    @Test
    public void viewModel_should_return_location() {

        LiveDataTestUtil.observeForTesting(viewModel.getLocation(), new LiveDataTestUtil.OnObservedListener<Location>() {
            @Override
            public void onObserved(Location liveData) {
                assertEquals(locationLiveData.getValue(), liveData);
            }
        });
    }

    @Test
    public void viewModel_should_return_restaurants_with_favourite(){
        
        List<User> userList = getDummyUserList();
        List<Restaurant> restaurantList = getDummyRestaurantList();
        
        List<Restaurant> restaurantWithFavourite = viewModel.setRestaurantWithFavourite(userList, restaurantList);

        assertEquals(EXPECTED_RESTAURANT_AMOUNT, checkForFavourite(restaurantWithFavourite));
    }


    private List<Restaurant> getDummyRestaurantList() {
        List<Restaurant> dummyRestaurants = new ArrayList<>();
        dummyRestaurants.add(new Restaurant("name", "address", "photoRef","placeId", false, 1, "phoneNumber", "Website"));
        dummyRestaurants.add(new Restaurant("name2", "address2", "photoRef2","1234", false, 2, "phoneNumber2", "Website2"));
        dummyRestaurants.add(new Restaurant("name3", "address3", "photoRef3","123", false, 3, "phoneNumber3", "Website3"));

        return dummyRestaurants;
    }

    private List<User> getDummyUserList() {

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

    private int checkForFavourite(List<Restaurant> restaurantWithFavourite) {
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