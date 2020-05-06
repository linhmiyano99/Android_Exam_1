package com.example.fossilandroidexam.view.adapter;

import android.content.Context;
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
import com.example.fossilandroidexam.model.StackoverflowService.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<User> listUser;

    public void addAllListUser(List<User> listUser) {
        this.listUser.addAll(listUser);
    }

    public void addAllListBookmark(List<String> listBookmark) {
        this.listBookmark.addAll(listBookmark);
    }

    public void putMapImage(String key, Bitmap value) {

        this.mapImage.put(key, value);
    }

    public List<String> getListBookmark() {
        return listBookmark;
    }

    private List<String> listBookmark;

    public Map<String, Bitmap> getMapImage() {
        return mapImage;
    }

    private Map<String, Bitmap> mapImage;




    private OnItemUserReputationClickListener listenerReputationItem;
    private OnItemBookmarkClickListener listenerBookMarkItem;

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
                    if (listenerReputationItem!=null)
                        listenerReputationItem.onItemClick((String) v.getTag());
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
                    if (listenerBookMarkItem!= null)
                        listenerBookMarkItem.onItemBookmark(key,value);

                    notifyDataSetChanged();
                }
            });
        }
    }


    public RecyclerViewAdapter() {
        this.listUser = new ArrayList<>();
        mapImage = new HashMap<>();
        listBookmark = new ArrayList<>();
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
        if (listBookmark != null) {
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


    public interface OnItemBookmarkClickListener{
        void onItemBookmark(String key, Boolean value);
    }
    public void setOnItemBookmarkClickListener(OnItemBookmarkClickListener listener) {
        this.listenerBookMarkItem = listener;
    }


    public interface OnItemUserReputationClickListener{
        void onItemClick(String string);
    }
    public void setOnItemUserReputationClickListener(OnItemUserReputationClickListener listener){
        this.listenerReputationItem = listener;
    }
}
