package com.example.fossilandroidexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private StackoverflowAPI stackoverflowAPI;
    private String token;
    private RecyclerView recyclerView;
    private Button btnAllUsers;
    private Button btnAllBookmarkUsers;
    RecyclerViewAdapter adapter;

    List<User> listUsers ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAllUsers = findViewById(R.id.btnAllUsers);
        btnAllBookmarkUsers = findViewById(R.id.btnAllBoorkmarkUsers);
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        listUsers = new ArrayList<>();
        adapter = new RecyclerViewAdapter(MainActivity.this, listUsers);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        createStackoverflowAPI();
        loadAllUsers(btnAllUsers);
    }

    Callback<ListWrapper<User>> usersCallback = new Callback<ListWrapper<User>> () {
        @Override
        public void onResponse(Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
               // List<User> listUsers = new ArrayList<>();
                listUsers.clear();
                listUsers.addAll(response.body().items);
                adapter = new RecyclerViewAdapter(MainActivity.this, listUsers);
                recyclerView.setAdapter(adapter);
                Log.d("xxxxxx","succeed");
            } else {
                Log.d("Users", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<ListWrapper<User>> call, Throwable t) {
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
        stackoverflowAPI.getUsers().enqueue(usersCallback);
    }

    public void loadAllBookmarkUsers(View view) {
        adapter.filtBoorkmark();
        recyclerView.setAdapter(adapter);
    }
}
