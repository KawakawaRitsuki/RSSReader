package com.kawakawaplanning.rssreader.Main;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kawakawaplanning.rssreader.R;


public class MainFragment extends Fragment {

    public CustomAdapter adapter;
    public ListView listView;
    public String searchUrl;

    public MainFragment(){}

    public static Fragment newInstance(String url){
        MainFragment fragment = new MainFragment();

        fragment.searchUrl = url;

        Bundle bundle = new Bundle();
        bundle.putString("URL",url);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView)v.findViewById(R.id.listView);
        adapter = new CustomAdapter(getActivity(),
                R.layout.card_item);

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (netWorkCheck(getActivity())) {
            GetNewsTask task = new GetNewsTask(searchUrl, adapter);
            task.execute();
        }

        listView.setDivider(null);
        listView.setVerticalScrollBarEnabled(false);
        listView.setSelector(android.R.color.transparent);
        listView.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.card_footer, listView, false));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClassName("com.kawakawaplanning.rssreader","com.kawakawaplanning.rssreader.Web.WebActivity");
                intent.putExtra("com.kawakawaplanning.rssreader.urlString", adapter.getItem(position).url);
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);
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
}
