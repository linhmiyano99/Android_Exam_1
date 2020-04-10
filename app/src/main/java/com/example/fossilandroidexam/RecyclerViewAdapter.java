package com.example.fossilandroidexam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<User> data;
    Context context;

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
                    Log.v("xxxxxx", String.valueOf(v.getTag()));
                }
            });

        }
    }

    public RecyclerViewAdapter(Context context, List<User> data) {
        this.data = data;
        this.context = context;
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

}
