package com.example.githubtask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HelperClass {
    public static final String TAG = "git_hub";
    public static final String API_URL = "https://api.github.com/users/square/";
    // TODO use your own access token
    public static final String ACCESS_TOKEN = "";


    public static final String REPOS_FILE = "repos_list";
    public static final String LIST_STRING = "list_string";

    public static final String DEF_RETURN_LIST_STRING = "def_return_string_list";
    public static final String NO_INTERNET = "There is NO internet connection";



    /**
     * @param context: context that called the method
     * @return true if there is a internet connection otherwise false
     */
    public static boolean checkInternetState(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else {
            // if device on airplane mode
            return false;
        }
    }

}
