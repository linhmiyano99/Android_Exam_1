package com.example.fossilandroidexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private StackoverflowAPI stackoverflowAPI;
    private RecyclerView recyclerView;
    private Button btnAllUsers;
    private RecyclerViewAdapter adapter;
    private Spinner spinnerPage;
    boolean isDetails = false;
    List<User> listUsers ;
    List<Reputation> listReputationOfUser;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAllUsers = findViewById(R.id.btnAllUsers);
        spinnerPage= findViewById(R.id.spinnerPage);
        List<Integer> listNumberOfSpinner =
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, listNumberOfSpinner);
        spinnerPage.setAdapter(arrayAdapter);
        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isDetails)
                {
                    viewDetailUser(selectedUser);
                }
                else {
                    loadAllUsers(btnAllUsers);
                }
                Log.d("xxxxxx","spinnerPage");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        public void onResponse(Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
               // List<User> listUsers = new ArrayList<>();
                listUsers.clear();
                listUsers.addAll(response.body().items);
                adapter = new RecyclerViewAdapter(MainActivity.this, listUsers);
                recyclerView.setAdapter(adapter);
                Log.d("usersCallback", String.valueOf(listUsers));
            } else {
                Log.d("usersCallback", "Code: " + response.code() + " Message: " + response.message());
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
        Log.d("spinnerPage", String.valueOf(spinnerPage.getSelectedItem()));
        stackoverflowAPI.getUsers((Integer) spinnerPage.getSelectedItem()).enqueue(usersCallback);
        Log.d("xxxxxxx","hello");
        isDetails = false;
    }

    public void loadAllBookmarkUsers(View view) {
        adapter.filtBoorkmark();
        recyclerView.setAdapter(adapter);
        isDetails = false;
    }
    public void viewDetailUser(View v){
        viewDetailUser(String.valueOf(v.getTag()));
        spinnerPage.setSelection(0);
        selectedUser = String.valueOf(v.getTag());
        isDetails = true;
    }
    public void viewDetailUser(String userId){
        stackoverflowAPI.getReputationForUser(userId, (Integer) spinnerPage.getSelectedItem()).enqueue(reputationCallBack);
    }
    Callback<ListWrapper<Reputation>> reputationCallBack = new Callback<ListWrapper<Reputation>> () {
        @Override
        public void onResponse(Call<ListWrapper<Reputation>> call, Response<ListWrapper<Reputation>> response) {
            if (response.isSuccessful()) {
                listReputationOfUser.clear();
                listReputationOfUser.addAll(response.body().items);
                recyclerView.setAdapter(new RecyclerViewAdapterReputation(listReputationOfUser));
                Log.d("reputationCallBack","succeed");
            } else {
                Log.d("reputationCallBack", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<ListWrapper<Reputation>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");
        }

    };
}
