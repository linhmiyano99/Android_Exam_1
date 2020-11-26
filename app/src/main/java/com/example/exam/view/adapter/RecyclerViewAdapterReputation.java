package com.example.exam.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;
import com.example.exam.data.model.Reputation;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterReputation extends RecyclerView.Adapter<RecyclerViewAdapterReputation.ViewHolder> {
    private List<Reputation> listReputations;

    public RecyclerViewAdapterReputation() {
        listReputations = new ArrayList<>();
    }

    public void addAllListReputations(List<Reputation> listReputations) {
        this.listReputations.addAll(listReputations);
    }

    @NonNull
    @Override
    public RecyclerViewAdapterReputation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view = inflater.inflate(R.layout.item_recyclerview_reputation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReputation.ViewHolder holder, int position) {
        Reputation reputation = listReputations.get(position);
        holder.txtDetail.setText(reputation.toString());
        holder.itemView.setTag(reputation.getStrUserId());
    }

    @Override
    public int getItemCount() {
        return listReputations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDetail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDetail = itemView.findViewById(R.id.txtDetail);
        }
    }

}
