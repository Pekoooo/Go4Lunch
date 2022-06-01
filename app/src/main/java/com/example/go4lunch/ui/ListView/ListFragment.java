package com.example.go4lunch.ui.ListView;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentListRestaurantBinding;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.ui.DetailedView.DetailedActivity;
import com.example.go4lunch.ui.MapView.MapFragment;
import com.example.go4lunch.utils.GetBoundsUtil;
import com.example.go4lunch.viewmodel.ViewModelRestaurant;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;


public class ListFragment extends Fragment implements RestaurantRecyclerViewAdapter.OnItemClickListener {

    private static final String TAG = "MyListFragment";
    private ViewModelRestaurant viewModel;
    private RestaurantRecyclerViewAdapter adapter;
    private FragmentListRestaurantBinding binding;
    private Location currentLocation;
    private List<PlaceModel> places = new ArrayList<>();
    private List<PlaceModel> selectedPlace = new ArrayList<>();
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private final List<Place.Field> fields = Arrays.asList(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ID);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelRestaurant.class);
        //viewModel.init();
        Places.initialize(requireContext(), BuildConfig.API_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListRestaurantBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getListOfRestaurants().observe(requireActivity(), nearbyResponseModel -> {
            if (nearbyResponseModel != null) {
                places = nearbyResponseModel.getResults();

            }
        });

        viewModel.location.observe(requireActivity(), location -> {
            currentLocation = location;
        });

        searchRestaurants();
    }

    private void searchRestaurants() {
        Location location = viewModel.location.getValue();
        String latlng = null;

        if(currentLocation == null && location != null) {
             latlng = location.getLatitude() + "," + location.getLongitude();
        } else if (currentLocation != null){
             latlng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        }


        viewModel.searchRestaurants(latlng);
        setRecyclerView();
        adapter.setRestaurants(places);
    }

    public void setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestaurantRecyclerViewAdapter(places, currentLocation, this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClick(int position) {
        String placeId = places.get(position).getPlaceId();
        Intent intent = new Intent(requireContext(), DetailedActivity.class);
        intent.putExtra("placeDetails", placeId);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        double radius = 2.0;
        double[] boundsFromLatLng = GetBoundsUtil.getBoundsFromLatLng(radius, currentLocation.getLatitude(), currentLocation.getLongitude());
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setLocationRestriction(RectangularBounds.newInstance(
                        new LatLng(boundsFromLatLng[0], boundsFromLatLng[1]),
                        new LatLng(boundsFromLatLng[2], boundsFromLatLng[3])
                ))
                .build(ListFragment.this.requireContext());
        ListFragment.this.startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String placeID = place.getId();

                adapter.setRestaurants(getPlaceFromList(placeID));

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(requireContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<PlaceModel> getPlaceFromList(String placeID) {
        selectedPlace.clear();
        for (int i = 0; i < places.size(); i++) {
            PlaceModel currentPlace = places.get(i);
            if(currentPlace.getPlaceId().equals(placeID)){
                selectedPlace.add(currentPlace);
            }
        }
        if(selectedPlace.isEmpty()){
            Toast.makeText(requireContext(), "could not find place nearby", Toast.LENGTH_SHORT).show();
            selectedPlace = places;
        }
        return selectedPlace;
    }
}




