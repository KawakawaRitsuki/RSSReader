package com.kawakawaplanning.rssreader.Async;

import android.os.AsyncTask;
import android.text.Html;
import android.widget.ArrayAdapter;

import com.kawakawaplanning.rssreader.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by KP on 15/05/04.
 */
public class GetNewsTask extends AsyncTask {
    JSONArray items;
    JSONObject titleGet;
    String searchUrl;

    static int i = 0;

    ArrayAdapter<Item> adapter;

    public GetNewsTask(String url, ArrayAdapter<Item> adapter){
        searchUrl = url;
        this.adapter= adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Object... params) {
    String get = null;
    try {
        URLConnection connection = new URL("https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=" + searchUrl + "&num=30").openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

        get = reader.readLine();
    }catch (IOException e) {

    }

        return get;
    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        try {
            JSONObject json = new JSONObject(o.toString());
            items = json.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries");
            titleGet = json.getJSONObject("responseData").getJSONObject("feed");

            i++;
        }catch (JSONException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            
        }
        adapter.clear();

        for(int i = 0; i != 29;i++) {
        try{
            String str = items.getJSONObject(i).getString("content").replace("\n","").replace(" ", "").replace("<p>","").replace("</p>","");
            CharSequence cs = Html.fromHtml(str);
            adapter.add(new Item(items.getJSONObject(i).getString("title"),cs,items.getJSONObject(i).getString("link")));
        }catch (JSONException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
    }

}
