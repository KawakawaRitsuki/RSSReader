package com.kawakawaplanning.rssreader.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kawakawaplanning.rssreader.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends ActionBarActivity implements View.OnClickListener{

    public SharedPreferences sharedPref;
    public String TitleDatas[];
    public ListView lv;
    public Button commitBtn;
    public EditText et;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        TitleDatas = sharedPref.getString("TitleData", "").split(",");

        lv = (ListView)findViewById(R.id.editLv);
        setList();


        commitBtn = (Button)findViewById(R.id.commitBtn);
        commitBtn.setOnClickListener(this);

        et = (EditText)findViewById(R.id.commitEt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commitBtn:
                commit();
                setList();
                break;
        }
    }

    public void commit(){
        final String name = et.getEditableText().toString();

        String regex = "http://.*|https://.*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);

        if (!name.isEmpty()) {//入力されているかどうかの判断
            if (m.find()) {//入力された文字列がURLの形式かどうかの判断

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

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                Toast.makeText(this, "URLではありません。", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"文字が入力されていません。",Toast.LENGTH_SHORT).show();
        }
    }

    public void setList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, TitleDatas);
        lv.setAdapter(adapter);
    }

}
