package com.kawakawaplanning.rssreader.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
                return true;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Bundle bundle = data.getExtras();
        Toast.makeText(this,bundle.getString("key.StringData"),Toast.LENGTH_SHORT).show();
        if (resultCode != RESULT_CANCELED){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("確認");
            alertDialogBuilder.setMessage("このニュースフィードを追加しますか？");
            alertDialogBuilder.setPositiveButton("はい",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println(bundle.getString("key.StringData"));
                        }
                    });
            alertDialogBuilder.setNegativeButton("いいえ",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            // アラートダイアログのキャンセルが可能かどうかを設定します
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            // アラートダイアログを表示します
            alertDialog.show();
        }else{
            finish();
        }
    }
}
