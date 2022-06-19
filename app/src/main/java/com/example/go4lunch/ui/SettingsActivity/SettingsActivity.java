package com.example.go4lunch.ui.SettingsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.model.AppModel.User;
import com.example.go4lunch.ui.DetailedView.DetailedActivity;
import com.example.go4lunch.ui.MainActivity;
import com.example.go4lunch.usecase.GetCurrentUserFromAuthUseCase;
import com.example.go4lunch.usecase.GetCurrentUserFromDBUseCase;
import com.example.go4lunch.viewmodel.SettingsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;

public class SettingsActivity extends AppCompatActivity {
    private final static String TAG = "MySettingActivity";
    SettingsViewModel viewModel;
    ActivitySettingsBinding binding;
    FirebaseUser userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userAuth = GetCurrentUserFromAuthUseCase.invoke();

        binding.buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUid = userAuth.getUid();
                userAuth.delete().addOnCompleteListener(new OnCompleteListener<Void>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        viewModel.deleteUser(userUid);
                        Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intent);


                    }
                });
            }
        });



    }
}