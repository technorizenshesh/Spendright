package com.my.spendright.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.R;

import java.util.ArrayList;


public class CategoryAdapterNewFilter extends BaseAdapter {

    Context context;
    String[] countryNames;
    LayoutInflater inflter;
    TextView countrycode;
    ImageView icon;
    ArrayList<GetCategoryModelNew.Result> code;

    public CategoryAdapterNewFilter(Context context, ArrayList<GetCategoryModelNew.Result> code) {
        this.context = context;
        this.code = code;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return code.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_account_category, null);
        //icon = (ImageView) view.findViewById(R.id.img_flag);
       countrycode = (TextView) view.findViewById(R.id.textview);
        // icon.setImageResource(flags[i]);
      //countrycode.setText(code[i]);
       countrycode.setText(code.get(i).getHolderName());

        return view;

    }
}
