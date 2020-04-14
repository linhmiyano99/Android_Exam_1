package com.example.fossilandroidexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private StackoverflowAPI stackoverflowAPI;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private EditText txtPage;
    boolean isDetails = false;
    List<User> listUsers ;
    List<Reputation> listReputationOfUser;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtPage= findViewById(R.id.txtPage);
        txtPage.setText("1");
        Button btnGoToPage = findViewById(R.id.btnGoToPage);
        btnGoToPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDetails)
                {
                    viewDetailUser(selectedUser);
                }
                else {
                    loadAllUsers();
                }
                Log.d("xxxxxx","spinnerPage");
            }
        });

       txtPage.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        listUsers = new ArrayList<>();
        listReputationOfUser = new ArrayList<>();
        adapter = new RecyclerViewAdapter(MainActivity.this, listUsers);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        createStackoverflowAPI();

    }

    Callback<ListWrapper<User>> usersCallback = new Callback<ListWrapper<User>> () {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
               // List<User> listUsers = new ArrayList<>();
                listUsers.clear();
                assert response.body() != null;
                listUsers.addAll(response.body().items);
                adapter = new RecyclerViewAdapter(MainActivity.this, listUsers);
                recyclerView.setAdapter(adapter);
                Log.d("usersCallback", String.valueOf(listUsers));
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
        txtPage.setText("1");
        stackoverflowAPI.getUsers(1).enqueue(usersCallback);
        isDetails = false;
    }
    public void loadAllUsers() {
        stackoverflowAPI.getUsers(Integer.parseInt(txtPage.getText().toString())).enqueue(usersCallback);
    }

    public void loadAllBookmarkUsers(View view) {
        adapter.filtBoorkmark();
        recyclerView.setAdapter(adapter);
        isDetails = false;
    }
    public void viewDetailUser(View v){
        txtPage.setText("1");
        selectedUser = String.valueOf(v.getTag());
        viewDetailUser(selectedUser);
        isDetails = true;
    }
    public void viewDetailUser(String userId){
        stackoverflowAPI.getReputationForUser(userId, Integer.parseInt(txtPage.getText().toString())).enqueue(reputationCallBack);
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
