package com.my.spendright.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.spendright.R;


public class WeekdaysDaysAdapter extends BaseAdapter {

    Context context;
    String[] DaysNames;
    LayoutInflater inflter;
    TextView countrycode;
    ImageView icon;
    String[] code;
   // ArrayList<DayDaysModel> code;

    public WeekdaysDaysAdapter(Context applicationContext, String[] code) {
        this.context = applicationContext;
        this.code = code;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return code.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_day_days, null);
        //icon = (ImageView) view.findViewById(R.id.img_flag);
       countrycode = (TextView) view.findViewById(R.id.textview);
        // icon.setImageResource(flags[i]);
      //countrycode.setText(code[i]);
       countrycode.setText(code[i]+"");


        return view;

    }
}

