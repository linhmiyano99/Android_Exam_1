package com.example.exam.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.exam.R;
import com.example.exam.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterBookmark extends RecyclerView.Adapter<RecyclerViewAdapterBookmark.ViewHolder> {

    private List<User> listUser;

    private OnItemUserBookmarkedReputationClickListener listener;

    public RecyclerViewAdapterBookmark() {
        this.listUser = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterBookmark.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view =
                inflater.inflate(R.layout.item_recylerview, parent, false);
        // Get the app's shared preferences
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterBookmark.ViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.textDisplay.setText(user.toString());
        holder.itemView.setTag(user.getStrUserId());
        Glide.with(holder.imageProfile).load(user.getSrtProfileImageUrl()).into(holder.imageProfile);
        holder.imageBookmark.setTag(user.getStrUserId());
        holder.constraint_layout.setTag(user.getStrUserId());
        holder.imageBookmark.setImageResource(R.drawable.bookmark_black);
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void setListUser(List<User> listUser) {
        this.listUser = listUser;
        notifyDataSetChanged();
    }

    public void setOnItemUserReputationClickListener(OnItemUserBookmarkedReputationClickListener listener) {
        this.listener = listener;
    }


    @SuppressLint("CheckResult")
    public interface OnItemUserBookmarkedReputationClickListener {
        void onItemBookmarkedClick(String string);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDisplay;
        ImageView imageProfile;
        ImageButton imageBookmark;
        ConstraintLayout constraint_layout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDisplay = itemView.findViewById(R.id.displayInfo);
            imageProfile = itemView.findViewById(R.id.profile_image);
            constraint_layout = itemView.findViewById(R.id.constraint_layout);
            imageBookmark = itemView.findViewById(R.id.bookmark);
            constraint_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemBookmarkedClick((String) v.getTag());
                }
            });
        }
    }
}
