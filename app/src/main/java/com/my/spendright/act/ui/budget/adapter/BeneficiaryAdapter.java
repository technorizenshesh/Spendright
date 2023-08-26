package com.my.spendright.act.ui.budget.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.R;
import com.my.spendright.act.ui.budget.BudgetGrpAct;
import com.my.spendright.act.ui.budget.listener.onGrpListener;
import com.my.spendright.act.ui.budget.model.BeneficiaryModel;
import com.my.spendright.act.ui.budget.model.BudgetGrpModel;
import com.my.spendright.databinding.ItemBeneficiaryBinding;
import com.my.spendright.databinding.ItemBudgetGroupBinding;

import java.util.ArrayList;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.MyViewHolder> {
    Context context;
    ArrayList<BeneficiaryModel.Datum> arrayList;
    onGrpListener listener;
    public BeneficiaryAdapter(Context context, ArrayList<BeneficiaryModel.Datum>arrayList,onGrpListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBeneficiaryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_beneficiary,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvAccountName.setText(arrayList.get(position).getBankAccountName());
        holder.binding.tvBankName.setText(arrayList.get(position).getBankName());
        holder.binding.tvAccountNumber.setText(". "+arrayList.get(position).getBankNumber());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemBeneficiaryBinding binding;
        public MyViewHolder(@NonNull ItemBeneficiaryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


        }
    }
}