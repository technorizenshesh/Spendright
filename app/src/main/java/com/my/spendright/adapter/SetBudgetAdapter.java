package com.my.spendright.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.SetBudgetActivity;
import com.my.spendright.utils.AddCategoryModel;
import com.my.spendright.utils.RetrofitClients;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetBudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetSetbudgetExpence.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private SubCategoryAdapter mAdapter1;
    ArrayList<GetSetbudgetExpence.Result.CategoryDetail> modelListCategory=new ArrayList<>();
    private View promptsView;
    private AlertDialog alertDialog;

    String select_Month_Week="";
    String endMonthDayEndWeek="";
    String month="";
    int arr[];
    int pos;
    String USerId="";
    String accountId="";

    public SetBudgetAdapter(Context context, ArrayList<GetSetbudgetExpence.Result> modelList,String USerId,String accountId) {
        this.mContext = context;
        this.modelList = modelList;
        this.USerId = USerId;
        this.accountId = accountId;
    }

    public void updateList(ArrayList<GetSetbudgetExpence.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_grp, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetSetbudgetExpence.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txt_Name.setText(model.getGroupName());
            if(model.isShowHide())
            {
                genericViewHolder.recyclerCategory.setVisibility(View.VISIBLE);

            }else
            {
                genericViewHolder.recyclerCategory.setVisibility(View.GONE);
            }

            genericViewHolder.RRPlus.setOnClickListener(v -> {
                AlertDaliogArea(model.getGroupId());
            });

            genericViewHolder.RRhide.setOnClickListener(v -> {
                if(model.isShowHide())
                {
                    model.setShowHide(false);
                    pos=position;

                }else
                {
                    model.setShowHide(true);
                }
                notifyDataSetChanged();
            });

            genericViewHolder.RRDelete.setOnClickListener(v -> {
                AlertDaliogDelete(position,model.getGroupId());
            });

            setAdapter(genericViewHolder.recyclerCategory, (ArrayList<GetSetbudgetExpence.Result.CategoryDetail>) modelList.get(position).getCategoryDetail());

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

    private GetSetbudgetExpence.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetSetbudgetExpence.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerCategory;
        RelativeLayout RRPlus;
        RelativeLayout RRDelete;
        RelativeLayout RRhide;
        ImageView imgPlus;
        TextView txt_Name;

        public ViewHolder(final View itemView) {
            super(itemView);

            txt_Name=itemView.findViewById(R.id.txt_Name);
            recyclerCategory=itemView.findViewById(R.id.recyclerCategory);
            RRhide=itemView.findViewById(R.id.RRhide);
            this.imgPlus=itemView.findViewById(R.id.imgPlus);
            this.RRDelete=itemView.findViewById(R.id.RRDelete);
            this.RRPlus=itemView.findViewById(R.id.RRPlus);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }

    private void setAdapter(RecyclerView recyclerView, ArrayList<GetSetbudgetExpence.Result.CategoryDetail> categoryDetail) {

        mAdapter1 = new SubCategoryAdapter(mContext,categoryDetail);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter1);
        mAdapter1.SetOnItemClickListener(new SubCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetSetbudgetExpence.Result.CategoryDetail model) {

            }
        });
    }

    private void AlertDaliogArea(String Grpid) {
        String[] strAr1=new String[] {"Sat","Sun","Mon","Tue","Wed","Thu","frd"};
        String[] strAr12=new String[] {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};

        LayoutInflater li;
        RelativeLayout RRAdd;
        EditText edtCategoryName;
        EditText edtAmt;
        TextView txtCurrentMonth;
        RelativeLayout RRSelctMonthWeek;
        RelativeLayout RRSelctDay;
        RelativeLayout RRselectWeeks;
        RelativeLayout RRcross;
        TextView txtSelectMonth;
        Spinner spinnerMonthDays;
        Spinner spinnerMonth;
        Spinner spinnerWeeks;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(mContext);
        promptsView = li.inflate(R.layout.alert_grp_category, null);
        RRAdd = (RelativeLayout) promptsView.findViewById(R.id.RRAdd);
        txtCurrentMonth = (TextView) promptsView.findViewById(R.id.txtCurrentMonth);
        edtCategoryName = (EditText) promptsView.findViewById(R.id.edtCategoryName);
        edtAmt = (EditText) promptsView.findViewById(R.id.edtAmt);
        RRcross = (RelativeLayout) promptsView.findViewById(R.id.RRcross);
        RRSelctDay = (RelativeLayout) promptsView.findViewById(R.id.RRSelctDay);
        RRselectWeeks = (RelativeLayout) promptsView.findViewById(R.id.RRselectWeeks);
        txtSelectMonth = (TextView) promptsView.findViewById(R.id.txtSelectMonth);
        spinnerMonthDays = (Spinner) promptsView.findViewById(R.id.spinnerMonthDays);
        spinnerMonth = (Spinner) promptsView.findViewById(R.id.spinnerMonth);
        spinnerWeeks = (Spinner) promptsView.findViewById(R.id.spinnerWeeks);
        RRSelctMonthWeek = (RelativeLayout) promptsView.findViewById(R.id.RRSelctMonthWeek);
        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);

        edtAmt.addTextChangedListener(new NumberTextWatcher(edtAmt,"#,###"));

        MonthDaysAdapter customAdapter=new MonthDaysAdapter(mContext,strAr12);
        spinnerMonth.setAdapter(customAdapter);

        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        month = month_date.format(cal.getTime());

        txtCurrentMonth.setText(month);

        RRcross.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        RRSelctMonthWeek.setOnClickListener(v -> {
        //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(mContext, RRSelctMonthWeek);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.poupup_menu, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().equals("Monthly"))
                    {
                        RRSelctDay.setVisibility(View.VISIBLE);
                        RRselectWeeks.setVisibility(View.GONE);
                        txtSelectMonth.setText(item.getTitle()+"");
                        select_Month_Week ="Monthly";

                        Calendar c = Calendar.getInstance();
                        int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

                        Log.e("monthMaxDays :>",""+monthMaxDays);

                        arr=new int[monthMaxDays];

                        AllDayDaysAdapter customAdapter=new AllDayDaysAdapter(mContext,arr);
                        spinnerMonthDays.setAdapter(customAdapter);

                    }else if(item.getTitle().equals("Weekly"))
                    {
                        RRSelctDay.setVisibility(View.GONE);
                        RRselectWeeks.setVisibility(View.VISIBLE);

                        select_Month_Week ="Weekly";

                        WeekdaysDaysAdapter customAdapter=new WeekdaysDaysAdapter(mContext,strAr1);
                        spinnerWeeks.setAdapter(customAdapter);

                        txtSelectMonth.setText(item.getTitle()+"");

                    }
                    return true;
                }
            });

            popup.show();

        });

        spinnerMonthDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                int endMonthDayEndWeekNew = arr[pos];
                endMonthDayEndWeek= String.valueOf(endMonthDayEndWeekNew);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spinnerWeeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                //   endMonthDayEndWeek = modelListCategory.get(pos).getId();
                endMonthDayEndWeek= strAr1[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                //   endMonthDayEndWeek = modelListCategory.get(pos).getId();
              //  month= strAr12[pos];
                Toast.makeText(mContext, ""+month, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        RRAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CategoryName=edtCategoryName.getText().toString();
                String Amt=edtAmt.getText().toString();

                if(select_Month_Week.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please Select month/Weeks", Toast.LENGTH_SHORT).show();

                }else if(endMonthDayEndWeek.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please Select End of month/Weeks", Toast.LENGTH_SHORT).show();

                }else if(CategoryName.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please Enter CategoryName", Toast.LENGTH_SHORT).show();

                }else if(Amt.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please Enter Amt", Toast.LENGTH_SHORT).show();

                }else
                {
                    /*select_Month_Week="";
                    endMonthDayEndWeek="";*/
                    AddGrpMethod(CategoryName,Grpid,Amt,month);
                    alertDialog.dismiss();

                }
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    private void AddGrpMethod(String CategoyrName,String GrpId,String Amt,String month){
        Call<AddCategoryModel> call = RetrofitClients.getInstance().getApi()
                .Api_add_category_group(USerId,GrpId,accountId,CategoyrName,Amt,select_Month_Week,endMonthDayEndWeek,month);
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

    public void AlertDaliogDelete(int position,String id) {

        LayoutInflater li;
        RelativeLayout RRDelete;
        EditText edtName;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(mContext);
        promptsView = li.inflate(R.layout.alert_delete_grp, null);
        RRDelete = (RelativeLayout) promptsView.findViewById(R.id.RRDelete);
        edtName = (EditText) promptsView.findViewById(R.id.edtName);
        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);

        RRDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                ((SetBudgetActivity)mContext).DeleteGrpMethod(id);
                modelList.remove(position);
                notifyDataSetChanged();

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

}

