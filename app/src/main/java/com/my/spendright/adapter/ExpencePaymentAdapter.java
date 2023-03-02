package com.my.spendright.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.Model.ReportModal;
import com.my.spendright.R;
import com.my.spendright.utils.SessionManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ExpencePaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetExpenSeReport.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private SessionManager sessionManager;


    public ExpencePaymentAdapter(Context context, ArrayList<GetExpenSeReport.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetExpenSeReport.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_expence_trasaction, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetExpenSeReport.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            if(model.getType().equalsIgnoreCase("income"))
            {
                genericViewHolder.txtPrice.setTextColor(ContextCompat.getColor(mContext, R.color.green));

                DecimalFormat formatter = new DecimalFormat("#,###,###");

               String FinalAmt = getFormatedNumber(model.getTransactionAmount());

                genericViewHolder.txtPrice.setText(FinalAmt+".00");

            }else
            {
                genericViewHolder.txtPrice.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                String FinalAmt = getFormatedNumber(model.getTransactionAmount());
                genericViewHolder.txtPrice.setText(FinalAmt+".00");
            }
            genericViewHolder.txtName.setText(model.getMainCategoryName());
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

    private GetExpenSeReport.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetExpenSeReport.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice;
        TextView txtName;
        TextView txtDateTime;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtPrice=itemView.findViewById(R.id.txtPrice);
            this.txtName=itemView.findViewById(R.id.txtName);
            this.txtDateTime=itemView.findViewById(R.id.txtDateTime);



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

