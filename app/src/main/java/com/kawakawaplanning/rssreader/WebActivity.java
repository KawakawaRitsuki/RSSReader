package com.kawakawaplanning.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by KP on 15/05/04.
 */
public class WebActivity extends ActionBarActivity {

    public WebView webView;
    public  String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView)findViewById(R.id.webView);
        Intent intent = getIntent();
        if(intent != null){
            url = intent.getStringExtra("com.kawakawaplanning.rssreader.urlString");
        }
//        webView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);

//        webView.getSettings().setTextZoom(150);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(/*"http://boilerpipe-web.appspot.com/extract?url=" + */ url /* + "&extractor=ArticleExtractor&output=htmlFragment"*/);
    }
}
