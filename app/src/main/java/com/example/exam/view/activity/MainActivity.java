package com.example.exam.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;
import com.example.exam.data.model.User;
import com.example.exam.modelview.StackOverflowViewModel;
import com.example.exam.view.adapter.RecyclerViewAdapter;
import com.example.exam.view.adapter.RecyclerViewAdapterBookmark;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    boolean isUserPage = true;
    int intUserPage = 1;
    StackOverflowViewModel viewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapterOfUsers;
    private RecyclerViewAdapterBookmark adapterOfBookMarkUsers;
    private Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(StackOverflowViewModel.class);
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (layoutManager.findLastCompletelyVisibleItemPosition() == Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() - 1) {
                    //bottom of list!
                    if (isUserPage) {
                        recyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                        viewModel.loadAllUsersOfPage(intUserPage);
                    }
                }
            }
        });

        adapterOfUsers = new RecyclerViewAdapter();
        adapterOfBookMarkUsers = new RecyclerViewAdapterBookmark();
        loadDataFromViewModelToRecyclerViewAdapter();
        loadDataFromViewModelToRecyclerViewAdapterBookmark();
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

    public void loadDataFromViewModelToRecyclerViewAdapterBookmark() {
        viewModel.getListBookmarkUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapterOfBookMarkUsers.setListUser(users);
            }
        });
    }

    public void loadDataFromViewModelToRecyclerViewAdapter() {
        //get list users response ,set value to adapter and load image
        viewModel.getListUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                restoreState();
                adapterOfUsers.addAllListUser(users);
                intUserPage++;
            }
        });
        //get Bookmark list response
        viewModel.getListBookmark().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapterOfUsers.addAllListBookmark(strings);
            }
        });

    }

    public void listenersClickItem() {
        adapterOfUsers.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemBookmark(String key, Boolean value) {
                viewModel.updateAUserOfBookmarkData(key, value);
            }

            @Override
            public void onItemUserClick(String string) {
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

    public void setBookmark() {
        isUserPage = false;
        recyclerView.setAdapter(adapterOfBookMarkUsers);
    }

    public void restoreState() {
        Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
        setUserPage();
    }

}
