package com.example.fossilandroidexam.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.data.model.stackoverflowservice.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterBookmark extends RecyclerView.Adapter<RecyclerViewAdapterBookmark.ViewHolder> {
    private List<User> listUser;
    private Map<String, Bitmap> mapImage;

    private OnItemUserBookmarkedReputationClickListener listener;

    public RecyclerViewAdapterBookmark() {
        this.listUser = new ArrayList<>();
        mapImage = new HashMap<>();
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
        holder.imageProfile.setImageBitmap(mapImage.get(user.getSrtProfileImageUrl()));
        holder.imageBookmark.setTag(user.getStrUserId());
        holder.linearLayout.setTag(user.getStrUserId());
        // load bookmark
        holder.imageBookmark.setImageResource(R.drawable.bookmark_black);

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void clearListUser() {
        listUser.clear();
    }

    public void addAllListUser(List<User> listUser) {
        this.listUser.addAll(listUser);
    }

    public void setOnItemUserReputationClickListener(OnItemUserBookmarkedReputationClickListener listener) {
        this.listener = listener;
    }

    public void addImage(String url, Bitmap resource) {
        mapImage.put(url, resource);
        notifyDataSetChanged();
    }

    @SuppressLint("CheckResult")
    public interface OnItemUserBookmarkedReputationClickListener {
        void onItemBookmarkedClick(String string);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDisplay;
        ImageView imageProfile;
        ImageView imageBookmark;
        LinearLayout linearLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDisplay = itemView.findViewById(R.id.displayInfo);
            imageProfile = itemView.findViewById(R.id.profile_image);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            imageBookmark = itemView.findViewById(R.id.bookmark);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemBookmarkedClick((String) v.getTag());
                }
            });
        }
    }
}
