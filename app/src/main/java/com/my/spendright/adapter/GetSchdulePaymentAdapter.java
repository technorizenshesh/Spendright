package com.my.spendright.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetSchdulepAymentModel;
import com.my.spendright.Model.ReportModal;
import com.my.spendright.R;
import com.my.spendright.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GetSchdulePaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetSchdulepAymentModel.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private SessionManager sessionManager;


    public GetSchdulePaymentAdapter(Context context, ArrayList<GetSchdulepAymentModel.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetSchdulepAymentModel.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_schdule_payment, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetSchdulepAymentModel.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

          genericViewHolder.Txtname.setText(model.getTypeOfPayment());


          genericViewHolder.txtDate.setText(model.getScheduleDate());

          String FinalAmt = getFormatedNumber(model.getAmount());

              genericViewHolder.TxtPrice.setText(FinalAmt+".00");

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

    private GetSchdulepAymentModel.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetSchdulepAymentModel.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Txtname;
        TextView TxtPrice;
        TextView txtDate;


        public ViewHolder(final View itemView) {
            super(itemView);

            Txtname=itemView.findViewById(R.id.Txtname);
            TxtPrice=itemView.findViewById(R.id.TxtPrice);
            txtDate=itemView.findViewById(R.id.txtDate);

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

