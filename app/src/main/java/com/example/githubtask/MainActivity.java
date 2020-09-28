package com.example.githubtask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import static com.example.githubtask.HelperClass.ACESS_TOKEN;
import static com.example.githubtask.HelperClass.API_URL;
import static com.example.githubtask.HelperClass.TAG;

public class MainActivity extends AppCompatActivity {


    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;


    List<GitHubRepoModel> gitHubRepoModels = new ArrayList<>();
    int page = 1;
    int limit = 10;
    GitHubAdapter gitHubAdapter;

    private GitHubAPIInterface gitHubAPIInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.rv);
        progressBar = findViewById(R.id.progress_bar);

        gitHubAdapter = new GitHubAdapter(this, gitHubRepoModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(gitHubAdapter);

        getData(page, limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    Log.i(TAG, "onScrollChange: ");
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getData(page,limit);
                }
            }
        });

    }

    private void getData(int page, int limit) {


        InitializeRetrofitVars();


        Call<List<GitHubRepoModel>> repoModelCall = gitHubAPIInterface.getRepsPages(page,
                limit, ACESS_TOKEN);

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
                        gitHubAdapter.notifyDataSetChanged();
                    }

                    // set Adapter
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