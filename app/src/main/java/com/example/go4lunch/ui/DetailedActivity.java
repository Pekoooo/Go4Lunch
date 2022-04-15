package com.example.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.AppModel.Restaurant;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.viewmodel.ViewModelDetailedView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailedActivity extends AppCompatActivity {

    private static final String TAG = "MyDetailedActivity";
    private PlaceModel placeDetailResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: is called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        ViewModelDetailedView viewModel = new ViewModelProvider(this).get(ViewModelDetailedView.class);
        //viewModel.init();

        if(getIntent().hasExtra("placeDetails")){
            Log.d(TAG, "onCreate: receiving place id from list fragment");
            String placeId  = getIntent().getStringExtra("placeDetails");

            viewModel.searchPlaceDetail(placeId);

            viewModel.getPlaceDetails().observe(this, placeDetailResponseModel -> {

                Log.d(TAG, "onChanged: place detail displayed");
                placeDetailResult = placeDetailResponseModel.getResult();

                Log.d(TAG, "onCreate: " + placeDetailResult.getWebsite());
                setDetails();

            });
        }
    }

    private void setDetails() {

        TextView restaurantName = findViewById(R.id.cardview_restaurant_name);
        TextView restaurantAddress = findViewById(R.id.cardview_restaurant_address);
        ImageView restaurantImage = findViewById(R.id.restaurant_detailed_image_view);
        FloatingActionButton restaurantFavButton = findViewById(R.id.restaurant_detailed_favourite_FAB);

        restaurantName.setText(placeDetailResult.getName());
        restaurantAddress.setText(placeDetailResult.getVicinity());

        String PlacePhotoApiCall = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                + placeDetailResult.getPhotos().get(0).getPhotoReference()
                + BuildConfig.API_KEY;

        Glide.with(restaurantImage.getContext())
                .load(PlacePhotoApiCall)
                .into(restaurantImage);

    }
}