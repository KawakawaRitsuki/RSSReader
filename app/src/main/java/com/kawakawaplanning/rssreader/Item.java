package com.kawakawaplanning.rssreader;

/**
 * Created by KP on 15/05/04.
 */
public class Item {


    public String title;
    public CharSequence text;
    public String url;

    public Item(String title, CharSequence text, String url) {
        super();

        this.title = title;
        this.text = text;
        this.url = url;
    }
}