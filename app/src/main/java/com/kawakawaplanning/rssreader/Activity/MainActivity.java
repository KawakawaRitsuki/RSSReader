package com.kawakawaplanning.rssreader.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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

        if (sharedPref.getString("URLData", "notfound").equals("notfound")){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString( "URLData",  "http://gigazine.net/news/rss_2.0/");
            editor.putString( "TitleData",  "Gigazine");
            editor.commit();
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
}
