package com.my.spendright.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.Model.UpdateSchdulepAymentModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.SetBudgetActivity;
import com.my.spendright.utils.AddCategoryModel;
import com.my.spendright.utils.RetrofitClients;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAllSubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelList;
    private OnItemClickListener mItemClickListener;

    private MyAllSubCategoryAdapter mAdapter;
    private View promptsView;
    private AlertDialog alertDialog;

    ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelListNew = new ArrayList<>();

    public MyAllSubCategoryAdapter(Context context, ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_grp_category_my_all, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetSetbudgetExpence.Result.CategoryDetail model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

          String FinalAmt = getFormatedNumber(model.getAmount());

          genericViewHolder.txtAmout.setText(FinalAmt+".00");

          genericViewHolder.txtCategoryName.setText(model.getCategoryName());

          genericViewHolder.txtCalneder.setText(model.getSelectMonthWeek()+","+model.getSelectEndDayMonthWeek());

          genericViewHolder.RREdit.setOnClickListener(v -> {

              AlertDialogEdit(model.getGroupId(),model.getId(),model.getUserId(),model.getCategoryName(),model.getAmount());

          });

          genericViewHolder.RRDeleteGrpCatgy.setOnClickListener(v -> {
              AlertDialogDelte(model.getId());
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
        RelativeLayout RREdit;
        RelativeLayout RRDeleteGrpCatgy;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtAmout=itemView.findViewById(R.id.txtAmout);
            this.txtCalneder=itemView.findViewById(R.id.txtCalneder);
            this.txtCategoryName=itemView.findViewById(R.id.txtCategoryName);
            this.RREdit=itemView.findViewById(R.id.RREdit);
            this.RRDeleteGrpCatgy=itemView.findViewById(R.id.RRDeleteGrpCatgy);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


    private void AlertDialogEdit(String Grpid,String id,String UserId,String CategoryName,String Amt) {

        LayoutInflater li;
        RelativeLayout RRSave;
        EditText edtCategoryName;
        EditText edtAmt;

        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(mContext);
        promptsView = li.inflate(R.layout.edit_grp_category, null);
        RRSave = (RelativeLayout) promptsView.findViewById(R.id.RRSave);
        edtCategoryName = (EditText) promptsView.findViewById(R.id.edtCategoryName);
        edtAmt = (EditText) promptsView.findViewById(R.id.edtAmt);
        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);

        edtCategoryName.setText(CategoryName);
        edtAmt.setText(Amt);

        RRSave.setOnClickListener(v -> {

            String CategoyrName=edtCategoryName.getText().toString();
            String Amtc=edtAmt.getText().toString();

            EdtGrpMethod(id,UserId,Grpid,CategoyrName,Amtc);
            alertDialog.dismiss();

        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

}

    private void AlertDialogDelte(String id) {

        LayoutInflater li;
        RelativeLayout RRDelete;

        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(mContext);
        promptsView = li.inflate(R.layout.delete_grp_category, null);
        RRDelete = (RelativeLayout) promptsView.findViewById(R.id.RRDelete);
        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);


        RRDelete.setOnClickListener(v -> {
            DeleteGrpMethod(id);
            alertDialog.dismiss();
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void EdtGrpMethod(String Id,String Userid,String GrpId,String CategoryNAme,String Amt){
        Call<AddCategoryModel> call = RetrofitClients.getInstance().getApi()
                .editgroupCategory(Id,Userid,GrpId,CategoryNAme,Amt);
        call.enqueue(new Callback<AddCategoryModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<AddCategoryModel> call, Response<AddCategoryModel> response) {
                try {

                    AddCategoryModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        Toast.makeText(mContext, "Add Category SuccessFully", Toast.LENGTH_SHORT).show();

                        ((SetBudgetActivity)mContext).getExpenceMethod();

                        //   getExpenceMethod();

                    } else {

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<AddCategoryModel> call, Throwable t) {
            }
        });
    }

   private void DeleteGrpMethod(String Id){
        Call<UpdateSchdulepAymentModel> call = RetrofitClients.getInstance().getApi()
                .Api_category_delete(Id);
        call.enqueue(new Callback<UpdateSchdulepAymentModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<UpdateSchdulepAymentModel> call, Response<UpdateSchdulepAymentModel> response) {
                try {

                    UpdateSchdulepAymentModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        Toast.makeText(mContext, "Add Category SuccessFully", Toast.LENGTH_SHORT).show();

                        ((SetBudgetActivity)mContext).getExpenceMethod();

                    } else {

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<UpdateSchdulepAymentModel> call, Throwable t) {
            }
        });
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

