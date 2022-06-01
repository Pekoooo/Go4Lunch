package com.example.go4lunch.ui.CoworkerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
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

public class CoworkersListViewAdapter extends RecyclerView.Adapter<CoworkersListViewAdapter.CoworkerListHolder>{
    private final List<User> coworkerList;
    private static final String TAG = "MyCoworkerListAdapter";

    public CoworkersListViewAdapter(List<User> userList){
        this.coworkerList = userList;
    }


    @NonNull
    @Override
    public CoworkerListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coworker_list_row, parent, false);
        Log.d(TAG, "onCreateViewHolder: is called");

        return new CoworkerListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoworkerListHolder holder, int position) {
        User currentCoworker = coworkerList.get(position);
        Log.d(TAG, "onBindViewHolder: " + currentCoworker.getUserName());
        String coworkerPictureUrl = currentCoworker.getAvatarURL();


        Glide.with(holder.coworkerPicture.getContext())
                .load(coworkerPictureUrl)
                .circleCrop()
                .into(holder.coworkerPicture);

        if(currentCoworker.getRestaurantChoiceId() == null){
            String userWithNoChoice = currentCoworker.getUserName() + " has not made a choice yet";
            holder.coworkerTextInfo.setText(userWithNoChoice);
            holder.coworkerTextInfo.setTextColor(Color.parseColor("#d3d3d3"));
            holder.coworkerTextInfo.setTypeface(null, Typeface.ITALIC);
        } else {
            String userWithChoice = currentCoworker.getUserName() + " is eating at " + currentCoworker.getRestaurantChoiceName();
            holder.coworkerTextInfo.setText(userWithChoice);

        }
    }

    @Override
    public int getItemCount() {
        return coworkerList.size();
    }

    static class CoworkerListHolder extends RecyclerView.ViewHolder {
        private final TextView coworkerTextInfo;
        private final ImageView coworkerPicture;

        public CoworkerListHolder(@NonNull View itemView) {
            super(itemView);

            coworkerTextInfo   = itemView.findViewById(R.id.coworker_info_text);
            coworkerPicture    = itemView.findViewById(R.id.coworker_joining_profile_picture);

        }
    }
}
