package com.example.fossilandroidexam.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.modelview.StackoverflowViewModel;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapter;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapterBookmark;


import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapterOfUsers;
    private RecyclerViewAdapterBookmark adapterOfBookMarkUsers;
    boolean isUserPage = true;


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
                }
            }
        });

        adapterOfUsers = new RecyclerViewAdapter(MainActivity.this, viewModel);
        adapterOfBookMarkUsers = new RecyclerViewAdapterBookmark(MainActivity.this, viewModel);
        Button btnLoadUserAvailable = findViewById(R.id.btnAllUsers);
        btnLoadUserAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserPage();
            }
        });
        Button btnLoadAllBookmarkUser = findViewById(R.id.btnAllBoorkmarkUsers);
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }


    public void setUserPage() {
        isUserPage = true;
        recyclerView.setAdapter(adapterOfUsers);
    }
    public void setBookmark(){
        isUserPage = false;
        recyclerView.setAdapter(adapterOfBookMarkUsers);
    }
}
