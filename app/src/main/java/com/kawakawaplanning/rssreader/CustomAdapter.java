package com.kawakawaplanning.rssreader;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by KP on 15/05/04.
 */
public class CustomAdapter extends ArrayAdapter<Item> {

    LayoutInflater mInflater;
    int mResId;
    int mAnimatedPosition = ListView.INVALID_POSITION;

    public CustomAdapter(Context context, int resource) {
        super(context, 0);
        mResId = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResId, parent, false);
        }

        Item item = getItem(position);

        TextView titleTv = (TextView) convertView.findViewById(R.id.title);
        titleTv.setText(item.title);
        TextView textTv = (TextView) convertView.findViewById(R.id.text);
        textTv.setText(item.text);

        // まだ表示していない位置ならアニメーションする
        if (mAnimatedPosition < position) {
            // XMLからアニメーターを作成
            Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.card_slide_in);
            // アニメーションさせるビューをセット
            animator.setTarget(convertView);
            // アニメーションを開始
            animator.start();
            mAnimatedPosition = position;
        }

        return convertView;
    }


}