package com.my.spendright.airetime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.spendright.R;
import com.my.spendright.airetime.model.GetInternationalModel;
import com.my.spendright.airetime.model.GetProductTypeModel;

import java.util.ArrayList;


public class ServicesProductTypeAdapter extends BaseAdapter {

    Context context;
    String[] countryNames;
    LayoutInflater inflter;
    TextView countrycode;
    ImageView icon;
    ArrayList<GetProductTypeModel.Content> code;

    public ServicesProductTypeAdapter(Context applicationContext, ArrayList<GetProductTypeModel.Content> code) {
        this.context = applicationContext;
        this.code = code;
        inflter = (LayoutInflater.from(applicationContext));
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
        view = inflter.inflate(R.layout.spinner_account_product, null);
        //icon = (ImageView) view.findViewById(R.id.img_flag);
       countrycode = (TextView) view.findViewById(R.id.textview);
        // icon.setImageResource(flags[i]);
      //countrycode.setText(code[i]);
       countrycode.setText(code.get(i).getName());

        return view;

    }
}
