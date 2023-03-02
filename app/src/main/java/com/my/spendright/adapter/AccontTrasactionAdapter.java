package com.my.spendright.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AccontTrasactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> modelList;
    private OnItemClickListener mItemClickListener;

    private AccontTrasactionAdapter mAdapter;
    ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> modelListNew=new ArrayList<>();

    public AccontTrasactionAdapter(Context context, ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_account_trasaction, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetBudgetActTransaction.AccountDetail.Transaction model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

           // String s= "<font  color=#9E16110D> ₦</font>";

                       if(model.getType().equalsIgnoreCase("income"))
                       {
                           genericViewHolder.txtPrice.setTextColor(ContextCompat.getColor(mContext, R.color.green));

                           String FinalAmt = getFormatedNumber(model.getTransactionAmount());

                           genericViewHolder.txtPrice.setText(FinalAmt+".00");

                       }else
                       {
                           genericViewHolder.txtPrice.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                           String FinalAmt = getFormatedNumber(model.getTransactionAmount());
                           genericViewHolder.txtPrice.setText(FinalAmt+".00");
                       }



                  genericViewHolder.txtName.setText(model.getMainCategoryName());
                  genericViewHolder.txtDescription.setText(model.getDescription());
                  genericViewHolder.txtDateTime.setText(model.getDateTime());
        }
    }

    @Override
    public int getItemCount()
    {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private GetBudgetActTransaction.AccountDetail.Transaction getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetBudgetActTransaction.AccountDetail.Transaction model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPrice;
        TextView txtName;
        TextView txtDateTime;
        TextView txtDescription;


        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtPrice=itemView.findViewById(R.id.txtPrice);
            this.txtName=itemView.findViewById(R.id.txtName);
            this.txtDateTime=itemView.findViewById(R.id.txtDateTime);
            this.txtDescription=itemView.findViewById(R.id.txtDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
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

