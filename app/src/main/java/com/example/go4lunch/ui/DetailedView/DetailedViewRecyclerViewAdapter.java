package com.example.go4lunch.ui.DetailedView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.model.AppModel.User;

import java.util.List;

public class DetailedViewRecyclerViewAdapter extends RecyclerView.Adapter<DetailedViewRecyclerViewAdapter.CoworkerViewHolder> {
    private final List<User> coworkerList;

    public DetailedViewRecyclerViewAdapter(List<User> coworkerList) {
        this.coworkerList = coworkerList;
    }

    @NonNull
    @Override
    public CoworkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coworker_joining_list_row, parent, false);

        return new CoworkerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoworkerViewHolder holder, int position) {
        User currentCoworker = coworkerList.get(position);

        String coworkerAvatarUrl;
        if (currentCoworker.getAvatarURL() == null) {
            coworkerAvatarUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png";
        } else {
            coworkerAvatarUrl = currentCoworker.getAvatarURL();
        }

        String coworkerIsJoining = currentCoworker.getUserName() + " is joining";

        holder.coworkerName.setText(coworkerIsJoining);
        Glide.with(holder.coworkerProfilePicture.getContext())
                .load(coworkerAvatarUrl)
                .circleCrop()
                .into(holder.coworkerProfilePicture);
    }

    @Override
    public int getItemCount() {
        return coworkerList.size();
    }

    static class CoworkerViewHolder extends RecyclerView.ViewHolder {
        private final TextView coworkerName;
        private final ImageView coworkerProfilePicture;

        public CoworkerViewHolder(@NonNull View itemView) {
            super(itemView);
            coworkerName = itemView.findViewById(R.id.coworker_info_text);
            coworkerProfilePicture = itemView.findViewById(R.id.coworker_joining_profile_picture);
        }
    }


}
