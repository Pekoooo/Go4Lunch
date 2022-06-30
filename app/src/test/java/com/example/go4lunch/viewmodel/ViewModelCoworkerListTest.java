package com.example.go4lunch.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.utils.GetDummies;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

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

        users.setValue(GetDummies.getDummyUserList());

        viewModel = new ViewModelCoworkerList(application, userRepository);

    }

    @Test
    public void viewModel_should_return_all_coworkers() {
        LiveDataTestUtil.observeForTesting(viewModel.getAllCoworkers(), liveData -> {

            assertEquals(users.getValue(), liveData);
            verify(userRepository, times(1)).getAllCoworkers();
        });

    }

}