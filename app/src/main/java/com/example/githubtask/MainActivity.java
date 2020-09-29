package com.example.githubtask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.githubtask.data.StoreData;
import com.example.githubtask.data.api.GitHubAPIInterface;
import com.example.githubtask.data.model.GitHubRepoModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.githubtask.HelperClass.ACCESS_TOKEN;
import static com.example.githubtask.HelperClass.API_URL;
import static com.example.githubtask.HelperClass.NO_INTERNET;
import static com.example.githubtask.HelperClass.TAG;

public class MainActivity extends AppCompatActivity {


    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    List<GitHubRepoModel> gitHubRepoModels = new ArrayList<>();
    int page = 1;
    int limit = 10;
    GitHubAdapter gitHubAdapter;
    SwipeRefreshLayout refreshLayout;
    private GitHubAPIInterface gitHubAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.rv);
        progressBar = findViewById(R.id.progress_bar);
        refreshLayout = findViewById(R.id.swipeContainer);
        gitHubAdapter = new GitHubAdapter(this, gitHubRepoModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(gitHubAdapter);

        // TODO check the internet
        if (HelperClass.checkInternetState(this)) {
            getData(page, limit);
        } else {
            Log.i(TAG, "onCreate: ");
            StoreData storeData = new StoreData();
            List<GitHubRepoModel> list = storeData.getSavedRepos(this);
            gitHubAdapter.setGitHubList(list);
        }

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        Log.i(TAG, "onScrollChange: ");
                        if (HelperClass.checkInternetState(this)) {
                            page++;
                            progressBar.setVisibility(View.VISIBLE);
                            getData(page, limit);
                        } else {
                            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        refreshLayout.setOnRefreshListener(() -> {
            if (HelperClass.checkInternetState(this)) {
                StoreData storeData = new StoreData();
                storeData.removeData(this);
                storeData.saveListRepos(gitHubRepoModels, this);

            } else {
                Toast.makeText(this, NO_INTERNET, Toast.LENGTH_SHORT).show();
            }
            refreshLayout.setRefreshing(false);
        });
    }

    private void getData(int page, int limit) {
        InitializeRetrofitVars();
        Call<List<GitHubRepoModel>> repoModelCall = gitHubAPIInterface.getRepsPages(page,
                limit, ACCESS_TOKEN);
        repoModelCall.enqueue(new Callback<List<GitHubRepoModel>>() {

            @Override
            public void onResponse(Call<List<GitHubRepoModel>> call, Response<List<GitHubRepoModel>> response) {
                if (response.body() != null) {
                    Log.i(TAG, "onResponse: response.isSuccessful() >> " + response.isSuccessful()
                            + " -- size of array >> " + response.body().size());
                }
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    if (response.body().size() != 0) {
                        gitHubAdapter.setGitHubList(response.body());
                        //   gitHubAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepoModel>> call, Throwable t) {

            }
        });

    }

    private void InitializeRetrofitVars() {
        Log.i(HelperClass.TAG, "MainActivity -- InitializeRetrofitVars() ");
        // logs request and response information.
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        gitHubAPIInterface = retrofit.create(GitHubAPIInterface.class);
    }


}