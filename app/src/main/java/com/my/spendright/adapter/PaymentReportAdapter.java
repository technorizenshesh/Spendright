package com.my.spendright.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.ReportModal;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.act.PaymentReport;
import com.my.spendright.act.PaymentReportDateWiseAct;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

public class PaymentReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<ReportModal.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private SessionManager sessionManager;


    public PaymentReportAdapter(Context context, ArrayList<ReportModal.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<ReportModal.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_payment_report, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final ReportModal.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

          genericViewHolder.txtRequestId.setText(model.getRequestId());
          genericViewHolder.txtDate.setText(model.getCurrentDate());
          genericViewHolder.txtpayment.setText(model.getAmount());
          genericViewHolder.txtType.setText(model.getType());

         // genericViewHolder.txtStatus.setText(model.getCheckStatus());
            genericViewHolder.txtDetails.setOnClickListener(v -> {

              //  mContext.startActivity(new Intent(mContext, PaymentReportDateWiseAct.class));

            });

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

    private ReportModal.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, ReportModal.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtRequestId;
        TextView txtDate;
        TextView txtpayment;
        TextView txtStatus;
        TextView txtType;
        TextView txtDetails;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtRequestId=itemView.findViewById(R.id.txtRequestId);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtpayment=itemView.findViewById(R.id.txtpayment);
            txtStatus=itemView.findViewById(R.id.txtStatus);
            txtType=itemView.findViewById(R.id.txtType);
            txtDetails=itemView.findViewById(R.id.txtDetails);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });
        }
    }

}

