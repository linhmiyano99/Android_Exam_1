package com.example.fossilandroidexam;

import android.content.Context;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<User> listUser;
    private Context context;
    private List<String> listBookmark;
    private Map<String, Bitmap> mapImage;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textDisplay;
        public ImageView imageProfile;
        public ImageView imageBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDisplay = itemView.findViewById(R.id.displayInfo);
            imageProfile = itemView.findViewById(R.id.profile_image);
            imageBookmark = itemView.findViewById(R.id.bookmark);
            imageBookmark.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //filtBoorkmark();
                    Log.d("mapBookmark", String.valueOf(listBookmark.size()));
                    UpdateBoorkmark task;
                    if(listBookmark.contains(v.getTag()))
                    {
                        listBookmark.remove(v.getTag());
                        task = new UpdateBoorkmark((String) v.getTag(), false);
                    }
                    else
                    {
                        listBookmark.add((String) v.getTag());
                        task = new UpdateBoorkmark((String) v.getTag(), true);
                    }
                    //chua tim ra cach update 1 itemview
                    notifyDataSetChanged();
                    task.execute(context);
                }
            });

        }
    }

    public RecyclerViewAdapter(Context context, List<User> data) {
        this.listUser = data;
        this.context = context;
        listBookmark = new ArrayList<>();
        mapImage = new HashMap<>();
        LoadBookmarkTask task = new LoadBookmarkTask(this);
        task.execute(context);
        for (User user: data) {
            LoadImageTask task2 = new LoadImageTask(this);
            task2.execute(user.srtProfileImageUrl);
        }
    }
    public RecyclerViewAdapter(Context context) {
        this.listUser = new ArrayList<>();
        this.context = context;
        listBookmark = new ArrayList<>();
        mapImage = new HashMap<>();
        LoadBookmarkTask task = new LoadBookmarkTask(this);
        task.execute(context);

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
        User user =  listUser.get(position);
        holder.textDisplay.setText(user.toString() );
        holder.itemView.setTag(user.strUserId);
        holder.imageProfile.setImageBitmap(mapImage.get(user.srtProfileImageUrl));
        holder.imageBookmark.setTag(user.strUserId);
        holder.imageProfile.setTag(user.strUserId);
        holder.textDisplay.setTag(user.strUserId);


        // load bookmark
        if(listBookmark.contains(user.strUserId)){
            holder.imageBookmark.setImageResource(R.drawable.bookmark_black);
        }
        else{
            holder.imageBookmark.setImageResource(R.drawable.bookmark_border);
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void setListBookmark(List<String> list){
        listBookmark = list;
    }
    public void addMapImage(String key, Bitmap value){
        mapImage.put(key, value);
    }
    public void filtBoorkmark()
    {
        List<User> listBookmarkUsers = new ArrayList<>();
        for (User user:listUser) {
            if(listBookmark.contains(user.strUserId)) {
                listBookmarkUsers.add(user);
            }
        }
        Log.d("listBookmark", String.valueOf(listBookmarkUsers));
        listUser.clear();
        listUser.addAll(listBookmarkUsers);
        notifyDataSetChanged();
    }
    public void notifyLoadImageDone(){
        notifyDataSetChanged();
    }
    public List<String> getListBookmark(){
        return this.listBookmark;
    }
    public void addListUser(List<User> list) {
        listUser.addAll(list);
        Log.d("List user in adapter", String.valueOf(list));
        /*for (User user: list) {
            LoadImageTask task2 = new LoadImageTask(this);
            task2.execute(user.srtProfileImageUrl);
        }*/
    }
    public void addListImage(List<User> list) {
        //listUser.addAll(list);
        Log.d("List user image adapter", String.valueOf(list));
        for (User user: list) {
            LoadImageTask task2 = new LoadImageTask(this);
            task2.execute(user.srtProfileImageUrl);
        }
    }
    public User getUserById(String useId) {
        for (User user: listUser) {
            if(user.strUserId == useId){
                return user;
            }
        }
        return null;
    }
}
