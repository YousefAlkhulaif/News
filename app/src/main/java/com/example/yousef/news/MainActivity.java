package com.example.yousef.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {
    private static final String JSON_URL = "https://content.guardianapis.com/search?&show-tags=contributor&api-key=5cd855b0-9da1-4366-bc2e-bd2f7f2836fd";
    public static boolean flag = false;
    public ArrayList<Story> news = new ArrayList<>();
    MyAdapter adapter;
    RecyclerView recyclerView;
    TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isNetworkOnline()) {
            getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            Toast.makeText(this, R.string.txt_noInternet, Toast.LENGTH_SHORT).show();
        }
        final Button btn_refresh = findViewById(R.id.btn_refresh);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(this, news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), 1
        );
        dividerItemDecoration.setOrientation(DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkOnline()) {

                    getSupportLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();
                } else {
                    Toast.makeText(MainActivity.this, R.string.txt_noInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                return true;
            case R.id.about:
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public Loader<List<Story>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.getString(getString(R.string.settings_min_orderBy_key), getString(R.string.settings_min_orderBy_default));
        String numOfStories = sharedPreferences.getString(getString(R.string.settings_min_page_size_key), "5");
        String queriedUrl;
        Uri uri = Uri.parse(JSON_URL);
        Uri.Builder uriBuilder = uri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.settings_min_orderBy_key), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.settings_min_page_size_key), numOfStories);
        queriedUrl = uriBuilder.toString();
        return new MyLoader(MainActivity.this, queriedUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Story>> loader, List<Story> data) {
        errorMsg = findViewById(R.id.txt_noNews);
        if (data.isEmpty()) {
            errorMsg.setVisibility(View.VISIBLE);
        } else {
            errorMsg.setVisibility(View.INVISIBLE);
        }
        adapter = new MyAdapter(this, data);
        recyclerView.setAdapter(adapter);
        if (flag) {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
            flag = false;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Story>> loader) {

    }

    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }
}
