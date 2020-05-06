package com.example.fossilandroidexam.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fossilandroidexam.R;
import com.example.fossilandroidexam.model.StackoverflowService.Reputation;
import com.example.fossilandroidexam.model.StackoverflowService.User;
import com.example.fossilandroidexam.modelview.StackoverflowViewModel;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapter;
import com.example.fossilandroidexam.view.adapter.RecyclerViewAdapterBookmark;


import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapterOfUsers;
    private RecyclerViewAdapterBookmark adapterOfBookMarkUsers;
    boolean isUserPage = true;
    private Parcelable recyclerViewState;
    int intUserPage = 1;
    StackoverflowViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewModel = new ViewModelProvider(this).get(StackoverflowViewModel.class);


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
                        recyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                        viewModel.loadAllUsersOfPage(intUserPage);
                    }
                }
            }
        });


        adapterOfUsers = new RecyclerViewAdapter();
        adapterOfBookMarkUsers = new RecyclerViewAdapterBookmark();
        LoadDataFromViewModelToRecyclerViewAdapter();
        LoadDataFromViewModelToRecyclerViewAdapterBookmark();



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
                viewModel.loadBookmarkUser(adapterOfUsers.getListBookmark());
            }
        });


        viewModel.loadAllUsersOfPage(intUserPage);


        recyclerView.setAdapter(adapterOfUsers);

        listenersClickItem();


    }

    public void LoadDataFromViewModelToRecyclerViewAdapterBookmark(){
        viewModel.getListBookmarkUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.d("getListBookmarkUsers", String.valueOf(users));
                adapterOfBookMarkUsers.clearListUser();
                adapterOfBookMarkUsers.addAllListUser(users);
                Log.d("List user in adapter", String.valueOf(users));
                for (User user : users) {
                    viewModel.loadImage(user.getSrtProfileImageUrl());
                }
            }
        });
        viewModel.getListbookmark().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapterOfBookMarkUsers.addAllListBookmark(strings);

            }
        });
        viewModel.getEntryImage().observe(this, new Observer<Map.Entry<String, Bitmap>>() {
            @Override
            public void onChanged(Map.Entry<String, Bitmap> stringBitmapEntry) {
                adapterOfBookMarkUsers.putMapImage(stringBitmapEntry.getKey(), stringBitmapEntry.getValue());
                adapterOfBookMarkUsers.notifyDataSetChanged();
            }
        });
    }

    public void LoadDataFromViewModelToRecyclerViewAdapter(){

        //get list users response ,set value to adapter and load image
        viewModel.getListUsers().observe( this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                restoreState();
                adapterOfUsers.addAllListUser(users);
                Log.d("List user in adapter", String.valueOf(users));
                for (User user : users) {
                    if (adapterOfUsers.getMapImage().containsKey(user.getSrtProfileImageUrl()))
                        return;
                    viewModel.loadImage(user.getSrtProfileImageUrl());
                }
                intUserPage++;
            }
        });
        //get Bookmark list response
        viewModel.getListbookmark().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapterOfUsers.addAllListBookmark(strings);
                adapterOfUsers.notifyDataSetChanged();
            }
        });

        //get image
        viewModel.getEntryImage().observe(this, new Observer<Map.Entry<String, Bitmap>>() {
            @Override
            public void onChanged(Map.Entry<String, Bitmap> stringBitmapEntry) {
                adapterOfUsers.putMapImage(stringBitmapEntry.getKey(), stringBitmapEntry.getValue());
                adapterOfUsers.notifyDataSetChanged();
            }
        });
    }


    public void listenersClickItem()
    {
        //click item from recyclerViewAdapter
        adapterOfUsers.setOnItemUserReputationClickListener(new RecyclerViewAdapter.OnItemUserReputationClickListener() {
            @Override
            public void onItemClick(String string) {
                Intent intent = new Intent(MainActivity.this, DetailOfUserActivity.class);
                intent.putExtra("userId", string);
                startActivity(intent);
            }
        });

        //click item from recyclerViewAdapter BookMark
        adapterOfBookMarkUsers.setOnItemUserReputationClickListener(new RecyclerViewAdapterBookmark.OnItemUserBookmarkedReputationClickListener() {
            @Override
            public void onItemBookmarkedClick(String string) {
                Intent intent = new Intent(MainActivity.this, DetailOfUserActivity.class);
                intent.putExtra("userId", string);
                startActivity(intent);
            }
        });

        //click bookmark item from recyclerViewAdapter
        adapterOfUsers.setOnItemBookmarkClickListener(new RecyclerViewAdapter.OnItemBookmarkClickListener() {
            @Override
            public void onItemBookmark(String key, Boolean value) {
                viewModel.updateAUserOfBookmarkData(key, value);
            }
        });

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
    public void restoreState(){
        Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
        setUserPage();
    }



}
