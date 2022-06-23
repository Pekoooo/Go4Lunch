package com.example.go4lunch.viewmodel;

import static org.junit.Assert.*;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.PlaceDetailRepository;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.utils.LiveDataTestUtil;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ViewModelCoworkerListTest {

    private ViewModelCoworkerList viewModel;

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    //MOCK
    private final Application application = Mockito.mock(Application.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    //DATA TO BE RETURNED
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();

    @Before
    public void setUp() {
        Mockito.doReturn(users)
                .when(userRepository)
                .getAllCoworkers();

        users.setValue(getDummyCoworkers());

        viewModel = new ViewModelCoworkerList(application, userRepository);

    }

    @Test
    public void viewModel_should_return_all_coworkers() {
        LiveDataTestUtil.observeForTesting(viewModel.getAllCoworkers(), new LiveDataTestUtil.OnObservedListener<List<User>>() {
            @Override
            public void onObserved(List<User> liveData) {

                assertEquals(users.getValue(), liveData);
                verify(userRepository, times(1)).getAllCoworkers();
            }
        });

    }

    private List<User> getDummyCoworkers() {

        List<User> dummyCoworkersList = new ArrayList<>();
        dummyCoworkersList.add(new User("uid0", "name0", "avatarUrl0", "email0"));
        dummyCoworkersList.add(new User("uid1", "name1", "avatarUrl1", "email1"));
        dummyCoworkersList.add(new User("uid2", "name2", "avatarUrl2", "email2"));

        return dummyCoworkersList;
    }
}