package com.example.fossilandroidexam.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.model.StackoverflowService.Reputation;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterReputation extends RecyclerView.Adapter<RecyclerViewAdapterReputation.ViewHolder> {

    public void addAllListReputations(List<Reputation> listReputations) {
        this.listReputations.addAll(listReputations);
    }

    private List<Reputation> listReputations;



    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDetail;
        ViewHolder(@NonNull View itemView) {
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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReputation.ViewHolder holder, int position) {
        Reputation reputation =  listReputations.get(position);
        holder.txtDetail.setText(reputation.toString());
        holder.itemView.setTag(reputation.getStrUserId());
    }

    @Override
    public int getItemCount() {
        return listReputations.size();
    }



}
