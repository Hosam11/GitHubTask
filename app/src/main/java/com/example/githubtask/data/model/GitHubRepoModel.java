package com.example.githubtask.data.model;

import com.google.gson.annotations.SerializedName;


public class GitHubRepoModel {

    @SerializedName("name")
    String repoName;
    @SerializedName("description")
    String repoDesc;
    @SerializedName("html_url")
    String repoURL;
    @SerializedName("owner")
    OwnerModel ownerModel;

    boolean fork;

    public OwnerModel getOwnerModel() {
        return ownerModel;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoDesc() {
        return repoDesc;
    }

    public boolean isFork() {
        return fork;
    }

    public static class OwnerModel {
        // FixME "Hint" i CAN NOT find the username of owner so i used login instead
        String login;
        @SerializedName("html_url")
        String ownerURL;

        public String getLogin() {
            return login;
        }

        public String getOwnerURL() {
            return ownerURL;
        }
    }

}
