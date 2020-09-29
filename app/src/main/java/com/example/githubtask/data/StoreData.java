package com.example.githubtask.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.githubtask.data.model.GitHubRepoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.githubtask.HelperClass.DEF_RETURN_LIST_STRING;
import static com.example.githubtask.HelperClass.LIST_STRING;
import static com.example.githubtask.HelperClass.REPOS_FILE;
import static com.example.githubtask.HelperClass.TAG;

public class StoreData {

    /**
     * save data locally in device
     * @param gitHubRepoModels list that you want to save
     * @param context  which call the method
     */
    public void saveListRepos(List<GitHubRepoModel> gitHubRepoModels, Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences(REPOS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String jsonListString = gson.toJson(gitHubRepoModels);
        prefsEditor.putString(LIST_STRING, jsonListString);
        prefsEditor.apply();
        Log.i(TAG, "## StoreData ## -- saveListRepos() >> listSaved+size >> " + gitHubRepoModels.size());

    }

    /**
     * get that data form SharedPreferences
     * @param context  which call the method
     * @return list of {@link GitHubRepoModel}
     */
    public List<GitHubRepoModel> getSavedRepos(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences(REPOS_FILE, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonListString = mPrefs.getString(LIST_STRING, DEF_RETURN_LIST_STRING);
        // if true that means the storage has no data so return a empty array
        if (jsonListString.equals(DEF_RETURN_LIST_STRING)) {
            return new ArrayList<>() ;
        }
        // because the return is a listOf object {GitHubRepoModel}
        Type type = new TypeToken<List<GitHubRepoModel>>() {}.getType();
        List<GitHubRepoModel> repos = gson.fromJson(jsonListString, type);
        if (repos != null) {
            Log.i(TAG, "## StoreData ## --  getSavedRepos() listSize >> " + repos.size());
        } else {
            Log.i(TAG, "## StoreData ## --  getSavedRepos() jsonListString >> " + jsonListString);
        }
        return repos;
    }

    /**
     * remove data from SharedPreferences in order to get a new data
     * @param context  which call the method
     */
    public void removeData(Context context) {
        SharedPreferences settings = context.getSharedPreferences(REPOS_FILE, Context.MODE_PRIVATE);
        settings.edit().remove(LIST_STRING).apply();
    }


}
