package com.kawakawaplanning.rssreader.Main;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PAdapter extends FragmentPagerAdapter {

    SharedPreferences sharedPref;

    public PAdapter(FragmentManager fm) {
        super(fm);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println(position + "," + sharedPref.getString("URLData", "").split(",")[position]);
        return MainFragment.newInstance(sharedPref.getString("URLData", "").split(",")[position]);
    }

    @Override
    public int getCount() {
        return sharedPref.getString("URLData", "null").split(",").length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return sharedPref.getString("TitleData", "").split(",")[position];
    }
}