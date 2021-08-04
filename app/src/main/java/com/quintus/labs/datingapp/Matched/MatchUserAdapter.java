package com.quintus.labs.datingapp.Matched;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.Chat.MessageActivity;
import com.quintus.labs.datingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchUserAdapter extends RecyclerView.Adapter<MatchUserAdapter.MyViewHolder> {
    List<Users> usersList;
    Activity mActivity;

    public MatchUserAdapter(List<Users> usersList, Activity activity) {
        this.usersList = usersList;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public MatchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchUserAdapter.MyViewHolder holder, int position) {
        final Users users = usersList.get(position);
        holder.name.setText(users.getName());
        holder.profession.setText("Hello em!");
        if (users.getProfileImageUrl() != null) {
            Glide.with(mActivity).load(users.getProfileImageUrl()).into(holder.imageView);
        }
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, MessageActivity.class);
                i.putExtra("name", users.getName());
                if(users.getProfileImageUrl() != null)
                    i.putExtra("avatar", users.getProfileImageUrl());
                else
                    i.putExtra("avatar", "NO");
                mActivity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imageView;
        TextView name, profession;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            profession = itemView.findViewById(R.id.mui_profession);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mActivity, "asdadsadsds", Toast.LENGTH_SHORT).show();
        }
    }
}
