package com.kawakawaplanning.rssreader.Web;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kawakawaplanning.rssreader.Main.MainActivity;

public class PAdapter extends FragmentPagerAdapter {

    SharedPreferences sharedPref;

    public PAdapter(FragmentManager fm) {
        super(fm);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return NormalWebFragment.newInstance();
            case 1:return FastWebFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:return "標準モード";
            case 1:return "高速モード";
        }
        return null;
    }
}