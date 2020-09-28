package com.example.githubtask.data.model;

import com.google.gson.annotations.SerializedName;

public class GitHubRepoModel {

    @SerializedName("name")
    String repoName;
    @SerializedName("description")
    String repoDesc;
    @SerializedName("full_name")
    String repoOwnerName;

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDesc() {
        return repoDesc;
    }

    public void setRepoDesc(String repoDesc) {
        this.repoDesc = repoDesc;
    }

    public String getRepoOwnerName() {
        return repoOwnerName;
    }

    public void setRepoOwnerName(String repoOwnerName) {
        this.repoOwnerName = repoOwnerName;
    }
}
