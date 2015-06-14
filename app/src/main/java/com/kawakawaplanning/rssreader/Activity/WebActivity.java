package com.kawakawaplanning.rssreader.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kawakawaplanning.rssreader.R;

/**
 * Created by KP on 15/05/04.
 */
public class WebActivity extends ActionBarActivity {

    public WebView webView;
    public String url;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView)findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if(intent != null){
            url = intent.getStringExtra("com.kawakawaplanning.rssreader.urlString");
        }
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new ViewClient());
        webView.loadUrl(url);

        /*

        このURLにかけると、本文のみを返してくれる。
        高速モードには向くかも。。。？

        - http://boilerpipe-web.appspot.com/extract?url=" + url + "&extractor=ArticleExtractor&output=htmlFragment"

        */
    }
    public final class ViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view , String url){
            //ロード完了時にやりたい事を書く
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, 0, 0, "ブラウザで開く");
        menu.add(0, 1, 0, "共有...");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Uri uri = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
                return true;
            case 1:
                String shareBody = webView.getTitle() + "\n" + url + "\n\n RSSReader -KawakawaPlanning-";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "シェア"));
                break;
        }
        return false;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    Intent intent=new Intent();
                    intent.setClassName("com.kawakawaplanning.rssreader","com.kawakawaplanning.rssreader.Activity.MainActivity");
                    startActivity(intent);
                    finish();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
