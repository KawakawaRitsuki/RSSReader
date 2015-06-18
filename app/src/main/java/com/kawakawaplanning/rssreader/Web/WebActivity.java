package com.kawakawaplanning.rssreader.Web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.kawakawaplanning.rssreader.R;

/**
 * Created by KP on 15/05/04.
 */
public class  WebActivity extends ActionBarActivity {

    public static String url;
    public ViewPager vp;
    public PAdapter adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        if(intent != null){
            url = intent.getStringExtra("com.kawakawaplanning.rssreader.urlString");
        }

        vp = (ViewPager)findViewById(R.id.mypager);//定義
        adap = new com.kawakawaplanning.rssreader.Web.PAdapter(this.getSupportFragmentManager());
        vp.setAdapter(adap);//アダプタ入れる

        /*

        このURLにかけると、本文のみを返してくれる。
        高速モードには向くかも。。。？

        - http://boilerpipe-web.appspot.com/extract?url=" + url + "&extractor=ArticleExtractor&output=htmlFragment"

        */
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem2:
                Uri uri = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
                return true;
            case R.id.menuitem3:
                String shareBody = NormalWebFragment.getTitle() + "\n" + url + "\n\n #KP_RSSReader";
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
                    intent.setClassName("com.kawakawaplanning.rssreader","com.kawakawaplanning.rssreader.Main.MainActivity");
                    startActivity(intent);
                    finish();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
