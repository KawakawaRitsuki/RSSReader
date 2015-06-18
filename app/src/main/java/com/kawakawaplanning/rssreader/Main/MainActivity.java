package com.kawakawaplanning.rssreader.Main;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kawakawaplanning.rssreader.Other.OpenSourceLicenseActivity;
import com.kawakawaplanning.rssreader.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {
    SharedPreferences sharedPref;
    public static Context context;
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
            findViewById(R.id.relative) .setVisibility(View.INVISIBLE);
        }

        vp = (ViewPager)findViewById(R.id.mypager);//定義
        adap = new PAdapter(this.getSupportFragmentManager());
        vp.setAdapter(adap);//アダプタ入れる
    }

    @Override
    protected void onResume() {
        super.onResume();

        check();
        adap.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem1:
                Intent intent=new Intent();
                intent.setClassName("com.kawakawaplanning.rssreader", "com.kawakawaplanning.rssreader.Edit.EditActivity");
                startActivity(intent);
                finish();
                break;
            case R.id.menuitem4:
                check();
                break;
            case R.id.menuitem5:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = (LayoutInflater)this.getSystemService(
                        LAYOUT_INFLATER_SERVICE);
                View view =  inflater.inflate(R.layout.opensourcelicense,
                        (ViewGroup)findViewById(R.id.rootLayout));
                setSpannableString(view);
                alertDialogBuilder.setTitle("オープンソースライセンス");
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialogBuilder.setCancelable(true);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
        return false;
    }
    private void setSpannableString(View view) {

        String message = "このソフトウェアは、オープンソースソフトウェアによって実現しました。";

        // リンク化対象の文字列、リンク先 URL を指定する
        Map<String, String> map = new HashMap<String, String>();
        map.put("オープンソースソフトウェア", "");

        // SpannableString の取得
        SpannableString ss = createSpannableString(message, map);

        // SpannableString をセットし、リンクを有効化する
        TextView textView = (TextView) view.findViewById(R.id.textView19);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private SpannableString createSpannableString(String message, Map<String, String> map) {

        SpannableString ss = new SpannableString(message);

        for (final Map.Entry<String, String> entry : map.entrySet()) {
            int start = 0;
            int end = 0;

            // リンク化対象の文字列の start, end を算出する
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
                break;
            }

            // SpannableString にクリックイベント、パラメータをセットする
            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent intent = new Intent(getApplicationContext(), OpenSourceLicenseActivity.class);
                    startActivity(intent);
                }
            }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return ss;
    }
    public boolean check(){
        if(!netWorkCheck(this)){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("ネットワークエラー");
            alertDialogBuilder.setMessage("ネットワークエラーが発生しました。インターネットの接続状態を確認して、メニューから再読み込みをしてください");
            alertDialogBuilder.setPositiveButton("OK", null);
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return netWorkCheck(this);
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
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(50);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, "RSS おすすめ");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
