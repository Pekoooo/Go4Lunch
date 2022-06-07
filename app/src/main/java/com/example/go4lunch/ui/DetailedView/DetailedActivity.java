package com.example.go4lunch.ui.DetailedView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailedBinding;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;
import com.example.go4lunch.usecase.GetCurrentUserFromDBUseCase;
import com.example.go4lunch.viewmodel.ViewModelDetailedView;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    private static final String TAG = "MyDetailedActivity";
    private User currentUser;
    private PlaceModel placeDetailResult;
    private ActivityDetailedBinding binding;
    private ViewModelDetailedView viewModel;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ViewModelDetailedView.class);

        if (getIntent().hasExtra("placeDetails")) {
            placeId = getIntent().getStringExtra("placeDetails");
            viewModel.searchPlaceDetail(placeId);
            viewModel.fetchCoworkersComing(placeId);
            viewModel.getPlaceDetails().observe(this, placeDetailResponseModel -> {
                placeDetailResult = placeDetailResponseModel.getResult();
                getUserData();
            });


            viewModel.getCoworkers().observe(this, this::setRecyclerView);
        }

    }
    
    private void setRecyclerView(List<User> coworkersComing) {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DetailedViewRecyclerViewAdapter adapter = new DetailedViewRecyclerViewAdapter(coworkersComing);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getUserData() {
        GetCurrentUserFromDBUseCase.invoke().addOnSuccessListener(user -> {
            currentUser = new User(
                    user.getUid(),
                    user.getUserName(),
                    user.getAvatarURL(),
                    user.getEmail()
            );

            currentUser.setRestaurantChoiceId(user.getRestaurantChoiceId());
            currentUser.setLikedRestaurants(user.getLikedRestaurants());
            setDetails();
            setButtonLogic();
        });
    }

    public void setButtonLogic() {
        setButtonState();
        //TODO TURN IF STATEMENT INTO TERNARY CONDITION
        binding.restaurantDetailedFavouriteFAB.setOnClickListener(v -> {

            if (getCurrentUser().getRestaurantChoiceId() == null) {
                //NO CHOICE MADE BY USER ? CLICK = SELECT CURRENT RESTAURANT
                viewModel.updateUserRestaurantChoice(placeDetailResult.getPlaceId(), placeDetailResult.getName(), getCurrentUser());
                viewModel.fetchCoworkersComing(placeId);
                animateChecked();

            } else if (getCurrentUser().getRestaurantChoiceId() != null) {
                //USER MADE CHOICE ?
                //YES →
                viewModel.fetchCoworkersComing(placeId);
                if (getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
                    // IS IT THE SAME ONE THAT WE ARE WATCHING ?
                    // YES ? →
                    viewModel.updateUserRestaurantChoice(null, null, getCurrentUser());
                    animateUnchecked();


                } else if (!getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
                    //NO ? →
                    viewModel.updateUserRestaurantChoice(placeDetailResult.getPlaceId(), placeDetailResult.getName(), getCurrentUser());
                    animateChecked();

                }
            }
        });

        binding.cardviewCallButton.setOnClickListener(v -> {
            String restaurantPhoneNumber;
            if (placeDetailResult.getPhone() != null) {
                restaurantPhoneNumber = placeDetailResult.getPhone();
            } else {
                restaurantPhoneNumber = "no phone number";
            }

            if (restaurantPhoneNumber.equals("no phone number")) {
                Toast.makeText(
                        DetailedActivity.this,
                        "No phone number for this establishment",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent intent1 = new Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:" + restaurantPhoneNumber));
                startActivity(intent1);
            }
        });

        binding.cardviewWebsiteButton.setOnClickListener(v -> {
            String restaurantWebsite;

            if (placeDetailResult.getWebsite() != null) {
                restaurantWebsite = placeDetailResult.getWebsite();
            } else {
                restaurantWebsite = null;
            }
            if (restaurantWebsite != null) {
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantWebsite));
                startActivity(intent2);
            } else {
                Toast.makeText(this, "No website for this restaurant", Toast.LENGTH_SHORT).show();
            }
        });

        binding.cardviewLikeButton.setOnClickListener(v -> {
            if(!isFavourite()){
                viewModel.addFavouritePlace(placeId, currentUser);
                setLiked();
            } else {
                viewModel.removeFavouritePlace(placeId, currentUser);
                setNotLiked();
            }
        });
    }

    private void setButtonState() {
        //TODO TURN IF STATEMENT INTO TERNARY CONDITION
        //SETS FAB STATE
        if (getCurrentUser().getRestaurantChoiceId() == null || !getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
            setUnchecked();

        } else if (getCurrentUser().getRestaurantChoiceId().equals(placeDetailResult.getPlaceId())) {
            setChecked();
        }

       // SETS FAVOURITE ICON
       if(isFavourite()){
           setLiked();
       } else {
           setNotLiked();
       }
    }

    private User getCurrentUser() {
        GetCurrentUserFromDBUseCase.invoke().addOnCompleteListener(task -> currentUser = task.getResult());
        return currentUser;
    }

    private void setDetails() {

        float ratingValue;

        binding.cardviewRestaurantName.setText(placeDetailResult.getName());
        binding.cardviewRestaurantAddress.setText(placeDetailResult.getVicinity());
        if (placeDetailResult.getRating() != null) {
            ratingValue = placeDetailResult.getRating();
        } else {
            ratingValue = 3f;
        }

        binding.ratingBar.setRating(ratingValue);

        String placePhotoApiCall;
        if (placeDetailResult.getPhotos() != null) {
            placePhotoApiCall = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                    + placeDetailResult.getPhotos().get(0).getPhotoReference()
                    + "&key="
                    + BuildConfig.API_KEY;
        } else {
            placePhotoApiCall = "https://media.istockphoto.com/photos/variety-food-on-a-table-cloth-restaurant-directly-above-picture-id1235685137?k=20&m=1235685137&s=612x612&w=0&h=ddicngEKM9UbIKflROe3tMj7DnC8VVT6F_m_jct2afs=";
        }

        Glide.with(binding.restaurantDetailedImageView.getContext())
                .load(placePhotoApiCall)
                .into(binding.restaurantDetailedImageView);
    }

    private boolean isFavourite(){
        boolean isFavourite = false;
        if(!currentUser.likedRestaurants.isEmpty()){
            for (int i = 0; i < currentUser.likedRestaurants.size(); i++) {
                String restaurantNameAtPosition = currentUser.likedRestaurants.get(i);
                if(restaurantNameAtPosition.equals(placeDetailResult.getPlaceId())){
                    isFavourite = true;
                    break;
                } else {
                    isFavourite = false;
                }
            }
        }

        return isFavourite;
    }

    private void setNotLiked() {
        Log.d(TAG, "setNotLiked: changing star to empty");
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_border);
        binding.cardviewLikeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, img,null,null);
    }

    private void setLiked() {
        Log.d(TAG, "setLiked: changing star to full");
        Drawable img = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star);
        binding.cardviewLikeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, img,null,null);
    }

    /**
     * ROTATION ANIMATION OF DETAILED VIEW FLOATING ACTION BUTTON.
     */
    private void animateChecked() {
        binding.restaurantDetailedFavouriteFAB.animate()
                .rotationBy(360)
                .setDuration(250)
                .withEndAction(this::setChecked);
    }

    private void animateUnchecked() {
        binding.restaurantDetailedFavouriteFAB.animate()
                .rotationBy(360)
                .setDuration(250)
                .withEndAction(this::setUnchecked);
    }

    private void setUnchecked(){
        binding.restaurantDetailedFavouriteFAB.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);

    }

    private void setChecked(){
        binding.restaurantDetailedFavouriteFAB.setImageResource(R.drawable.ic_check_circle);

    }
}