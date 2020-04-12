package com.example.fossilandroidexam;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<User> data;
    Context context;
    Map<String, Boolean> mapBookmark;


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
                    if(!mapBookmark.containsKey(v.getTag())){
                        mapBookmark.put((String) v.getTag(), false);
                    }else {
                        if (mapBookmark.get(v.getTag())
                        ) {
                            mapBookmark.put((String) v.getTag(), false);

                        } else {
                            mapBookmark.put((String) v.getTag(), true);
                        }
                    }
                    notifyDataSetChanged();
                    UpdateBoorkmark task = new UpdateBoorkmark((String) v.getTag(),  mapBookmark.get(v.getTag()));
                    task.execute(context);
                }
            });

        }
    }

    public RecyclerViewAdapter(Context context, List<User> data) {
        this.data = data;
        this.context = context;
        mapBookmark = new HashMap<>();
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
        User user =  data.get(position);
        holder.textDisplay.setText(user.toString() );
        holder.itemView.setTag(user.user_id);
        loadImage(holder, user.profile_image);
        holder.imageBookmark.setTag(user.user_id);
        if(!mapBookmark.containsKey(user.user_id)){
            holder.imageBookmark.setImageResource(R.drawable.bookmark_border);
        }
        else {
            if (mapBookmark.get(user.user_id)){
                holder.imageBookmark.setImageResource(R.drawable.bookmark_black);

            }
            else{
                holder.imageBookmark.setImageResource(R.drawable.bookmark_border);

            }

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    /**
     *  load Image
     */
    public void loadImage(final RecyclerViewAdapter.ViewHolder holder, String imageUrl) {
        // Create a task to download and display image.///
        DownloadImageTask task = new DownloadImageTask(holder.imageProfile);
        // Execute task (Pass imageUrl).
        task.execute(imageUrl);
    }
    public void setMapBookmark(Map<String, Boolean> list){
        mapBookmark = list;
    }

}
