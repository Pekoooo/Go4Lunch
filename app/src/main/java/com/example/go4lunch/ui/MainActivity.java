package com.example.go4lunch.ui;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.ui.DetailedView.DetailedActivity;
import com.example.go4lunch.ui.SettingsActivity.SettingsActivity;
import com.example.go4lunch.usecase.GetCurrentUserFromDBUseCase;
import com.example.go4lunch.viewmodel.MainViewModel;
import com.example.go4lunch.workmanager.WorkerSendNotification;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {

    private static final int RC_SIGN_IN = 1606;
    private static final int ERROR_DIALOG_REQUEST = 1989;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final String CHANNEL_ID = "notification";
    static User currentUser = new User();
    private WorkManager workManager;
    private final Clock clock = Clock.systemDefaultZone();
    private MainViewModel viewModel;
    private static final String TAG = "MyMainActivity";
    private static final String TAG_WORK_MANAGER = "MyWorkManager";
    private ActivityMainBinding binding;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView drawerUserName;
    private TextView drawerUserEmail;
    private ImageView drawerUserPicture;
    private NavController navController;
    private final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        workManager = WorkManager.getInstance(this);

        checkForPerms();
        bindViewHeader();
        initUi();
        createNotificationChannel();
        setNotificationWorker();


        // CHECK FOR LOGIN
        if (!viewModel.isCurrentUserLogged()) {
            startSignInActivity(signInLauncher);
        } else {
            getCurrentUserData();
        }
    }

    private void createNotificationChannel() {
        Log.d(TAG_WORK_MANAGER, "createNotificationChannel: is called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setNotificationWorker() {
        Log.d(TAG_WORK_MANAGER, "setNotificationWorker: is called");
        LocalDateTime currentDate = LocalDateTime.now(clock);
        LocalDateTime thisNoon = currentDate.with(LocalTime.of(12, 0));

        if (currentDate.isAfter(thisNoon)) {
            thisNoon = thisNoon.plusDays(1);
        }
        
        long timeLeft = ChronoUnit
                .MILLIS
                .between(currentDate, thisNoon);
        
        Log.d(TAG_WORK_MANAGER, "setNotificationWorker: " + timeLeft);

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                WorkerSendNotification.class,
                1,
                TimeUnit.DAYS)
                .addTag("TEST")
                .setConstraints(constraints)
                .setInitialDelay(timeLeft, TimeUnit.MILLISECONDS)
                .build();
        
        workManager.enqueueUniquePeriodicWork(
                "TEST",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
        );

        workManager.getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                Log.d(TAG_WORK_MANAGER, "onChanged: " + workInfo.getState());
            }
        });
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

    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            final Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    viewModel.searchRestaurants(task.getResult());
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void checkForPerms() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            Log.d(TAG, "checkForPerms: has perms");
            getDeviceLocation();

        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale),
                    LOCATION_PERMISSION_REQUEST_CODE,
                    perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted: is called");
        getDeviceLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Log.d(TAG, "onPermissionsDenied: showing dialog");
            new AppSettingsDialog.Builder(this).build().show();

        } else {
            Log.d(TAG, "onPermissionsDenied: permission denied for the first time");
            EasyPermissions.requestPermissions(this, getString(R.string.rationale),
                    LOCATION_PERMISSION_REQUEST_CODE, this.perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: are you even called ?");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            checkForPerms();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        Log.d(TAG, "onSignInResult: is called");

        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == Activity.RESULT_OK) {
            Log.d(TAG, "onSignInResult: result ok");

            //Create auth user in Firestore db
            viewModel.createUser();
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
        Log.d(TAG, "getCurrentUserData: is called");
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
        Log.d(TAG, "updateDrawerUi: is called");
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
}