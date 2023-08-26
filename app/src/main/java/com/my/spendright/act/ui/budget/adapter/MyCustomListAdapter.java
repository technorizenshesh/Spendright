package com.my.spendright.act.ui.budget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.spendright.R;

public class MyCustomListAdapter extends BaseAdapter {

    private boolean userAlreadyCliced;
    private String [] stringTexts;
    private float [] progressBarValues;

    public MyCustomListAdapter() {
        userAlreadyCliced = false;
    }

    public void setIfUserAlreadyClickedOption(boolean clicked) {
        userAlreadyCliced = clicked;
    }

    public void setOptions(String  [] text) {
        stringTexts = text;
    }

    public void setProgressBarValues(float [] values) {
        progressBarValues = values;
    }

    @Override
    public int getCount() {
        return stringTexts.length;
    }

    @Override
    public Object getItem(int position) {
        return stringTexts[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parentViewGroup) {
        if(view == null) {
            view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.list_view_each_row, parentViewGroup, false);
        }

        if(userAlreadyCliced) {
            // Hide Text
            view.findViewById(R.id.progress_view).setVisibility(View.VISIBLE);

            // Show Text and set progress
            ((LinearLayout.LayoutParams) view.findViewById(R.id.progress_view).getLayoutParams()).weight = progressBarValues[position];
            view.findViewById(R.id.text_view).setVisibility(View.GONE);
        } else {
            // Hide Progress
            view.findViewById(R.id.progress_view).setVisibility(View.GONE);

            // Show and set text
            view.findViewById(R.id.text_view).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.text_view)).setText(stringTexts[position]);
        }
        return view;
    }
}