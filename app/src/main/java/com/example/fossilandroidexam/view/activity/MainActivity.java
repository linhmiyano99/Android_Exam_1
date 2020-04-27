package com.example.fossilandroidexam.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.modelview.StackoverflowViewModel;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapter;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapterBookmark;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapterReputation;


import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapterOfUsers;
    private RecyclerViewAdapterBookmark adapterOfBookMarkUsers;
    private RecyclerViewAdapterReputation adapterReputations;
    boolean isDetails = false;
    boolean isUserPage = true;
    private String userId;
    private Button btnLoadAllBookmarkUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StackoverflowViewModel viewModel = new ViewModelProvider(this).get(StackoverflowViewModel.class);
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if(layoutManager.findLastCompletelyVisibleItemPosition()== Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() -1){
                    //bottom of list!

                    if(isUserPage) {

                        loadAllUsersOfNextPage();
                    }
                    else if(isDetails){

                        viewDetailsOfUser(userId);
                    }
                }
            }
        });

        adapterOfUsers = new RecyclerViewAdapter(MainActivity.this, viewModel);
        adapterOfBookMarkUsers = new RecyclerViewAdapterBookmark(MainActivity.this, viewModel);
        adapterReputations = new RecyclerViewAdapterReputation(MainActivity.this, viewModel);
        Button btnLoadUserAvailable = findViewById(R.id.btnAllUsers);
        btnLoadUserAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserPage();
            }
        });
        btnLoadAllBookmarkUser = findViewById(R.id.btnAllBoorkmarkUsers);
        btnLoadAllBookmarkUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBookmark();
                adapterOfUsers.updateListBookmarkUsers(); // need minimize
            }
        });
        loadAllUsersOfNextPage();
        recyclerView.setAdapter(adapterOfUsers);
    }
    public void loadAllUsersOfNextPage() {
        adapterOfUsers.loadUserOfPage();
        setUserPage();
    }
    public void viewDetailsOfUser(String userId){
        adapterReputations.setUserId(userId);
        adapterReputations.loadDetailOfUser();
        this.userId = userId;
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        recyclerView.setAdapter(adapterOfUsers);
        isDetails = false;
        isUserPage = true;
    }

    public void setDetails() {
        isDetails = true;
        isUserPage = false;
        recyclerView.setAdapter(adapterReputations);
    }

    public void setUserPage() {
        isUserPage = true;
        isDetails = false;
        recyclerView.setAdapter(adapterOfUsers);
    }
    public void setBookmark(){
        isUserPage = false;
        isDetails = false;
        recyclerView.setAdapter(adapterOfBookMarkUsers);
    }
    public void resetDetailPage(){
        adapterReputations.resetDetailPage();
        adapterReputations.clear();
    }
}
