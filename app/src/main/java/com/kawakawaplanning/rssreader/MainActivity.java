package com.kawakawaplanning.rssreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {
    SharedPreferences sharedPref;
    public static Context context;
    public final int MENU_SELECT_A = 0;
    static public PAdapter adap;
    ViewPager vp;
    android.support.v4.app.FragmentManager fragmentManager;

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
    protected void onStart() {
        super.onStart();
        vp = (ViewPager)findViewById(R.id.mypager);//定義
        adap = new PAdapter(this.getSupportFragmentManager());
        vp.setAdapter(adap);//アダプタ入れる
    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, MENU_SELECT_A, 0, "RSS追加");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SELECT_A:
                AlertDialog.Builder alertDialogBuildername = new AlertDialog.Builder(this);
                LayoutInflater inflatername = (LayoutInflater)this.getSystemService(
                        LAYOUT_INFLATER_SERVICE);
                View viewname =  inflatername.inflate(R.layout.dialogday,
                        (ViewGroup)findViewById(R.id.dialogname_layout));

                final EditText nameEt = (EditText)viewname.findViewById(R.id.editText);
                alertDialogBuildername.setTitle("URL追加");//もし返りがおかしかったら。。。
                alertDialogBuildername.setView(viewname);
                alertDialogBuildername.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String name = nameEt.getEditableText().toString();

                                String regex = "http://.*|https://.*";
                                Pattern p = Pattern.compile(regex);
                                Matcher m = p.matcher(name);


                                if (!name.isEmpty()) {
                                    if (m.find()) {
                                        final Handler handler = new Handler();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {

                                                    URLConnection connection = new URL("https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=" + name + "&num=30").openConnection();
                                                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                                                    JSONObject json = new JSONObject(reader.readLine());
                                                    String getTitle = json.getJSONObject("responseData").getJSONObject("feed").getString("title");

                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("URLData", sharedPref.getString("URLData", "1") + "," + name);
                                                    editor.putString("TitleData", sharedPref.getString("TitleData", "1") + "," + getTitle);
                                                    editor.commit();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            vp = (ViewPager) findViewById(R.id.mypager);//定義
                                                            adap = new PAdapter(fragmentManager);
                                                            vp.setAdapter(adap);//アダプタ入れる
                                                            return;
                                                        }
                                                    });

                                                } catch (Exception e) {
                                                }
                                            }
                                        }).start();
                                    } else {
                                        Toast.makeText(context,"URLではありません。",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(context,"文字が入力されていません。",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                alertDialogBuildername.setCancelable(true);
                AlertDialog alertDialogname = alertDialogBuildername.create();
                alertDialogname.show();

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
