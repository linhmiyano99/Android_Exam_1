package com.example.fossilandroidexam.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.fossilandroidexam.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<User> listUser;
    private MainActivity context;
    private List<String> listBookmark;
    private Map<String, Bitmap> mapImage;
    private StackoverflowViewModel viewModel;
    private int intUserPage;

    //this override the implemented method from asyncTask

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textDisplay;
        public ImageView imageProfile;
        public ImageView imageBookmark;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDisplay = itemView.findViewById(R.id.displayInfo);
            imageProfile = itemView.findViewById(R.id.profile_image);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.resetDetailPage();
                    context.viewDetailsOfUser((String) v.getTag());
                    context.setDetails();
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
                try {
                    addListUser(users);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
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
        holder.itemView.setTag(user.strUserId);
        holder.imageProfile.setImageBitmap(mapImage.get(user.srtProfileImageUrl));
        holder.imageBookmark.setTag(user.strUserId);
        holder.linearLayout.setTag(user.strUserId);

        // load bookmark
        if(listBookmark != null) {
            if (listBookmark.contains(user.strUserId)) {
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

    public List<String> getListBookmark() {
        return this.listBookmark;
    }

    @SuppressLint("CheckResult")
    public void addListUser(List<User> list) throws ExecutionException, InterruptedException {
        listUser.addAll(list);
        Log.d("List user in adapter", String.valueOf(list));
        for (User user : list
        ) {
            if(mapImage.containsKey(user.srtProfileImageUrl))
                return;
           viewModel.loadImage(user.srtProfileImageUrl);
        }
    }
    public void updateListBookmarkUsers(){
        viewModel.loadBookmarkUser(listBookmark);
    }
    public void loadUserOfPage(){
        viewModel.loadAllUsersOfPage(intUserPage++);
    }
}

