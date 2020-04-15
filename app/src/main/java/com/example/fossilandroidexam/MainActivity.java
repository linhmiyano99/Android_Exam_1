package com.example.fossilandroidexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private StackoverflowAPI stackoverflowAPI;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    boolean isDetails = false;
    List<User> listUsers ;
    List<Reputation> listReputationOfUser;
    int intUserPage;
    private Parcelable recyclerViewState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intUserPage = 1;
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);

                if(layoutManager.findLastCompletelyVisibleItemPosition()== Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() -1){
                    //bottom of list!
                    loadAllUsers();
                    recyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                }

            }
        });
        listUsers = new ArrayList<>();
        listReputationOfUser = new ArrayList<>();
        adapter = new RecyclerViewAdapter(MainActivity.this, listUsers);

        createStackoverflowAPI();
        loadAllUsers();

    }

    Callback<ListWrapper<User>> usersCallback = new Callback<ListWrapper<User>> () {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<User> listUsers = new ArrayList<>(response.body().items);
                if(listUsers.size() == 0)
                    return;
                Log.d("listUsers", String.valueOf(listUsers.size()));
                //adapter = new RecyclerViewAdapter(MainActivity.this, listUsers);
                adapter.addListUser(listUsers);
                Log.d("adapter", String.valueOf(adapter.getItemCount()));
                Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
                recyclerView.setAdapter(adapter);

            } else {
                Log.d("usersCallback", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<User>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");

        }

    };
    private void createStackoverflowAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StackoverflowAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        stackoverflowAPI = retrofit.create(StackoverflowAPI.class);
    }

    public void loadAllUsers(View view) {
        //txtPage.setText("1");
        stackoverflowAPI.getAllUsers(intUserPage++).enqueue(usersCallback);
        isDetails = false;
    }
    public void loadAllUsers() {
        stackoverflowAPI.getAllUsers(intUserPage++).enqueue(usersCallback);
    }

    public void loadAllBookmarkUsers(View view) {
        List<String> listBookmark=new ArrayList<String>();
        listBookmark= adapter.getListBookmark();
        adapter.filtBoorkmark();

        recyclerView.setAdapter(adapter);
        isDetails = false;
    }
    public void viewDetailUser(View v){
        //txtPage.setText("1");

        viewDetailUser(String.valueOf(v.getTag()));
        isDetails = true;
    }
    public void viewDetailUser(String userId){
        stackoverflowAPI.getReputationForUser(userId,1).enqueue(reputationCallBack);
    }
    Callback<ListWrapper<Reputation>> reputationCallBack = new Callback<ListWrapper<Reputation>> () {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<Reputation>> call, Response<ListWrapper<Reputation>> response) {
            if (response.isSuccessful()) {
                listReputationOfUser.clear();
                assert response.body() != null;
                listReputationOfUser.addAll(response.body().items);
                recyclerView.setAdapter(new RecyclerViewAdapterReputation(listReputationOfUser));
                Log.d("reputationCallBack","succeed");
            } else {
                Log.d("reputationCallBack", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<Reputation>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");
        }

    };
}
