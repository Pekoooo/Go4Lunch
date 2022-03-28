package com.example.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final int RC_SIGN_IN = 1606;
    private static final int ERROR_DIALOG_REQUEST = 1989;
    private DrawerLayout drawer;
    private static final String TAG = "MyMainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initUi();
        getLocationPermission();
        //startSignInActivity();
    }

    @AfterPermissionGranted(LOCATION_PERMISSION_REQUEST_CODE)
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: is called");
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initFragments();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale), LOCATION_PERMISSION_REQUEST_CODE, perms);
        }
    }

 //   private void startSignInActivity() {
 //       // Choose authentication providers
 //       List<AuthUI.IdpConfig> providers = Arrays.asList(
 //               new AuthUI.IdpConfig.GoogleBuilder().build(),
 //               new AuthUI.IdpConfig.EmailBuilder().build());
//
 //       // Launch the activity
 //       startActivityForResult(
 //               AuthUI.getInstance()
 //                       .createSignInIntentBuilder()
 //                       .setTheme(R.style.LoginTheme)
 //                       .setAvailableProviders(providers)
 //                       .setIsSmartLockEnabled(false, true)
 //                       .setLogo(R.drawable.go4lunch_ic_sign)
 //                       .build(),
 //               RC_SIGN_IN);
 //   }

    private void initFragments() {
        Log.d(TAG, "initFragments: Initializing the fragments");
        //initFragments fragment
        Fragment fragment = new Fragment();

        //Open Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    private void initUi() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        Log.d(TAG, "initUi: Initializing main activity UI");
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Toggle the icon to open and close the drawer menu
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_your_lunch:
                Intent intent = new Intent(MainActivity.this, YourLunchActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                //TODO IMPLEMENT LOGOUT CODE HERE
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: getting permissions result");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initFragments();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionDenied(this, perms.get(0))) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale), LOCATION_PERMISSION_REQUEST_CODE, perms.get(0));

        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            initFragments();
            Toast.makeText(this, "Current Location", Toast.LENGTH_SHORT).show();
        }
    }
}