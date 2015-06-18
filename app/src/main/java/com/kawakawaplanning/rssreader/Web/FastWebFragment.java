package com.kawakawaplanning.rssreader.Web;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kawakawaplanning.rssreader.R;

public class FastWebFragment extends Fragment {

    public ProgressBar progressBar;
    private TextView titleTv;
    private TextView contentTv;

//    public FastWebFragment(){}

    public static Fragment newInstance(){
        FastWebFragment fragment = new FastWebFragment();

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_web_fast, container, false);
        progressBar = (ProgressBar) v.findViewById (R.id.progress);
        titleTv =     (TextView)    v.findViewById (R.id.titleTv);
        contentTv =   (TextView)    v.findViewById (R.id.contentTv);

        return v;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GetFastDataTask task = new GetFastDataTask(WebActivity.url,progressBar,titleTv,contentTv);
        task.execute();
    }

}
