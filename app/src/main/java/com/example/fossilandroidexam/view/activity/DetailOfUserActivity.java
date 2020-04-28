package com.example.fossilandroidexam.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.modelview.StackoverflowViewModel;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapterReputation;

import java.util.Objects;

public class DetailOfUserActivity extends AppCompatActivity {
    private RecyclerViewAdapterReputation adapterReputation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_user);
        StackoverflowViewModel viewModel = new ViewModelProvider(this).get(StackoverflowViewModel.class);
        TextView userId = findViewById(R.id.txtUserId);
        userId.setText(Objects.requireNonNull(getIntent().getExtras()).getString("userId"));
        RecyclerView recyclerView = findViewById(R.id.listDetails);
        recyclerView.setHasFixedSize(true);
        adapterReputation = new RecyclerViewAdapterReputation(DetailOfUserActivity.this, viewModel);
        adapterReputation.setUserId(Objects.requireNonNull(getIntent().getExtras()).getString("userId"));
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
        adapterReputation.loadDetailOfUser();
    }

    public void detail(View view) {
        viewDetailsOfUser();
    }
}
