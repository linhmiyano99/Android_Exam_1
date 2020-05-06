package com.example.fossilandroidexam.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.model.StackoverflowService.Reputation;
import com.example.fossilandroidexam.modelview.StackoverflowViewModel;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapterReputation;

import java.util.List;
import java.util.Objects;

public class DetailOfUserActivity extends AppCompatActivity {
    private RecyclerViewAdapterReputation adapterReputation;
    private int intDetailPage;
    private StackoverflowViewModel viewModel;
    private String userIdReputation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_user);
        intDetailPage = 1;
        viewModel = new ViewModelProvider(this).get(StackoverflowViewModel.class);

        TextView userId = findViewById(R.id.txtUserId);
        userId.setText(Objects.requireNonNull(getIntent().getExtras()).getString("userId"));
        RecyclerView recyclerView = findViewById(R.id.listDetails);
        recyclerView.setHasFixedSize(true);
        adapterReputation = new RecyclerViewAdapterReputation();
        userIdReputation = Objects.requireNonNull(getIntent().getExtras()).getString("userId");

        viewModel.getListReputation().observe(this, new Observer<List<Reputation>>() {
            @Override
            public void onChanged(List<Reputation> reputations) {
                adapterReputation.addAllListReputations(reputations);
                intDetailPage++;
                adapterReputation.notifyDataSetChanged();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                viewDetailsOfUser();
            }
        });

        recyclerView.setAdapter(adapterReputation);
        viewDetailsOfUser();
    }
    public void viewDetailsOfUser(){
        viewModel.loadDetailsOfUserOfPage(userIdReputation, intDetailPage++);

    }

}
