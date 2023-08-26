package com.my.spendright.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.R;
import com.my.spendright.act.ui.budget.model.BeneficiaryModel;
import com.my.spendright.databinding.ItemBeneficiaryDeleteBinding;
import com.my.spendright.listener.BeneficiaryListener;

import java.util.ArrayList;

public class ManageBeneficiaryAdapter extends RecyclerView.Adapter<ManageBeneficiaryAdapter.MyViewHolder> {

        Context context;
        ArrayList<BeneficiaryModel.Datum> arrayList;
        BeneficiaryListener listener;


        public ManageBeneficiaryAdapter(Context context, ArrayList<BeneficiaryModel.Datum> arrayList, BeneficiaryListener listener) {
                this.context = context;
                this.arrayList = arrayList;
                this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemBeneficiaryDeleteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_beneficiary_delete, parent, false);
                return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                holder.binding.tvAccountName.setText(arrayList.get(position).getBankAccountName());
                holder.binding.tvAccountNumber.setText(". " + arrayList.get(position).getBankNumber());
                holder.binding.tvBankName.setText(arrayList.get(position).getBankName());

                holder.binding.ivDelete.setOnClickListener(view -> listener.OnBeneficiary(position));

        }


        @Override
        public int getItemCount() {
                return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
                ItemBeneficiaryDeleteBinding binding;

                public MyViewHolder(@NonNull ItemBeneficiaryDeleteBinding itemView) {
                        super(itemView.getRoot());
                        binding = itemView;

                }

        }

}
