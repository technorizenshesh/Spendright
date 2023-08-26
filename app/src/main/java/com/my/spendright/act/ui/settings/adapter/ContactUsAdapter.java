package com.my.spendright.act.ui.settings.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.R;
import com.my.spendright.act.TermsCondition;
import com.my.spendright.act.ui.settings.model.ContactUsModel;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.ItemBudgetCatBinding;
import com.my.spendright.databinding.ItemContactBinding;

import java.util.ArrayList;


public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.MyViewHolder> {
    Context context;
    ArrayList<ContactUsModel.Result> arrayList;

    public ContactUsAdapter(Context context, ArrayList<ContactUsModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_contact,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvTitle.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemContactBinding binding;
        public MyViewHolder(@NonNull ItemContactBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.llMain.setOnClickListener(view -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(arrayList.get(getAbsoluteAdapterPosition()).getLink())));
            });
        }
    }
}
