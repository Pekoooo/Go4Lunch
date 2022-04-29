package com.example.go4lunch.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailedBinding;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.usecase.GetCurrentUserFromDBUseCase;
import com.example.go4lunch.viewmodel.ViewModelDetailedView;

public class DetailedActivity extends AppCompatActivity {

    private static final String TAG = "MyDetailedActivity";
    private User currentUser;
    private PlaceModel placeDetailResult;
    private ActivityDetailedBinding binding;
    private ViewModelDetailedView viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: is called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ViewModelDetailedView.class);

        if (getIntent().hasExtra("placeDetails")) {
            String placeId = getIntent().getStringExtra("placeDetails");
            viewModel.searchPlaceDetail(placeId);
            viewModel.getPlaceDetails().observe(this, placeDetailResponseModel -> {
                placeDetailResult = placeDetailResponseModel.getResult();
                getUserData();
            });
        }
    }

    private void getUserData() {

        viewModel.getUserData().addOnSuccessListener(user -> {
            currentUser = new User(
                    user.getUid(),
                    user.getUserName(),
                    user.getAvatarURL(),
                    user.getEmail()
            );

            currentUser.setRestaurantChoiceId(user.getRestaurantChoiceId());

            setDetails();
            setButtonLogic();

        });

    }

    public void setButtonLogic() {
        setButtons();
        //TODO TURN IF STATEMENT INTO TERNARY CONDITION
        binding.restaurantDetailedFavouriteFAB.setOnClickListener(v -> {

            if (getCurrentUser().getRestaurantChoiceId() == null) {
                //NO CHOICE MADE BY USER ? CLICK = SELECT CURRENT RESTAURANT
                viewModel.updateUserRestaurantChoice(placeDetailResult.getPlaceId(), getCurrentUser());
                animateChecked();

            } else if (getCurrentUser().getRestaurantChoiceId() != null) {
                //USER MADE CHOICE ?
                //YES →
                if (getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
                    // IS IT THE SAME ONE THAT WE ARE WATCHING ?
                    // YES ? →
                    viewModel.updateUserRestaurantChoice(null, getCurrentUser());
                    animateUnchecked();


                } else if (!getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
                    //NO ? →
                    Log.d(TAG, "onClick: Choice not null and choice is not the current restaurant, replacing old choice with new choice");
                    viewModel.updateUserRestaurantChoice(placeDetailResult.getPlaceId(), getCurrentUser());
                    animateChecked();

                }
            }
        });
    }



    private void setButtons() {
        //TODO TURN IF STATEMENT INTO TERNARY CONDITION
        if (getCurrentUser().getRestaurantChoiceId() == null || !getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
            binding.restaurantDetailedFavouriteFAB.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);


        } else if (getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
            binding.restaurantDetailedFavouriteFAB.setImageResource(R.drawable.ic_check_circle);
        }
    }

    public User getCurrentUser() {
        GetCurrentUserFromDBUseCase.invoke().addOnCompleteListener(task -> currentUser = task.getResult());
        return currentUser;
    }

    private void setDetails() {

        binding.cardviewRestaurantName.setText(placeDetailResult.getName());
        binding.cardviewRestaurantAddress.setText(placeDetailResult.getVicinity());

        String PlacePhotoApiCall = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                + placeDetailResult.getPhotos().get(0).getPhotoReference()
                + "&key="
                + BuildConfig.API_KEY;

        Log.d(TAG, "setDetails: photo :" + PlacePhotoApiCall);

        Glide.with(binding.restaurantDetailedImageView.getContext())
                .load(PlacePhotoApiCall)
                .into(binding.restaurantDetailedImageView);
    }


    /**
     * ROTATION ANIMATION OF DETAILED VIEW FLOATING ACTION BUTTON.
     */
    private void animateChecked() {
        binding.restaurantDetailedFavouriteFAB.animate()
                .rotationBy(360)
                .setDuration(250)
                .withEndAction(() -> binding.restaurantDetailedFavouriteFAB.setImageResource(R.drawable.ic_check_circle));
    }

    private void animateUnchecked() {
        binding.restaurantDetailedFavouriteFAB.animate()
                .rotationBy(360)
                .setDuration(250)
                .withEndAction(() -> binding.restaurantDetailedFavouriteFAB.setImageResource(R.drawable.ic_baseline_check_circle_outline_24));
    }
}