package com.example.go4lunch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.ui.DetailedView.DetailedActivity;
import com.example.go4lunch.ui.SettingsActivity.SettingsActivity;
import com.example.go4lunch.usecase.GetCurrentUserFromDBUseCase;
import com.example.go4lunch.viewmodel.ViewModelUser;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 1606;
    private static final int ERROR_DIALOG_REQUEST = 1989;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    static User currentUser = new User();
    private ViewModelUser viewModelUser;
    private DrawerLayout drawer;
    private static final String TAG = "MyMainActivity";
    private ActivityMainBinding binding;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView drawerUserName;
    private TextView drawerUserEmail;
    private ImageView drawerUserPicture;
    private NavController navController;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        initViewModel();
        bindViewHeader();
        setContentView(binding.getRoot());
        initUi();

        // CHECK FOR LOGIN
        if (!viewModelUser.isCurrentUserLogged()) {
            startSignInActivity(signInLauncher);
        } else {
            getCurrentUserData();
        }


    }

    public void initViewModel() {
        viewModelUser = new ViewModelProvider(this).get(ViewModelUser.class);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {

        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == Activity.RESULT_OK) {

            //Create auth user in Firestore db
            viewModelUser.createUser();
            Toast.makeText(this, "Signed in successfully !", Toast.LENGTH_SHORT).show();

            //Get user info from db and update UI
            getCurrentUserData();

        } else {
            if (response == null)
                startSignInActivity(signInLauncher);
        }
    }

    private void startSignInActivity(ActivityResultLauncher<Intent> signInLauncher) {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers =
                Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                        new AuthUI.IdpConfig.TwitterBuilder().build());

        // Launch the activity
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false, true)
                .setLogo(R.drawable.go4lunch_ic_sign)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void getCurrentUserData() {
        GetCurrentUserFromDBUseCase.invoke().addOnSuccessListener(user -> {
            drawerUserName = MainActivity.this.findViewById(R.id.drawer_profile_name);
            drawerUserEmail = MainActivity.this.findViewById(R.id.drawer_profile_email);
            drawerUserPicture = MainActivity.this.findViewById(R.id.drawer_profile_picture);

            currentUser = user;

            if (user != null) {
                MainActivity.this.updateDrawerUi(user);

            }

        });
    }

    public void updateDrawerUi(User user) {
        drawerUserName.setText(user.getUserName());
        if (user.getEmail() != null) {
            drawerUserEmail.setText(user.getEmail());
        } else {
            drawerUserEmail.setText(R.string.no_email);
        }

        if (user.getAvatarURL() != null) {
            Glide.with(drawerUserPicture.getContext())
                    .load(user.getAvatarURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(drawerUserPicture);
        }
    }

    private void bindViewHeader() {
        View parentView = binding.navView.getHeaderView(0);

        drawerUserName = parentView.findViewById(R.id.drawer_profile_name);
        drawerUserEmail = parentView.findViewById(R.id.drawer_profile_email);
        drawerUserPicture = parentView.findViewById(R.id.drawer_profile_picture);
    }

    private void initUi() {
        navController = Navigation.findNavController(this, R.id.fragment);
        setNavController();

        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        setSupportActionBar(binding.toolbar);
        binding.navView.setNavigationItemSelectedListener(this);

        //Toggle the icon to open and close the drawer menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNavController() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.mapFragment: {
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.toolbar.setTitle(R.string.Im_hungry);
                    break;
                }
                case R.id.listFragment: {
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.toolbar.setTitle(R.string.Restaurants_around);
                    Menu toolbarMenu = binding.toolbar.getMenu();
                    toolbarMenu.clear();
                    break;
                }
                case R.id.coworkersFragment: {
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.toolbar.setTitle(R.string.Available_workmates);
                    break;
                }

                default: {
                    binding.toolbar.setTitle(R.string.Im_hungry);
                    break;
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_your_lunch:
                if (currentUser.getRestaurantChoiceId() != null) {
                    Intent intent = new Intent(this, DetailedActivity.class);
                    intent.putExtra("placeDetails", currentUser.getRestaurantChoiceId());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "You haven't made a choice yet !", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_settings:
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }
        return true;
    }

    private void logOut() {
        firebaseAuth.signOut();
        startSignInActivity(signInLauncher);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUser.getUserName() != null) {
            getCurrentUserData();
        }
    }
}