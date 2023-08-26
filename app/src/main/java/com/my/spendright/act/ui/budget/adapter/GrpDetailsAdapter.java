package com.my.spendright.act.ui.budget.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.act.KYCAct;
import com.my.spendright.act.ui.budget.BudgetGrpAct;
import com.my.spendright.act.ui.budget.BudgetGrpDetailModel;
import com.my.spendright.act.ui.budget.listener.onGrpCatListener;
import com.my.spendright.act.ui.settings.model.BudgetCategoryModel;
import com.my.spendright.databinding.ItemBudgetGroupBinding;
import com.my.spendright.databinding.ItemBudgetGrpDetailsBinding;
import com.my.spendright.utils.Preference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GrpDetailsAdapter extends RecyclerView.Adapter<GrpDetailsAdapter.MyViewHolder> {
    Context context;
    ArrayList<BudgetCategoryModel.Data>arrayList;
    LoginModel finallyPr;
    onGrpCatListener listener;
    public GrpDetailsAdapter(Context context, ArrayList<BudgetCategoryModel.Data>arrayList, LoginModel finallyPr,onGrpCatListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.finallyPr =finallyPr;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetGrpDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_budget_grp_details,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          // Preference.getCurrentDate()


       /* if(Calendar.getInstance().getTime().getDate() >= Preference.parseDate(arrayList.get(position).getBcatDueDate()).getDate() ) {
            arrayList.get(position).setBcatUnlockShareStatus("1");

        } else arrayList.get(position).setBcatUnlockShareStatus("0");*/
        if(Calendar.getInstance().getTimeInMillis() >= Preference.parseDateCopy(arrayList.get(position).getBcatDueDate()) ){
            Log.e("Current Date badi hai==",position+"");
            arrayList.get(position).setBcatUnlockShareStatus("1");
        } else {
            Log.e("Current Date chotihai==",position+"");
            //arrayList.get(position).setBcatUnlockShareStatus("0");
        }

        if(Preference.getCurrentDate().equalsIgnoreCase(arrayList.get(position).getBcatDueDate())) {
            holder.binding.tvDate.setTextColor(context.getResources().getColor(R.color.green));

        }else {
            if(Calendar.getInstance().getTimeInMillis() > Preference.parseDateCopy(arrayList.get(position).getBcatDueDate())) {
                holder.binding.tvDate.setTextColor(context.getResources().getColor(R.color.red));
            } else if(Calendar.getInstance().getTimeInMillis() < Preference.parseDateCopy(arrayList.get(position).getBcatDueDate()) ) {
                holder.binding.tvDate.setTextColor(context.getResources().getColor(R.color.black));

            }
        }







            //  Log.e("chk un===", arrayList.get(position).getBcatUnlockShareStatus());
      Log.e("system====",System.currentTimeMillis()+"");
        Log.e("Calander===", Calendar.getInstance().getTime().getDate()+"");
     //  Log.e("date====",(Preference.parseDate(arrayList.get(position).getBcatDueDate()).getDate())+"");
        Log.e("currentDate====",Preference.getCurrentDate()+"");
        Log.e("date====",arrayList.get(position).getBcatDueDate()+"");


        holder.binding.tvCatName.setText(arrayList.get(position).getBcatCategoryName());
      //  holder.binding.tvAmount.setText("₦"+arrayList.get(position).getBcatBudgetAmount());
        holder.binding.tvAmount.setText("₦"+ Preference.doubleToStringNoDecimalSecond(Double.parseDouble(arrayList.get(position).getBcatBudgetAmount())));
        if(arrayList.get(position).getBcatCategoryEmoji()!=null && !arrayList.get(position).getBcatCategoryEmoji().equalsIgnoreCase("")) holder.binding.tvImg.setText(Preference.decodeEmoji(arrayList.get(position).getBcatCategoryEmoji()));


        holder.binding.tvType.setText(arrayList.get(position).getBcatCategoryName());
      //  holder.binding.tvDetailsAmount.setText("₦"+arrayList.get(position).getBcatBudgetAmount());
        holder.binding.tvDetailsAmount.setText("₦"+ Preference.doubleToStringNoDecimalSecond(Double.parseDouble(arrayList.get(position).getBcatBudgetAmount())));

        holder.binding.tvDate.setText("Due on "+arrayList.get(position).getBcatDueDate());
        if(arrayList.get(position).getBcatCategoryEmoji()!=null && !arrayList.get(position).getBcatCategoryEmoji().equalsIgnoreCase("")) holder.binding.ivImg.setText(Preference.decodeEmoji(arrayList.get(position).getBcatCategoryEmoji()));


        if(arrayList.get(position).getBcatUnlockShareStatus().equalsIgnoreCase("0")){
            if(Double.parseDouble(arrayList.get(position).getBcatBudgetAmount())==0) {
              //  holder.binding.btnWithdraw.setText(context.getString(R.string.delete));
                holder.binding.btnEdit.setVisibility(View.VISIBLE);
                holder.binding.ivEdit.setVisibility(View.VISIBLE);
                holder.binding.btnDelete.setVisibility(View.VISIBLE);
                holder.binding.ivDelete.setVisibility(View.VISIBLE);
                holder.binding.btnWithdraw.setVisibility(View.GONE);
                holder.binding.tvFund.setText(context.getString(R.string.fund_not_available));
                holder.binding.tvFundText.setText(context.getString(R.string.txt_fund_not_available));

            }
            else {
                holder.binding.btnWithdraw.setVisibility(View.VISIBLE);
                holder.binding.btnWithdraw.setText(context.getString(R.string.unblock_funds));
                holder.binding.btnEdit.setVisibility(View.GONE);
                holder.binding.ivEdit.setVisibility(View.GONE);
                holder.binding.btnDelete.setVisibility(View.GONE);
                holder.binding.ivDelete.setVisibility(View.GONE);
                holder.binding.tvFund.setText(context.getString(R.string.fund_available));
                holder.binding.tvFundText.setText(context.getString(R.string.txt_fund_available));
            }
        }
        else {
            if(Double.parseDouble(arrayList.get(position).getBcatBudgetAmount())==0){
                holder.binding.btnEdit.setVisibility(View.VISIBLE);
                holder.binding.ivEdit.setVisibility(View.VISIBLE);
                holder.binding.btnDelete.setVisibility(View.VISIBLE);
                holder.binding.ivDelete.setVisibility(View.VISIBLE);
                holder.binding.btnWithdraw.setVisibility(View.GONE);
              //  holder.binding.btnWithdraw.setText(context.getString(R.string.delete));
                holder.binding.tvFund.setText(context.getString(R.string.fund_not_available));
                holder.binding.tvFundText.setText(context.getString(R.string.txt_fund_not_available));

            }else {
                holder.binding.btnWithdraw.setVisibility(View.VISIBLE);
                holder.binding.btnWithdraw.setText(context.getString(R.string.withdraw_funds));
                holder.binding.btnEdit.setVisibility(View.VISIBLE);
                holder.binding.ivEdit.setVisibility(View.VISIBLE);
                holder.binding.btnDelete.setVisibility(View.GONE);
                holder.binding.ivDelete.setVisibility(View.GONE);
                holder.binding.tvFund.setText(context.getString(R.string.fund_ready_to_withdraw));
                holder.binding.tvFundText.setText(context.getString(R.string.text_fund_withdraw));
            }



        }


        if(arrayList.get(position).isChk()==false) {
              holder.binding.llMain.setVisibility(View.VISIBLE);
              holder.binding.llDetails.setVisibility(View.GONE);

          }
          else {
              holder.binding.llMain.setVisibility(View.GONE);
              holder.binding.llDetails.setVisibility(View.VISIBLE);
          }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemBudgetGrpDetailsBinding binding;
        public MyViewHolder(@NonNull ItemBudgetGrpDetailsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivHide.setOnClickListener(view -> {
                if(arrayList.get(getAdapterPosition()).isChk()==false){
                    arrayList.get(getAbsoluteAdapterPosition()).setChk(true);
                    notifyDataSetChanged();
                }
            });

            binding.ivShow.setOnClickListener(view -> {
                if(arrayList.get(getAdapterPosition()).isChk()==true){
                    arrayList.get(getAbsoluteAdapterPosition()).setChk(false);
                    notifyDataSetChanged();
                }
            });



            binding.btnEdit.setOnClickListener(view -> {
                listener.onGrpCate("edit",arrayList.get(getAdapterPosition()));

            });


            binding.btnDelete.setOnClickListener(view -> {
                listener.onGrpCate("delete", arrayList.get(getAdapterPosition()));
            });


            binding.btnWithdraw.setOnClickListener(view -> {
                if (arrayList.get(getAdapterPosition()).getBcatUnlockShareStatus().equalsIgnoreCase("0")) {
                   if(binding.btnWithdraw.getText().toString().equalsIgnoreCase("Unlock Fund"))  listener.onGrpCate("unlock", arrayList.get(getAdapterPosition()));
                } else {
                     if(finallyPr.getResult().getKycStatus().equalsIgnoreCase("1")) {
                     listener.onGrpCate("withdraw", arrayList.get(getAdapterPosition()));
                     }
                     else {
                         context.startActivity(new Intent(context, KYCAct.class)
                                 .putExtra("user_id", finallyPr.getResult().getId())
                                 .putExtra("mobile", finallyPr.getResult().getMobile())
                                 .putExtra("name", finallyPr.getResult().getLastName() + finallyPr.getResult().getOtherLegalName())
                                 .putExtra("from", "WithdrawScreen"));
                     }

                 }
            });

        }
    }
}
