package com.example.go4lunch.viewmodel;

import static org.junit.Assert.*;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.repositories.MainRepository;
import com.example.go4lunch.repositories.PlaceDetailRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.utils.LiveDataTestUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
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

        placeDetail.setValue(getDummyPlace());
        users.setValue(getDummyCoworkers());

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

    private Restaurant getDummyPlace() {
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

    private List<User> getDummyCoworkers() {

        List<User> dummyCoworkersList = new ArrayList<>();
        dummyCoworkersList.add(new User("uid0", "name0", "avatarUrl0", "email0"));
        dummyCoworkersList.add(new User("uid1", "name1", "avatarUrl1", "email1"));
        dummyCoworkersList.add(new User("uid2", "name2", "avatarUrl2", "email2"));

        return dummyCoworkersList;
    }


}