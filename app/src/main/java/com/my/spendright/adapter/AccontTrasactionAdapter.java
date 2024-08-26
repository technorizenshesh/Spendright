package com.my.spendright.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.R;
import com.my.spendright.utils.Preference;

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
                         //  String FinalAmt = Preference.doubleToStringNoDecimalSecond(Double.parseDouble(model.getTransactionAmount().replace(",","")));
                         //  FinalAmt = String.valueOf(Double.parseDouble(FinalAmt.replace(",",""))); //+ Double.parseDouble(modelList.get(position).getAdminFee().replace(",","")));
                       //    genericViewHolder.txtPrice.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(FinalAmt)));
                           if(Double.parseDouble(model.getTransactionAmount())<10){
                               genericViewHolder.txtPrice.setText("₦"+ "0"+Preference.doubleToStringNoDecimal(Double.parseDouble(model.getTransactionAmount().replace(",",""))));
                           }
                           else genericViewHolder.txtPrice.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(model.getTransactionAmount().replace(",",""))));

                       }else
                       {
                           genericViewHolder.txtPrice.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                           Log.e("transaction amount",model.getTransactionAmount());
                          // String FinalAmt = Preference.doubleToStringNoDecimalSecond(Double.parseDouble(model.getTransactionAmount().replace(",","")));
                        //   FinalAmt = String.valueOf(Double.parseDouble(FinalAmt.replace(",",""))); //+ Double.parseDouble(modelList.get(position).getAdminFee().replace(",","")));
                        //   genericViewHolder.txtPrice.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(FinalAmt)));
                            if(Double.parseDouble(model.getTransactionAmount())<10){
                                genericViewHolder.txtPrice.setText("₦"+ "0"+Preference.doubleToStringNoDecimal(Double.parseDouble(model.getTransactionAmount().replace(",",""))));
                            }
                           else genericViewHolder.txtPrice.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(model.getTransactionAmount().replace(",",""))));

                       }

            if(!model.getEmoji().equalsIgnoreCase("")){
                genericViewHolder.tvImg.setVisibility(View.VISIBLE);
                genericViewHolder.img1.setVisibility(View.GONE);
                genericViewHolder.tvImg.setText(Preference.decodeEmoji(model.getEmoji()));
                 // genericViewHolder.img1.setImageBitmap(Preference.drawTextToBitmap(mContext,R.id.img1,model.getEmoji()));
            }
            else {
                genericViewHolder.tvImg.setVisibility(View.GONE);
                genericViewHolder.img1.setVisibility(View.VISIBLE);
            }

           if(!model.getMainCategoryName().equalsIgnoreCase("")) genericViewHolder.txtName.setText(model.getMainCategoryName());
           else genericViewHolder.txtName.setText(/*"Ref: "+*/model.getCatName());
            genericViewHolder.txtDateTime.setText(Preference.convertDate(model.getDateTime()));

            if(!model.getDescription().equalsIgnoreCase("")){
                     genericViewHolder.txtDescription.setVisibility(View.VISIBLE);
                        if(!model.getMainCategoryName().equalsIgnoreCase("")) genericViewHolder.txtDescription.setText(model.getMainCategoryName()+ "/"+model.getDescription());
                        else genericViewHolder.txtDescription.setText(/*"Ref: "+*/model.getCatName() + "/" + model.getDescription());


                      //  genericViewHolder.txtDescription.setText(model.getCatName()+"/"+model.getDescription());
                 }else {
                     genericViewHolder.txtDescription.setVisibility(View.GONE);

                 }
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
        TextView txtDescription,tvImg;
        ImageView img1;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtPrice=itemView.findViewById(R.id.txtPrice);
            this.txtName=itemView.findViewById(R.id.txtName);
            this.txtDateTime=itemView.findViewById(R.id.txtDateTime);
            this.txtDescription=itemView.findViewById(R.id.txtDescription);
            this.tvImg = itemView.findViewById(R.id.tvImg);
            this.img1 = itemView.findViewById(R.id.img1);


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

