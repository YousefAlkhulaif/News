package com.example.yousef.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MyLoader extends AsyncTaskLoader<List<Story>> {
    List<Story> news = new ArrayList<>();
    private String jsonUrl;

    MyLoader(@NonNull Context context, String jsonUrl) {
        super(context);
        this.jsonUrl = jsonUrl;
    }

    @Nullable
    @Override
    public List<Story> loadInBackground() {
        try {
            URL url = new URL(jsonUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.connect();
            InputStream input = connection.getInputStream();
            String line;
            InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            JSONObject root = new JSONObject(output.toString());
            JSONObject response = root.getJSONObject(getContext().getResources().getString(R.string.response_key));
            jsonDetails(response);
        } catch (IOException e) {
            e.printStackTrace();
            MainActivity.flag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    private void jsonDetails(JSONObject json) throws JSONException {
        news = new ArrayList<>();
        JSONArray JsonNews = json.getJSONArray(getContext().getResources().getString(R.string.results_key));
        for (int i = 0; i < JsonNews.length(); i++) {
            JSONObject toExtractFrom = (JsonNews.getJSONObject(i));
            String id = toExtractFrom.getString(getContext().getResources().getString(R.string.id_key));
            String type = toExtractFrom.getString(getContext().getResources().getString(R.string.type_key));
            String sectionName = toExtractFrom.getString(getContext().getResources().getString(R.string.section_name_key));
            String date = toExtractFrom.getString(getContext().getResources().getString(R.string.date_key));
            String title = toExtractFrom.getString(getContext().getResources().getString(R.string.web_title_key));
            String webUrl = toExtractFrom.getString(getContext().getResources().getString(R.string.web_url_key));
            JSONArray authors = toExtractFrom.getJSONArray(getContext().getResources().getString(R.string.tags_key));
            String author = "";
            if (authors.length() > 0) {
                author = authors.getJSONObject(0).getString(getContext().getResources().getString(R.string.web_title_key));
            }
            Story oneStoryToAdd = new Story(id, type, sectionName, date, title, webUrl, author);
            news.add(oneStoryToAdd);

        }
    }

}

