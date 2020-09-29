package com.example.githubtask;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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
import java.util.Timer;
import java.util.TimerTask;

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

    final Handler handler = new Handler();
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<GitHubRepoModel> gitHubRepoModels = new ArrayList<>();
    int page = 1;
    int limit = 10;
    GitHubAdapter gitHubAdapter;
    SwipeRefreshLayout refreshLayout;
    Timer timer;
    TimerTask timerTask;
    StoreData storeData;
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
        storeData = new StoreData();

        // check internet connection if exist get data from api else from cache
        if (HelperClass.checkInternetState(this)) {
            getData(page, limit, false);
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
                            getData(page, limit, false);
                        } else {
                            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // on swipe refresh see if there is internet connection  get a new data
        refreshLayout.setOnRefreshListener(() -> {
            if (HelperClass.checkInternetState(this)) {
                callNewData();
            } else {
                Toast.makeText(this, NO_INTERNET, Toast.LENGTH_SHORT).show();
            }
            refreshLayout.setRefreshing(false);
        });
    }

    /**
     * get data from api
     *
     * @param page            the number of page that you want to return
     * @param limit           the number that you want limit the page to
     * @param isSaveToStorage the flag that tell us whether is method call came from
     *                        refresh cache or else so decide  save locally
     */
    private void getData(int page, int limit, boolean isSaveToStorage) {
        InitializeRetrofitVars();
        Call<List<GitHubRepoModel>> repoModelCall = gitHubAPIInterface.getRepsPages(page,
                limit, ACCESS_TOKEN);
        repoModelCall.enqueue(new Callback<List<GitHubRepoModel>>() {

            @Override
            public void onResponse(Call<List<GitHubRepoModel>> call, Response<List<GitHubRepoModel>> response) {
                if (response.body() != null) {
                    Log.i(TAG, "onResponse: response.isSuccessful() >> " + response.isSuccessful()
                            + " -- size of array >> " + response.body().size() +
                            " - page >> " + page);
                }
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    if (response.body().size() != 0) {
                        gitHubAdapter.setGitHubList(response.body());
                        if (isSaveToStorage) {
                            storeData.saveListRepos(gitHubRepoModels, MainActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepoModel>> call, Throwable t) {

            }
        });

    }

    /**
     * Initialize variables needed in retrofit
     */
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

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    /**
     * schedule a task every 1 hour
     */
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 3600000ms the TimerTask will run every 3600000ms = 1 Hour
        timer.schedule(timerTask, 3600000, 3600000);
    }

    /**
     * initializeTimerTask to get a new data and display notification
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(() -> {
                    if (HelperClass.checkInternetState(MainActivity.this)) {
                        callNewData();
                    }
                    Log.i(TAG, "## MainActivity ## >> run() TimerTask");
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, "1")
                            .setContentTitle("Data Updated")
                            .setContentText("data Updated Successfully")
                            .setSmallIcon(R.drawable.ic_launcher_background);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());
                    }

                });
            }
        };
    }

    /**
     * reset the page number to default , clear the data from SharedPreferences
     * also clear the list that is on the adapter finally get a new data from api
     */
    private void callNewData() {
        page = 1;
        storeData.removeData(MainActivity.this);
        gitHubRepoModels.clear();
        gitHubAdapter.notifyDataSetChanged();
        getData(page, limit, true);
    }
}
