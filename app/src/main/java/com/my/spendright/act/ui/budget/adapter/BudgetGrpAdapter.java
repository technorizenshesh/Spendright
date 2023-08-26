package com.my.spendright.act.ui.budget.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.AccountModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.BudgetGrpAct;
import com.my.spendright.act.ui.budget.listener.onGrpListener;
import com.my.spendright.act.ui.budget.model.BudgetGrpModel;
import com.my.spendright.adapter.AccountAdapter;
import com.my.spendright.databinding.ItemBudgetGroupBinding;
import com.my.spendright.databinding.ItemFundBinding;
import com.my.spendright.utils.Preference;

import java.util.ArrayList;

public class BudgetGrpAdapter extends RecyclerView.Adapter<BudgetGrpAdapter.MyViewHolder> {
    Context context;
    ArrayList<BudgetGrpModel.Group>arrayList;
    onGrpListener listener;
    public BudgetGrpAdapter(Context context, ArrayList<BudgetGrpModel.Group>arrayList,onGrpListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_budget_group,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvGrpName.setText(arrayList.get(position).getGroupName());
        holder.binding.tvGrpDescription.setText(arrayList.get(position).getGroupDescription());
        holder.binding.viewGrpColor.setBackgroundColor(Color.parseColor(arrayList.get(position).getGroupColor()));

       holder.binding.tvTotal.setText("₦"+Preference.doubleToStringNoDecimalSecond(Double.parseDouble(arrayList.get(position).getAmount())));
       //  holder.binding.tvTotal.setText("₦"+arrayList.get(position).getAmount());

        if (Double.parseDouble(arrayList.get(position).getAmount())>0) holder.binding.ivDelete.setVisibility(View.GONE);
            else holder.binding.ivDelete.setVisibility(View.VISIBLE);




        holder.itemView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, BudgetGrpAct.class)
                    .putExtra("id",arrayList.get(position).getGroupRowId())
                    .putExtra("grpName",arrayList.get(position).getGroupName()));
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemBudgetGroupBinding binding;
        public MyViewHolder(@NonNull ItemBudgetGroupBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

          binding.ivDelete.setOnClickListener(view -> listener.onGrp(getAbsoluteAdapterPosition(),arrayList.get(getAbsoluteAdapterPosition()),"delete"));

        }
    }
}
