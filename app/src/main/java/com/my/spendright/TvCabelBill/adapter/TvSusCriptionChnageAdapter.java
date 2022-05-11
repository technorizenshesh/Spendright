package com.my.spendright.TvCabelBill.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.act.SetBudget.SetBudgetActivity;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

public class TvSusCriptionChnageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<TvSuscriptionServiceModel.Content.Varation> modelList;
    private OnItemClickListener mItemClickListener;
    private SessionManager sessionManager;


    public TvSusCriptionChnageAdapter(Context context, ArrayList<TvSuscriptionServiceModel.Content.Varation> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<TvSuscriptionServiceModel.Content.Varation> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_tv_subscription, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final TvSuscriptionServiceModel.Content.Varation model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            sessionManager = new SessionManager((Activity) mContext);

          genericViewHolder.txtAmt.setText(model.getVariationAmount());
          genericViewHolder.txtName.setText(model.getName());

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

    private TvSuscriptionServiceModel.Content.Varation getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, TvSuscriptionServiceModel.Content.Varation model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAmt;
        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtAmt=itemView.findViewById(R.id.txtAmt);
            txtName=itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }

   
   


  

}

