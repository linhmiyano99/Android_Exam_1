package com.example.fossilandroidexam.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.model.StackoverflowService.User;
import com.example.fossilandroidexam.modelview.StackoverflowViewModel;
import com.example.fossilandroidexam.view.activity.DetailOfUserActivity;
import com.example.fossilandroidexam.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<User> listUser;
    private MainActivity context;
    private List<String> listBookmark;
    private Map<String, Bitmap> mapImage;
    private StackoverflowViewModel viewModel;
    private int intUserPage;

    //this override the implemented method from asyncTask

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
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailOfUserActivity.class);
                    intent.putExtra("userId", (String) v.getTag());
                    context.startActivity(intent);
                }
            });
            imageBookmark = itemView.findViewById(R.id.bookmark);
            imageBookmark.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //filtBoorkmark();
                    boolean value = true;
                    String key = (String) v.getTag();
                    if (listBookmark.contains(key)) {
                        listBookmark.remove(key);
                        value = false;
                    } else {
                        listBookmark.add(key);
                    }

                    viewModel.updateAUserOfBookmarkData(key, value);
                    notifyDataSetChanged();
                }
            });
        }
    }


    public RecyclerViewAdapter(Context context, StackoverflowViewModel viewModel) {
        this.listUser = new ArrayList<>();
        this.context = (MainActivity) context;
        mapImage = new HashMap<>();
        intUserPage = 1;
        this.viewModel = viewModel;
        this.viewModel.getListUsers().observe((LifecycleOwner) context, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                addListUser(users);
                intUserPage++;
            }
        });
        listBookmark = new ArrayList<>();
        this.viewModel.getListbookmark().observe((LifecycleOwner) context, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                listBookmark.addAll(strings);
            }
        });

        this.viewModel.getEntryImage().observe((LifecycleOwner) context, new Observer<Map.Entry<String, Bitmap>>() {
            @Override
            public void onChanged(Map.Entry<String, Bitmap> stringBitmapEntry) {
                mapImage.put(stringBitmapEntry.getKey(), stringBitmapEntry.getValue());
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view =
                inflater.inflate(R.layout.item_recylerview, parent, false);
        // Get the app's shared preferences
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.textDisplay.setText(user.toString());
        holder.itemView.setTag(user.getStrUserId());
        holder.imageProfile.setImageBitmap(mapImage.get(user.getSrtProfileImageUrl()));
        holder.imageBookmark.setTag(user.getStrUserId());
        holder.linearLayout.setTag(user.getStrUserId());

        // load bookmark
        if(listBookmark != null) {
            if (listBookmark.contains(user.getStrUserId())) {
                holder.imageBookmark.setImageResource(R.drawable.bookmark_black);
            } else {
                holder.imageBookmark.setImageResource(R.drawable.bookmark_border);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }



    @SuppressLint("CheckResult")
    private void addListUser(List<User> list) {
        listUser.addAll(list);
        Log.d("List user in adapter", String.valueOf(list));
        for (User user : list
        ) {
            if(mapImage.containsKey(user.getSrtProfileImageUrl()))
                return;
           viewModel.loadImage(user.getSrtProfileImageUrl());
        }
    }
    public void updateListBookmarkUsers(){
        viewModel.loadBookmarkUser(listBookmark);
    }
    public void loadUserOfPage(){
        viewModel.loadAllUsersOfPage(intUserPage);
    }
}

