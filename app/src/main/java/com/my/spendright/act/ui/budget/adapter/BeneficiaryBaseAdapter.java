package com.my.spendright.act.ui.budget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.model.BeneficiaryModel;

import java.util.ArrayList;

public class BeneficiaryBaseAdapter extends BaseAdapter {

    Context context;

    LayoutInflater inflter;
    TextView tvAccountName, tvBankName,tvAccountNumber;

    ArrayList<BeneficiaryModel.Datum> arrayList;

    public BeneficiaryBaseAdapter(Context applicationContext, ArrayList<BeneficiaryModel.Datum> arrayList) {
        this.context = applicationContext;
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
        view = inflter.inflate(R.layout.item_beneficiary, null);
        tvAccountName = (TextView) view.findViewById(R.id.tvAccountName);
        tvBankName = (TextView) view.findViewById(R.id.tvBankName);
        tvAccountNumber = (TextView) view.findViewById(R.id.tvAccountNumber);
        tvAccountName.setText(arrayList.get(i).getBankAccountName());

      if(!arrayList.get(i).getBankName().equalsIgnoreCase("")) {
          tvBankName.setVisibility(View.VISIBLE);
          tvBankName.setText(arrayList.get(i).getBankName());
      }else {
          tvBankName.setVisibility(View.GONE);
      }

        if(!arrayList.get(i).getBankNumber().equalsIgnoreCase("")) {
            tvAccountNumber.setVisibility(View.VISIBLE);
            tvAccountNumber.setText(". " + arrayList.get(i).getBankNumber());
        }else {
            tvAccountNumber.setVisibility(View.GONE);
        }





        return view;

    }
}
