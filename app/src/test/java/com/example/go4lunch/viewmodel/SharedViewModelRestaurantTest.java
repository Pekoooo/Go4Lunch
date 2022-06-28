package com.example.go4lunch.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.RestaurantRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.utils.GetDummies;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModelRestaurantTest {

    private static final int EXPECTED_RESTAURANT_AMOUNT = 2;

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final RestaurantRepository mRestaurantRepository = Mockito.mock(RestaurantRepository.class);
    private final Location location = Mockito.mock(Location.class);
    private final Application application = Mockito.mock(Application.class);

    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    private SharedViewModelRestaurant viewModel;


    @Before
    public void setUp() {
        Mockito.doReturn(locationLiveData)
                .when(mRestaurantRepository)
                .getLocation();

        viewModel = new SharedViewModelRestaurant(application, mRestaurantRepository, userRepository);

        locationLiveData.setValue(location);
    }

    @Test
    public void viewModel_should_return_location() {

        LiveDataTestUtil.observeForTesting(viewModel.getLocation(), liveData -> {
            assertEquals(locationLiveData.getValue(), liveData);

            verify(mRestaurantRepository, times(1)).getLocation();
        });
    }

    @Test
    public void viewModel_should_return_restaurants_with_favourite(){
        
        List<User> userList = GetDummies.getDummyUserList();
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