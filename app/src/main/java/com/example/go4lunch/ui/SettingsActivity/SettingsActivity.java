package com.example.go4lunch.ui.SettingsActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.ui.MainActivity;
import com.example.go4lunch.usecase.GetCurrentUserFromAuthUseCase;
import com.example.go4lunch.viewmodel.SettingsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    private final static String TAG = "MySettingActivity";
    SettingsViewModel viewModel;
    ActivitySettingsBinding binding;
    FirebaseUser userAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userAuth = GetCurrentUserFromAuthUseCase.invoke();

        binding.buttonDeleteAccount.setOnClickListener(v -> {
            String userUid = userAuth.getUid();
            userAuth.delete().addOnCompleteListener(task -> {

                viewModel.deleteUser(userUid);
                Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);


            });
        });



    }
}