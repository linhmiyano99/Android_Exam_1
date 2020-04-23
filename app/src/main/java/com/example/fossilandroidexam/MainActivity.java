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
    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapter adapterBookMark;
    private RecyclerViewAdapterReputation adapterReputation;
    boolean isDetails = false;
    boolean isUserPage = true;
/*    List<User> listUsers ;
    List<Reputation> listReputationOfUser;
    List <String> listBookmarkStrings;*/

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
                        loadAllUsers();
                    }
                    else if(isDetails){
                        Log.d("isDetails", String.valueOf(intDetailPage));

                        recyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                        viewDetailUser(userId);
                    }
                }

            }
        });

        adapter = new RecyclerViewAdapter(MainActivity.this);
        adapterBookMark = new RecyclerViewAdapter(MainActivity.this);
        adapterReputation = new RecyclerViewAdapterReputation();

        createStackoverflowAPI();
        loadAllUsers();
        loadAllUsers();

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
                    adapter.addListUser(list);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        isUserPage = true;
    }
    public void loadAllUsers() {
        stackoverflowAPI.getAllUsers(intUserPage++).enqueue(usersCallback);
        isDetails = false;
        isUserPage = true;
    }

    public void loadAllBookmarkUsers(View view) {
        List <String> listBookmarkStrings;
        listBookmarkStrings = adapter.getListBookmark();
        if (listBookmarkStrings!= null ) {
            String groupStringId=listBookmarkStrings.get(0);
            for (int i =1; i < listBookmarkStrings.size(); i++) {
                groupStringId += ";" + listBookmarkStrings.get(i);
            }

            stackoverflowAPI.getUserFromId(groupStringId).enqueue(idUserCallBack);


        } else return;

        isDetails = false;
        isUserPage = false;
    }
    public void viewDetailUser(View v){
        //txtPage.setText("1");
        intDetailPage =1;
        adapterReputation.clear();
        userId = String.valueOf(v.getTag());
        viewDetailUser(userId);
    }
    public void viewDetailUser(String userId){
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
                adapterReputation.addLisDetail(list);
                Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
                recyclerView.setAdapter(adapterReputation);
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
        public void onResponse(Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                List<User> list = new ArrayList<>(response.body().items);
                if(response.body().items.size() == 0)
                    return;
                try {
                    adapterBookMark.addListUser(list);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerView.setAdapter(adapterBookMark);
                adapterBookMark.notifyDataSetChanged();

            }
            else return;
        }

        @Override
        public void onFailure(Call<ListWrapper<User>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");
        }
    };
}
