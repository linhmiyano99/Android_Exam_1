package com.example.fossilandroidexam.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.data.model.stackoverflowservice.User;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<User> listUser;
    private List<String> listBookmark;
    private OnItemClickListener listener;

    public RecyclerViewAdapter() {
        this.listUser = new ArrayList<>();
        listBookmark = new ArrayList<>();
    }

    public void addAllListUser(List<User> listUser) {
        this.listUser.addAll(listUser);
        notifyDataSetChanged();
    }

    public void addAllListBookmark(List<String> listBookmark) {
        this.listBookmark.addAll(listBookmark);
        notifyDataSetChanged();
    }

    public List<String> getListBookmark() {
        return listBookmark;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view =
                inflater.inflate(R.layout.item_recylerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final User user = listUser.get(position);
        holder.textDisplay.setText(user.toString());
        holder.itemView.setTag(user.getStrUserId());
        Glide.with(holder.imageProfile).load(user.getSrtProfileImageUrl()).into(holder.imageProfile);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public interface OnItemClickListener {
        void onItemBookmark(String key, Boolean value);
        void onItemUserClick(String string);
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
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemUserClick((String) v.getTag());
                }
            });
            imageBookmark = itemView.findViewById(R.id.bookmark);
            imageBookmark.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    boolean value = true;
                    String key = (String) v.getTag();
                    if (listBookmark.contains(key)) {
                        listBookmark.remove(key);
                        value = false;
                    } else {
                        listBookmark.add(key);
                    }
                    if (listener!= null) {
                        listener.onItemBookmark(key, value);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
