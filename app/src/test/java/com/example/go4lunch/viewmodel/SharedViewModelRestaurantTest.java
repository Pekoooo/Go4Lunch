package com.example.go4lunch.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import com.example.go4lunch.utils.CheckUtils;
import com.example.go4lunch.utils.GetDummies;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class SharedViewModelRestaurantTest {

    private static final int EXPECTED_RESTAURANT_AMOUNT = 2;

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final RestaurantRepository mRestaurantRepository = Mockito.mock(RestaurantRepository.class);
    private final Location goodLocation = Mockito.mock(Location.class);
    private final Location badLocation = Mockito.mock(Location.class);
    private final Application application = Mockito.mock(Application.class);

    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    private SharedViewModelRestaurant viewModel;


    @Before
    public void setUp() {
        Mockito.doReturn(locationLiveData)
                .when(mRestaurantRepository)
                .getLocation();

        viewModel = new SharedViewModelRestaurant(application, mRestaurantRepository, userRepository);

        locationLiveData.setValue(goodLocation);
    }

    @Test
    public void viewModel_should_return_location() {

        LiveDataTestUtil.observeForTesting(viewModel.getLocation(), liveData -> {
            assertEquals(goodLocation, liveData);

            verify(mRestaurantRepository, times(1)).getLocation();
        });
    }

    @Test
    public void viewModel_should_return_different_location() {

        LiveDataTestUtil.observeForTesting(viewModel.getLocation(), liveData -> {
            assertNotEquals(badLocation, liveData);
            verify(mRestaurantRepository, times(1)).getLocation();
        });
    }

    @Test
    public void viewModel_should_return_restaurants_with_favourite(){
        
        List<User> userList = GetDummies.getDummyUserList();
        List<Restaurant> restaurantList = GetDummies.getDummyRestaurantList();
        List<Restaurant> restaurantWithFavourite = viewModel.setRestaurantWithFavourite(userList, restaurantList);

        assertEquals(EXPECTED_RESTAURANT_AMOUNT, CheckUtils.checkForFavourite(restaurantWithFavourite));
    }


}