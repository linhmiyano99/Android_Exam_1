package com.example.fossilandroidexam;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private StackoverflowAPI stackoverflowAPI;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapterOfUsers;
    private RecyclerViewAdapter adapterOfBookMarkUsers;
    private RecyclerViewAdapterReputation adapterReputations;
    boolean isDetails = false;
    boolean isUserPage = true;
    int intUserPage;
    int intDetailPage;
    private Parcelable recyclerViewState;
    private String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intUserPage = 1;
        intDetailPage = 1;
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

                    if(isUserPage) {
                        Log.d("isUserPage", String.valueOf(intUserPage));

                        recyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                        loadAllUsersOfNextPage();
                    }
                    else if(isDetails){
                        Log.d("isDetails", String.valueOf(intDetailPage));

                        recyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                        viewDetailsOfUser(userId);
                    }
                }

            }
        });

        adapterOfUsers = new RecyclerViewAdapter(MainActivity.this);
        adapterOfBookMarkUsers = new RecyclerViewAdapter(MainActivity.this);
        adapterReputations = new RecyclerViewAdapterReputation();

        createStackoverflowAPI();
        loadAllUsersOfNextPage();

    }

    Callback<ListWrapper<User>> usersCallback = new Callback<ListWrapper<User>> () {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<User> list = new ArrayList<>(response.body().items);
                if(response.body().items.size() == 0)
                    return;
                Log.d("listUsers", String.valueOf(list.size()));
                try {
                    adapterOfUsers.addListUser(list);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
                recyclerView.setAdapter(adapterOfUsers);

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

    public void loadAllUsersOfNextPage(View view) {
        //txtPage.setText("1");
        //stackoverflowAPI.getAllUsers(intUserPage++).enqueue(usersCallback);
        recyclerView.setAdapter(adapterOfUsers);

        isDetails = false;
        isUserPage = true;
    }
    public void loadAllUsersOfNextPage() {
        stackoverflowAPI.getAllUsers(intUserPage++).enqueue(usersCallback);
        isDetails = false;
        isUserPage = true;
    }

    public void loadAllBookmarkUsers(View view) {
        List <String> listBookmarkStrings;
        listBookmarkStrings = adapterOfUsers.getListBookmark();
        if (listBookmarkStrings!= null ) {
            StringBuilder groupStringId= new StringBuilder(listBookmarkStrings.get(0));
            for (int i =1; i < listBookmarkStrings.size(); i++) {
                groupStringId.append(";").append(listBookmarkStrings.get(i));
            }

            stackoverflowAPI.getUserFromId(groupStringId.toString()).enqueue(idUserCallBack);


        } else return;

        isDetails = false;
        isUserPage = false;
    }
    public void viewDetailsOfUser(View v){
        //txtPage.setText("1");
        intDetailPage =1;
        adapterReputations.clear();
        userId = String.valueOf(v.getTag());
        viewDetailsOfUser(userId);

    }
    public void viewDetailsOfUser(String userId){
        Log.d("viewDetailUser", String.valueOf(intDetailPage));
        isUserPage = false;
        isDetails = true;
        stackoverflowAPI.getReputationForUser(userId, intDetailPage++).enqueue(reputationCallBack);
    }
    Callback<ListWrapper<Reputation>> reputationCallBack = new Callback<ListWrapper<Reputation>> () {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<Reputation>> call, Response<ListWrapper<Reputation>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<Reputation> list = new ArrayList<>(response.body().items);
                if(response.body().items.size() == 0)
                    return;
                adapterReputations.addLisDetail(list);
                Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
                recyclerView.setAdapter(adapterReputations);
                Log.d("reputationCallBack", String.valueOf(intDetailPage));
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
    Callback<ListWrapper<User>> idUserCallBack = new Callback<ListWrapper<User>>() {

        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<User> list = new ArrayList<>(response.body().items);
                if(response.body().items.size() == 0)
                    return;
                try {
                    adapterOfBookMarkUsers.addListUser(list);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.setAdapter(adapterOfBookMarkUsers);
                adapterOfBookMarkUsers.notifyDataSetChanged();

            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<User>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");
        }
    };
}
