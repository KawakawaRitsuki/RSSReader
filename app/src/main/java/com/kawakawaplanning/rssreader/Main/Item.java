package com.kawakawaplanning.rssreader.Main;

/**
 * Created by KP on 15/05/04.
 */
public class Item {


    public String title;
    public CharSequence text;
    public String url;


    /*

    - title -> 項目のタイトル
    - text  -> 項目の説明
    - url   -> 項目選択時のURL

     */

    public Item(String title, CharSequence text, String url) {
        super();

        this.title = title;
        this.text = text;
        this.url = url;
    }
}