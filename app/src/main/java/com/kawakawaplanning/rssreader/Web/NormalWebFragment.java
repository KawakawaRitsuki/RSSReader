package com.kawakawaplanning.rssreader.Web;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kawakawaplanning.rssreader.R;

public class NormalWebFragment extends Fragment {

    private static WebView webView;
    public ProgressBar progressBar;

    public NormalWebFragment(){}

    public static Fragment newInstance(){
        NormalWebFragment fragment = new NormalWebFragment();

        return fragment;
    }

    public static String getTitle(){
        return webView.getTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_web_normal, container, false);
        webView = (WebView)v.findViewById(R.id.webView);
        progressBar = (ProgressBar) v.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

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
        webView.loadUrl(WebActivity.url);
        return v;

    }

    public final class ViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view , String url){
            //ロード完了時にやりたい事を書く
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
