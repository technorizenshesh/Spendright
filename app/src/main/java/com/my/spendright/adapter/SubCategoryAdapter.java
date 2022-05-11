package com.my.spendright.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.Model.HomeModal;
import com.my.spendright.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelList;
    private OnItemClickListener mItemClickListener;

    private SubCategoryAdapter mAdapter;
    ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelListNew=new ArrayList<>();

    public SubCategoryAdapter(Context context, ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_grp_category, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetSetbudgetExpence.Result.CategoryDetail model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

          genericViewHolder.txtAmout.setText(model.getAmount());
          genericViewHolder.txtCategoryName.setText(model.getCategoryName());
          genericViewHolder.txtCalneder.setText(model.getSelectMonthWeek()+","+model.getSelectEndDayMonthWeek());

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

    private GetSetbudgetExpence.Result.CategoryDetail getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetSetbudgetExpence.Result.CategoryDetail model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmout;
        TextView txtCalneder;
        TextView txtCategoryName;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtAmout=itemView.findViewById(R.id.txtAmout);
            this.txtCalneder=itemView.findViewById(R.id.txtCalneder);
            this.txtCategoryName=itemView.findViewById(R.id.txtCategoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

