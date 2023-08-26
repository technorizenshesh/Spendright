package com.my.spendright.act.ui.budget.withdraw.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.my.spendright.R;
import com.my.spendright.act.ui.budget.withdraw.model.UserSuggModel;

import java.util.ArrayList;
import java.util.List;

public class UserArrayAdapter extends ArrayAdapter<UserSuggModel.Result> {
    private final Context mContext;
    private final List<UserSuggModel.Result> arrayList;
    private final int mLayoutResourceId;

    public UserArrayAdapter(Context context, int resource, List<UserSuggModel.Result> arrayList) {
        super(context, resource, arrayList);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.arrayList = arrayList;

    }

    public int getCount() {
        return arrayList.size();
    }



    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.textView1);
            name.setText(arrayList.get(position).getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


}
