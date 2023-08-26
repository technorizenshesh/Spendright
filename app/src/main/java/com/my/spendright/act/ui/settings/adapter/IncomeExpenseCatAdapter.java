package com.my.spendright.act.ui.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.R;
import com.my.spendright.act.ui.settings.listener.onCategoryClickListener;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.ItemBudgetCatBinding;
import com.my.spendright.utils.Preference;

import java.util.ArrayList;

public class IncomeExpenseCatAdapter  extends RecyclerView.Adapter<IncomeExpenseCatAdapter.MyViewHolder> {
    Context context;
    ArrayList<IncomeExpenseCatModel.Category> arrayList;
    MyViewHolder holderCopy;
    onCategoryClickListener listener;
    public IncomeExpenseCatAdapter(Context context, ArrayList<IncomeExpenseCatModel.Category>arrayList,onCategoryClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetCatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_budget_cat,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.tvCatName.setText(arrayList.get(position).getCatName());

        if(!arrayList.get(position).getCatEmoji().equalsIgnoreCase("")) holder.binding.tvImg.setText(Preference.decodeEmoji(arrayList.get(position).getCatEmoji()));

        if(arrayList.get(position).isChk()==true){
            holder.binding.ivEdit.setVisibility(View.VISIBLE);
            holder.binding.ivDelete.setVisibility(View.VISIBLE);
        }
        else {
            holder.binding.ivEdit.setVisibility(View.GONE);
            holder.binding.ivDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemBudgetCatBinding binding;
        public MyViewHolder(@NonNull ItemBudgetCatBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivEdit.setOnClickListener(view -> listener.onCategoryClick(arrayList.get(getAdapterPosition()),"edit"));

            binding.ivDelete.setOnClickListener(view -> listener.onCategoryClick(arrayList.get(getAdapterPosition()),"delete"));

        }
    }


    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}