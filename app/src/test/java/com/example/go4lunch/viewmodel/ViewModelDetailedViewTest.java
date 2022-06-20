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
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ViewModelDetailedViewTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final PlaceDetailRepository placeRepository = Mockito.mock(PlaceDetailRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final Application application = Mockito.mock(Application.class);

    private final MutableLiveData<PlaceModel> getPlaceDetails = new MutableLiveData<>();

    ViewModelDetailedView viewModel;


    @Before
    public void setUp() throws Exception {

        Mockito.doReturn(getPlaceDetails)
                .when(placeRepository)
                .getPlaceDetails();
        

        getPlaceDetails.setValue(getPlaceDetailsInfo());

        viewModel = new ViewModelDetailedView(application);

    }





    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_test() {

        LiveDataTestUtil.observeForTesting(viewModel.getPlaceDetails(), new LiveDataTestUtil.OnObservedListener<PlaceModel>() {
            @Override
            public void onObserved(PlaceModel detail) {
                assertEquals(getPlaceDetailsInfo(), detail);
            }
        });

    }


    private PlaceModel getPlaceDetailsInfo() {
        return new PlaceModel("L'Orient Palace", "1");
    }


}