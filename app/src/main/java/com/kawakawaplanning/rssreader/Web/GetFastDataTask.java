package com.kawakawaplanning.rssreader.Web;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetFastDataTask extends AsyncTask {
    private String title;
    private String content;
    private String status;

    private String url;

    private TextView titleTv;
    private TextView contentTv;

    ProgressBar progressBar;

    public GetFastDataTask(String url,ProgressBar progressBar,TextView titleTv,TextView contentTv){
        this.url         = url;
        this.progressBar = progressBar;
        this.titleTv     = titleTv;
        this.contentTv   = contentTv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Object... params) {
        String get = null;
        try {
            URLConnection connection = new URL("http://boilerpipe-web.appspot.com/extract?url=" + url + "&extractor=ArticleExtractor&output=json&extractImages=").openConnection();
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
            title = json.getJSONObject("response").getString("title");
            content = json.getJSONObject("response").getString("content");
            status = json.getString("status");

            if(status.equals("success")){
                titleTv.setText(title);
                contentTv.setText(content);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (NullPointerException e){

        }
//        progressBar.setVisibility(View.INVISIBLE);
    }

}
