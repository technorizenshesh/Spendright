package com.my.spendright.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.HomeCatModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ItemHomeBinding;
import com.my.spendright.listener.HomeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    Context context;
    ArrayList<HomeCatModel.Result>arrayList;
    HomeListener listener;

    public HomeAdapter(Context context, ArrayList<HomeCatModel.Result> arrayList, HomeListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_home,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getCategoryName());
        Picasso.get().load(arrayList.get(position).getCategoryImage()).into(holder.binding.ivImg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemHomeBinding binding;
        public MyViewHolder(@NonNull ItemHomeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.llMain.setOnClickListener(view -> listener.home(arrayList.get(getAbsoluteAdapterPosition()).getId(),arrayList.get(getAbsoluteAdapterPosition()).getCategoryName()));
        }
    }
}
