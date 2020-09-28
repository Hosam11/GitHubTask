package com.example.githubtask.data.api;

import android.util.Log;

import com.example.githubtask.HelperClass;
import com.example.githubtask.data.model.GitHubRepoModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.githubtask.HelperClass.API_URL;

public class GitHubAPIProvider {

    private GitHubAPIInterface gitHubAPIInterface;

    public GitHubAPIProvider() {
     /*   Log.i(HelperClass.TAG, "GitHubAPIProvider -- cons() ");
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

        gitHubAPIInterface = retrofit.create(GitHubAPIInterface.class);*/
    }


     void getData(int page, int limit) {
        /*Call<GitHubRepoModel> repoModelCall = gitHubAPIInterface.getRepsPages(page, limit);

        repoModelCall.enqueue(new Callback<GitHubRepoModel>() {
            @Override
            public void onResponse(Call<GitHubRepoModel> call, Response<GitHubRepoModel> response) {

            }

            @Override
            public void onFailure(Call<GitHubRepoModel> call, Throwable t) {

            }
        });*/
    }


}
