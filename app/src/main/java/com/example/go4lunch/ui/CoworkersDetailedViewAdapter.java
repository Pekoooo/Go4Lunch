package com.example.go4lunch.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.AppModel.User;

import java.util.List;

public class CoworkersDetailedViewAdapter extends RecyclerView.Adapter<CoworkersDetailedViewAdapter.CoworkerJoiningHolder>{
    private List<User> mUserList;

    public CoworkersDetailedViewAdapter(List<User> userList){

        this.mUserList = userList;


    }


    @NonNull
    @Override
    public CoworkerJoiningHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CoworkerJoiningHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class CoworkerJoiningHolder extends RecyclerView.ViewHolder {

        private final TextView coworkerName;
        private final ImageView coworkerPicture;

        public CoworkerJoiningHolder(@NonNull View itemView) {
            super(itemView);

            coworkerName       = itemView.findViewById(R.id.coworker_joining_profile_picture);
            coworkerPicture    = itemView.findViewById(R.id.coworker_joining_name);

        }
    }
}
