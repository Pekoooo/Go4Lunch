package com.example.go4lunch.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentListBinding;
import com.example.go4lunch.model.GooglePlacesAPI.Place;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.service.GooglePlacesService;
import com.example.go4lunch.viewmodel.ViewModelRestaurant;

import java.util.List;
import java.util.Map;

import retrofit2.Call;


public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    private List<Place> nearbyList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RestaurantRecyclerViewAdapter adapter = new RestaurantRecyclerViewAdapter(nearbyList);
        recyclerView.setAdapter(adapter);

        getRestaurantsNearby();

        return v;
    }

    private void getRestaurantsNearby() {
        Log.d(TAG, "getRestaurantsNearby: is Called");
       //TODO : Call repository's Place API call and retrieve a list of PLACES and display it
    }

   
}