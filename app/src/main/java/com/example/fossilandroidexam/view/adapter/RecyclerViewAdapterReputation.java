package com.example.fossilandroidexam.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.model.StackoverflowService.Reputation;
import com.example.fossilandroidexam.modelview.StackoverflowViewModel;


import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterReputation extends RecyclerView.Adapter<RecyclerViewAdapterReputation.ViewHolder> {

    private List<Reputation> listReputations;
    private StackoverflowViewModel viewModel;
    private int intDetailPage;
    private String userId;



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDetail = itemView.findViewById(R.id.txtDetail);
        }
    }

    public RecyclerViewAdapterReputation(Context context, StackoverflowViewModel viewModel) {
        listReputations = new ArrayList<>();
        intDetailPage = 1;
        this.viewModel = viewModel;
        this.viewModel.getListReputation().observe((LifecycleOwner) context, new Observer<List<Reputation>>() {
            @Override
            public void onChanged(List<Reputation> reputations) {
                addLisDetail(reputations);
            }
        });
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
        notifyDataSetChanged();
        Log.d("List user in adapter", String.valueOf(listReputations.size()));
    }
    public void clear() {
        listReputations.clear();
    }
    public void loadDetailOfUser(){
        viewModel.loadDetailsOfUserOfPage(userId, intDetailPage++);

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void resetDetailPage(){
        intDetailPage = 1;
    }
}
