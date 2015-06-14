package com.kawakawaplanning.rssreader.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kawakawaplanning.rssreader.R;
import com.kawakawaplanning.rssreader.TouchListView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends ActionBarActivity implements View.OnClickListener{

    public SharedPreferences sharedPref;
    public String TitleData[];
    public String URLData[];
    public TouchListView lv;
    public Button commitBtn;
    public EditText et;
    public static Context context;
    private ArrayList<String> array;
    private IconicAdapter adapter=null;
    private ArrayList<String> urlArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);



        context = this;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        TitleData = sharedPref.getString("TitleData", "").split(",");
        URLData = sharedPref.getString("URLData", "").split(",");
        lv = (TouchListView)findViewById(R.id.list);
        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);

        setList();


        commitBtn = (Button)findViewById(R.id.commitBtn);
        commitBtn.setOnClickListener(this);

        et = (EditText)findViewById(R.id.commitEt);
    }

    private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            String title = adapter.getItem(from);
            String url = urlArray.get(from);

            adapter.remove(title);
            adapter.insert(title, to);

            urlArray.remove(from);
            urlArray.add(to,url);

            StringBuffer sb = new StringBuffer();
            StringBuffer sb2 = new StringBuffer();

            for(int i = 0 ;i < adapter.getCount();i++){
                if(i == 0){
                    sb.append(adapter.getItem(i));
                    sb2.append(urlArray.get(i));

                }else{
                    sb.append("," + adapter.getItem(i));
                    sb2.append("," + urlArray.get(i));
                }
            }
            System.out.println(sb.toString());
            System.out.println(sb2.toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString( "URLData",  sb2.toString());
            editor.putString("TitleData", sb.toString());
            editor.commit();
        }
    };

    private TouchListView.RemoveListener onRemove = new TouchListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(adapter.getItem(which));
            urlArray.remove(which);

            StringBuffer sb = new StringBuffer();
            StringBuffer sb2 = new StringBuffer();

            for(int i = 0 ;i < adapter.getCount();i++){
                if(i == 0){
                    sb.append(adapter.getItem(i));
                    sb2.append(urlArray.get(i));

                }else{
                    sb.append("," + adapter.getItem(i));
                    sb2.append("," + urlArray.get(i));
                }
            }
            System.out.println(sb.toString());
            System.out.println(sb2.toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString( "URLData",  sb2.toString());
            editor.putString("TitleData", sb.toString());
            editor.commit();
        }
    };

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
        array=new ArrayList<String>(Arrays.asList(TitleData));
        urlArray = new ArrayList<String>(Arrays.asList(URLData));
        adapter=new IconicAdapter();
        lv.setAdapter(adapter);
    }
    class IconicAdapter extends ArrayAdapter<String> {
        IconicAdapter() {
            super(EditActivity.context, R.layout.row2, array);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();

                row = inflater.inflate(R.layout.row2, parent, false);
            }

            TextView label = (TextView) row.findViewById(R.id.label);

            label.setText(array.get(position));

            return (row);
        }
    }
}
