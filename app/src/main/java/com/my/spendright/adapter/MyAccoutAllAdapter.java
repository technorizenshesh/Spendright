package com.my.spendright.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetAllAccountModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.settings.MyAccountAct;
import com.my.spendright.act.UpdateMyAccountAct;
import com.my.spendright.utils.Preference;

import java.util.ArrayList;

public class MyAccoutAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetAllAccountModel.Result> modelList;
    private OnItemClickListener mItemClickListener;

    private View promptsView;
    private AlertDialog alertDialog;

    public MyAccoutAllAdapter(Context context, ArrayList<GetAllAccountModel.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetAllAccountModel.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_my_account_all, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetAllAccountModel.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txtActName.setText(model.getHolderName());


            genericViewHolder.txtMyBudget.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(model.getCurrentBalance())));

            genericViewHolder.ivDelete.setOnClickListener(v -> {
               if(modelList.size() > 1) AlertDaliogDelete(position,model.getId());
                 else  accountDeleteAlert();
            });

            genericViewHolder.ivEdit.setOnClickListener(v -> {

                Toast.makeText(mContext, ""+model.getId(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(new Intent(mContext, UpdateMyAccountAct.class)
                        .putExtra("AccountNameId",model.getId())
                        .putExtra("AccontHolderName",model.getHolderName())
                        .putExtra("Amt",model.getCurrentBalance())
                );
            });
        }
    }

    private void accountDeleteAlert() {
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("You can not delete this account");
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                mContext.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public int getItemCount()
    {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private GetAllAccountModel.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetAllAccountModel.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtActName;
        TextView txtMyBudget;
        RelativeLayout RRDeleteAccount;
        RelativeLayout RREdit;
        ImageView ivEdit,ivDelete;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtActName=itemView.findViewById(R.id.txtActName);
            txtMyBudget=itemView.findViewById(R.id.txtMyBudget);
            RRDeleteAccount=itemView.findViewById(R.id.RRDeleteAccount);
            RREdit=itemView.findViewById(R.id.RREdit);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            ivDelete=itemView.findViewById(R.id.ivDelete);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


    public void AlertDaliogDelete(int position,String id) {

        LayoutInflater li;
        RelativeLayout RRDelete;
        EditText edtName;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(mContext);
        promptsView = li.inflate(R.layout.alert_delete_account, null);
        RRDelete = (RelativeLayout) promptsView.findViewById(R.id.RRDelete);
        edtName = (EditText) promptsView.findViewById(R.id.edtName);
        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);

        RRDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                ((MyAccountAct)mContext).DeleteGrpMethod(id);
                modelList.remove(position);
                notifyDataSetChanged();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }



}

