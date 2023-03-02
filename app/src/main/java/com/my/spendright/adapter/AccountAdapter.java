package com.my.spendright.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.AccountModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ItemFundBinding;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {
    Context context;
    ArrayList<AccountModel>arrayList;

    public AccountAdapter(Context context, ArrayList<AccountModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFundBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_fund,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          holder.binding.tvAccountNo.setText(arrayList.get(position).getAccountNumber());
          holder.binding.tvBankName.setText(arrayList.get(position).getBankName());

        Log.e("size===",arrayList.size()+"");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemFundBinding binding;
        public MyViewHolder(@NonNull ItemFundBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
