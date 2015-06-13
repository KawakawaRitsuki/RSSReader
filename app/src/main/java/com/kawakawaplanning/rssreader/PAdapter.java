package com.kawakawaplanning.rssreader;

/**
 * Created by KP on 15/05/05.
 */

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by KP on 2015/03/25.
 */
public class PAdapter extends FragmentPagerAdapter {

    SharedPreferences sharedPref;

    public PAdapter(FragmentManager fm) {
        super(fm);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
    }

    @Override
    public Fragment getItem(int position) {
//        switch(position){
//            case 0:
//                return MainFragment.newInstance("http://gigazine.net/news/rss_2.0/");
//            case 1:
//                return MainFragment.newInstance("http://feeds.gizmodo.jp/rss/gizmodo/index.xml");
//            case 2:
//                return MainFragment.newInstance("http://feeds.lifehacker.jp/rss/lifehacker/index.xml");
//            case 3:
//                return MainFragment.newInstance("http://y-anz-m.blogspot.com/feeds/posts/default");
//            case 4:
//                return MainFragment.newInstance("http://kyoko-np.net/index.xml");
//        }


        String getStr[] = sharedPref.getString("URLData", "").split(",");

//        Toast.makeText(MainActivity.context,sharedPref.getString("URLData", "") + position,Toast.LENGTH_SHORT).show();

        return MainFragment.newInstance(getStr[position]);

    }

    @Override
    public int getCount() {
        String getStr[] = sharedPref.getString("URLData", "null").split(",");

        return getStr.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String getStr[] = sharedPref.getString("TitleData", "").split(",");

//        Toast.makeText(MainActivity.context,sharedPref.getString("TitleData", "") + position,Toast.LENGTH_SHORT).show();

        return getStr[position];
    }
}