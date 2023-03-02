package com.my.spendright.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.Model.HomeModal;
import com.my.spendright.R;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.SetBudget.SetBudgetActivity;
import com.my.spendright.utils.AddCategoryModel;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetProfileModel.AccountDetail> modelList;
    private OnItemClickListener mItemClickListener;
    private SessionManager sessionManager;


    public MyAccountHomeAdapter(Context context, ArrayList<GetProfileModel.AccountDetail> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetProfileModel.AccountDetail> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_my_account, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetProfileModel.AccountDetail model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            sessionManager = new SessionManager((Activity) mContext);

//          genericViewHolder.txtAccountName.setText(model.getHolderName());

          genericViewHolder.txtAmt.setText(model.getTotal());

            String FinalAmt = getFormatedNumber(model.getCurrentBalance());

            genericViewHolder.txtAmt1.setText(FinalAmt+".00");

          genericViewHolder.txtBudget.setOnClickListener(v -> {
              sessionManager.saveAccountId(model.getId());
              mContext.startActivity(new Intent(mContext, SetBudgetActivity.class));
          });

//          setMEthod(genericViewHolder.cubiclinechart);
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

    private GetProfileModel.AccountDetail getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetProfileModel.AccountDetail model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAccountName;
        TextView txtBudget;
        TextView txtAmt;
        TextView txtAmt1;
//        ValueLineChart cubiclinechart;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtAccountName=itemView.findViewById(R.id.txtAccountName);
            txtBudget=itemView.findViewById(R.id.txtBudget);
            txtAmt=itemView.findViewById(R.id.txtAmt);
            txtAmt1=itemView.findViewById(R.id.txtAmt1);
//            cubiclinechart=itemView.findViewById(R.id.cubiclinechart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


//    private void setMEthod(ValueLineChart cubiclinechart) {
//
//        ValueLineSeries series = new ValueLineSeries();
//        series.setColor(0xFF56B7F1);
//
//        series.addPoint(new ValueLinePoint("Jan", 0.4f));
//        series.addPoint(new ValueLinePoint("Feb", 3.4f));
//        series.addPoint(new ValueLinePoint("Mar", 1.2f));
//        series.addPoint(new ValueLinePoint("Apr", .4f));
//        series.addPoint(new ValueLinePoint("Mai", 2.6f));
//        series.addPoint(new ValueLinePoint("Jun", 1.0f));
//        series.addPoint(new ValueLinePoint("Jul", 3.5f));
//        series.addPoint(new ValueLinePoint("Aug", 2.4f));
//        series.addPoint(new ValueLinePoint("Sep", 2.4f));
//        series.addPoint(new ValueLinePoint("Oct", 3.4f));
//        series.addPoint(new ValueLinePoint("Nov", .4f));
//        series.addPoint(new ValueLinePoint("Dec", 1.3f));
//
//        cubiclinechart.addSeries(series);
//        cubiclinechart.startAnimation();
//    }



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

