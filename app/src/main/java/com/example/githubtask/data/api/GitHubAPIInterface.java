package com.example.githubtask.data.api;

import com.example.githubtask.data.model.GitHubRepoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubAPIInterface {

    @GET("repos?")
    Call<List<GitHubRepoModel>> getRepsPages(@Query("page") int page,
                                            @Query("per_page") int perPage,
                                             @Query("access_token") String accessToken);


}
