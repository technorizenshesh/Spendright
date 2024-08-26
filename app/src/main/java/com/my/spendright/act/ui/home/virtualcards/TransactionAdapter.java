package com.my.spendright.act.ui.home.virtualcards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.R;
import com.my.spendright.databinding.ItemTransactionBinding;
import com.my.spendright.listener.BeneficiaryListener;
import com.my.spendright.utils.Preference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
 public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
   Context context;
    private ArrayList<GetExpenSeReport.Result> modelList;
    BeneficiaryListener listener;

    public TransactionAdapter(Context context,ArrayList<GetExpenSeReport.Result> modelList,BeneficiaryListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_transaction,parent,false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String txt[] = modelList.get(position).getRefrence_no().split("-");
        holder.binding.txtRef.setText("Ref: "+modelList.get(position).getRefrence_no() /*txt[1]  + txt[2]*/);

      if(modelList.get(position).getFlag()!=null){
          if( modelList.get(position).getFlag().equalsIgnoreCase("AMOUNT_TRANSFERRED_TO_OTHER_USER_IN_MAIN_WALLET") || modelList.get(position).getFlag().equalsIgnoreCase("WIDRAW_MONEY_FROM_MAIN_WALLET_TO_BANK"))
              holder.binding.txtName.setText(modelList.get(position).getCat_name()+"/"+modelList.get(position).getDescription());
          else holder.binding.txtName.setText(modelList.get(position).getCat_name()+"/"+modelList.get(position).getService());

      } else holder.binding.txtName.setText(modelList.get(position).getCat_name()+"/"+modelList.get(position).getService());

        holder.binding.txtDateTime.setText(Preference.convertDate(modelList.get(position).getDateTime()));
        holder.binding.txtPrice.setTextColor(ContextCompat.getColor(context, R.color.red));
        String FinalAmt =   Preference.doubleToStringNoDecimal(Double.parseDouble(modelList.get(position).getTransactionAmount().replace(",","")));
        FinalAmt = String.valueOf(Double.parseDouble(FinalAmt.replace(",",""))); // + Double.parseDouble(modelList.get(position).getAdminFee()));
         if(Double.parseDouble(FinalAmt)<10)
             holder.binding.txtPrice.setText("₦"+"0"+Preference.doubleToStringNoDecimal(Double.parseDouble(FinalAmt.replace(",",""))));
         else  holder.binding.txtPrice.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(FinalAmt.replace(",",""))));




        if(!modelList.get(position).getEmoji().equalsIgnoreCase("")){
            holder.binding.tvImg.setVisibility(View.VISIBLE);
            holder.binding.img1.setVisibility(View.GONE);
            holder.binding.tvImg.setText(Preference.decodeEmoji(modelList.get(position).getEmoji()));
            // genericViewHolder.img1.setImageBitmap(Preference.drawTextToBitmap(mContext,R.id.img1,model.getEmoji()));
        }
        else {
            holder.binding.tvImg.setVisibility(View.GONE);
            holder.binding.img1.setVisibility(View.VISIBLE);
        }



        holder.itemView.setOnClickListener(view -> listener.OnBeneficiary(position));



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionBinding binding;
        public MyViewHolder(@NonNull ItemTransactionBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String getFormatedNumber(String number){
        if(!number.isEmpty()) {
            double val = Double.parseDouble(number);
            return NumberFormat.getNumberInstance(Locale.US).format(val);
        }else{
            return "0";
        }
    }




}
