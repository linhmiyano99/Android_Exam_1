package com.example.fossilandroidexam;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterReputation extends RecyclerView.Adapter<RecyclerViewAdapterReputation.ViewHolder> {

    List<Reputation> listReputations;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDetail = itemView.findViewById(R.id.txtDetail);
        }
    }

    public RecyclerViewAdapterReputation() {
        listReputations = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterReputation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view =
                inflater.inflate(R.layout.item_recyclerview_reputation, parent, false);
        // Get the app's shared preferences


        return new ViewHolder(view) ;
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReputation.ViewHolder holder, int position) {
        Reputation reputation =  listReputations.get(position);
        holder.txtDetail.setText(reputation.toString());
        holder.itemView.setTag(reputation.strUserId);

    }

    @Override
    public int getItemCount() {
        return listReputations.size();
    }
    public void addLisDetail(List<Reputation> list) {
        listReputations.addAll(list);
        Log.d("List user in adapter", String.valueOf(listReputations.size()));
    }
    public void clear() {
        listReputations.clear();
    }
}
