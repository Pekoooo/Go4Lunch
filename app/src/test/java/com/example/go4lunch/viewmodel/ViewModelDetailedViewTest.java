package com.example.go4lunch.viewmodel;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.PlaceDetailRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.utils.GetDummies;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Application;

import java.util.List;

public class ViewModelDetailedViewTest {

    private ViewModelDetailedView viewModel;

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    //MOCK
    private final Application application = Mockito.mock(Application.class);
    private final PlaceDetailRepository placeDetailRepository = Mockito.mock(PlaceDetailRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    //DATA TO BE RETURNED
    private final MutableLiveData<Restaurant> placeDetail = new MutableLiveData<>();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();


    @Before
    public void setUp() {
        //RETURNS OF MOCK
        Mockito.doReturn(placeDetail)
                .when(placeDetailRepository)
                .getPlaceDetails();

        Mockito.doReturn(users)
                .when(userRepository)
                .getCoworkersComing();

        placeDetail.setValue(GetDummies.getDummyRestaurant());
        users.setValue(GetDummies.getDummyUserList());

        viewModel = new ViewModelDetailedView(userRepository, placeDetailRepository, application);

    }

    @Test
    public void viewModel_should_return_dummy_place() {
        //WHEN
        LiveDataTestUtil.observeForTesting(viewModel.getPlaceDetails(), new LiveDataTestUtil.OnObservedListener<Restaurant>() {
            @Override
            public void onObserved(Restaurant liveData) {

                //THEN
                assertEquals(placeDetail.getValue(), liveData);
                verify(placeDetailRepository, times(1)).getPlaceDetails();
            }
        });
    }

    @Test
    public void viewModel_should_return_coworker_list() {
        LiveDataTestUtil.observeForTesting(viewModel.getCoworkers(), new LiveDataTestUtil.OnObservedListener<List<User>>() {
            @Override
            public void onObserved(List<User> liveData) {

                //THEN
                assertEquals(users.getValue(), liveData);
                verify(userRepository, times(1)).getCoworkersComing();

            }
        });
    }
}