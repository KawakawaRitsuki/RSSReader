package com.kawakawaplanning.rssreader.Activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kawakawaplanning.rssreader.PAdapter;
import com.kawakawaplanning.rssreader.R;


public class MainActivity extends ActionBarActivity {
    SharedPreferences sharedPref;
    public static Context context;
    public final int MENU_SELECT_A = 0;
    static public PAdapter adap;
    ViewPager vp;
    android.support.v4.app.FragmentManager fragmentManager;

    /*

        テスト用URL

    - http://gigazine.net/news/rss_2.0/
    - http://feeds.gizmodo.jp/rss/gizmodo/index.xml
    - http://feeds.lifehacker.jp/rss/lifehacker/index.xml
    - http://y-anz-m.blogspot.com/feeds/posts/default
    - http://kyoko-np.net/index.xml

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        fragmentManager = this.getSupportFragmentManager();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPref.getString("URLData", "notfound").equals("notfound")){
            findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
            findViewById(R.id.relative).setVisibility(View.INVISIBLE);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("resume");
        vp = null;
        adap = null;

        vp = (ViewPager)findViewById(R.id.mypager);//定義
        adap = new PAdapter(this.getSupportFragmentManager());
        vp.setAdapter(adap);//アダプタ入れる

        if(!netWorkCheck(this)){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("ネットワークエラー");
            alertDialogBuilder.setMessage("ネットワークエラーが発生しました。インターネットの接続状態を確認して、再読み込み（上から下にスライド）してください。");
            alertDialogBuilder.setPositiveButton("OK", null);
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, MENU_SELECT_A, 0, "RSS編集");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SELECT_A:
                Intent intent=new Intent();
                intent.setClassName("com.kawakawaplanning.rssreader","com.kawakawaplanning.rssreader.Activity.EditActivity");
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
    public static boolean netWorkCheck(Context context){
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }
    public void browser(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, "RSS おすすめ");
        startActivity(intent);
    }
}
