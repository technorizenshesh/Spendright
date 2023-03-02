package com.my.spendright.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetMyAccountModel;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.SetBudget.SetBudgetActivity;
import com.my.spendright.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetMyAccountModel.AccountDetail> modelList;
    private OnItemClickListener mItemClickListener;
    private SessionManager sessionManager;


    public MyAccountAdapter(Context context, ArrayList<GetMyAccountModel.AccountDetail> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetMyAccountModel.AccountDetail> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_my_account_new, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetMyAccountModel.AccountDetail model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

          genericViewHolder.txtCount.setText(model.getId());
          genericViewHolder.txtMonth.setText(model.getMonthName()+" 2022");

            String FinalAmt = getFormatedNumber(model.getTotal());
            genericViewHolder.txtAmt.setText(FinalAmt+".00");
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

    private GetMyAccountModel.AccountDetail getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetMyAccountModel.AccountDetail model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMonth;
        TextView txtCount;
        TextView txtAmt;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtMonth=itemView.findViewById(R.id.txtMonth);
            txtCount=itemView.findViewById(R.id.txtCount);
            txtAmt=itemView.findViewById(R.id.txtAmt);

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

