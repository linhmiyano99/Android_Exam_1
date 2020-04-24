package com.example.fossilandroidexam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements ListBookmarkResponse, ImageResponse {
    private List<User> listUser;
    private Context context;
    private List<String> listBookmark;
    private Map<String, Bitmap> mapImage;

    //this override the implemented method from asyncTask
    @Override
    public void processListBookmarkFinish(List<String> output) {
        listBookmark = output;
        notifyDataSetChanged();
    }

    @Override
    public void processImageFinish(String key, Bitmap image) {
        mapImage.put(key, image);
        notifyDataSetChanged();

    }

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
            imageBookmark = itemView.findViewById(R.id.bookmark);
            imageBookmark.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //filtBoorkmark();
                    Log.d("mapBookmark", String.valueOf(listBookmark.size()));
                    UpdateBookmark task;

                    if(listBookmark.contains(v.getTag()))
                    {
                        listBookmark.remove(v.getTag());
                        task = new UpdateBookmark((String) v.getTag(), false);
                    }
                    else
                    {
                        listBookmark.add((String) v.getTag());
                        task = new UpdateBookmark((String) v.getTag(), true);
                    }
                    //chua tim ra cach update 1 itemview
                    notifyDataSetChanged();
                    task.execute(context);



                }
            });
     /*       linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userId = ;
                    viewDetailUser(userId);
                }*/
            }
    }


  /*  public RecyclerViewAdapter(Context context, List<User> data) {
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
    }*/
    public RecyclerViewAdapter(Context context) {
        this.listUser = new ArrayList<>();
        this.context = context;
        listBookmark = new ArrayList<>();
        mapImage = new HashMap<>();
        //LoadBookmarkTask task = new LoadBookmarkTask(this);
        LoadBookmarkTask task = new LoadBookmarkTask();
        //this to set delegate/listener back to this class
        task.delegate = this;
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
        holder.linearLayout.setTag(user.strUserId);

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



    public void clearListUser()
    {
        listUser.clear();
    }
    public void notifyLoadImageDone(){
        notifyDataSetChanged();
    }
    public List<String> getListBookmark(){
        return this.listBookmark;
    }
    @SuppressLint("CheckResult")
    public void addListUser(List<User> list) throws ExecutionException, InterruptedException {
        listUser.addAll(list);
        Log.d("List user in adapter", String.valueOf(list));
        /*for (final User user: list
             ) {
            Glide.with(context).
                    load(user.srtProfileImageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d("Glide", "onLoadFailed");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("Glide", "onResourceReady");

                            mapImage.put(user.strUserId, (Drawable) target);
                            notifyDataSetChanged();
                            return true;
                        }
                    });
        }*/
        for (User user: list
             ) {
            LoadImageTask task = new LoadImageTask();
            task.delegate = this;
            task.execute(user.srtProfileImageUrl);
        }
    }
/*
    public void addListImage(List<User> list) {

        Log.d("List user image adapter", String.valueOf(list));
        for (User user: list) {
            LoadImageTask task = new LoadImageTask();
            task.delegate = this;
            task.execute(user.srtProfileImageUrl);
        }
    }
*/

}
