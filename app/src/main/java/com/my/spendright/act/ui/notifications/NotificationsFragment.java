package com.my.spendright.act.ui.notifications;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.Model.ReportModal;
import com.my.spendright.R;
import com.my.spendright.act.PaiChartAct;
import com.my.spendright.act.PaymentReport;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.act.SettingActivity;
import com.my.spendright.act.UpdatedAccountInfoTrasaction;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.adapter.CategoryAdapterNewFilter;
import com.my.spendright.adapter.ExpencePaymentAdapter;
import com.my.spendright.adapter.PaymentReportAdapter;
import com.my.spendright.databinding.FragmentNotificationsBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationsFragment extends Fragment {

    FragmentNotificationsBinding binding;
    private SessionManager sessionManager;
    ArrayList<GetExpenSeReport.Result> modelList=new ArrayList<>();
    ExpencePaymentAdapter mAdapter;

    private int mYear, mMonth,mDay;
    String paymentdate="";


    String ToDate="";
    String frmDate="";

    String FrmAmt="";
    String ToAmt="";

    String totalInCOmes="";
    String totalExpences="";

    String Transaction_Type="";

    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notifications, container, false);

        sessionManager = new SessionManager(getActivity());

        setUpNavigationDrawer();

       binding.RRimgSetting.setOnClickListener(v -> {
           startActivity(new Intent(getActivity(), SettingActivity.class));
       });

       /*binding.imgFilter.setOnClickListener(v -> {
           startActivity(new Intent(getActivity(), FIlterActivity.class));
       });*/

       binding.imgGraph.setOnClickListener(v -> {
           startActivity(new Intent(getActivity(), PaiChartAct.class)
                   .putExtra("income",totalInCOmes)
                   .putExtra("expence",totalExpences));
         //  binding.llgraph.setVisibility(View.GONE);
           //binding.RRgraph.setVisibility(View.VISIBLE);
       });

       binding.imgGraph1.setOnClickListener(v -> {
           binding.llgraph.setVisibility(View.VISIBLE);
           binding.RRgraph.setVisibility(View.GONE);
       });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetPaymentReportMethod();
            GetAccountBudgetMethod();
        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(ArrayList<GetExpenSeReport.Result> modelList)
    {
        mAdapter = new ExpencePaymentAdapter(getActivity(),modelList);
        binding.recycleViewReport.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleViewReport.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleViewReport.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new ExpencePaymentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetExpenSeReport.Result model) {

               startActivity(new Intent(getActivity(), UpdatedAccountInfoTrasaction.class).putExtra("trasaction_id",model.getId()));

            }
        });
    }

    private void GetAccountBudgetMethod()
    {
        Call<GetCategoryModelNew> call = RetrofitClients.getInstance().getApi()
                .Api_get_account_detail(sessionManager.getUserID());
        call.enqueue(new Callback<GetCategoryModelNew>() {
            @Override
            public void onResponse(Call<GetCategoryModelNew> call, Response<GetCategoryModelNew> response) {

                binding.progressBar.setVisibility(View.GONE);

                GetCategoryModelNew finallyPr = response.body();

                if (finallyPr.getStatus().equalsIgnoreCase("1"))
                {
                    modelListCategory = (ArrayList<GetCategoryModelNew.Result>) finallyPr.getResult();

                    if (getActivity()!=null){

                        CategoryAdapterNewFilter customAdapter=new CategoryAdapterNewFilter(getActivity(),modelListCategory);
                        binding.drawerFilterLayout.spinnerBudgetAct.setAdapter(customAdapter);
                    }
                }else {

                    binding.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<GetCategoryModelNew> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void GetPaymentReportMethod(){
        modelList.clear();
        Call<GetExpenSeReport> call = RetrofitClients.getInstance().getApi()
               .get_vtpass_history_search(sessionManager.getUserID(),FrmAmt,ToAmt,frmDate,ToDate,Transaction_Type,"");
        call.enqueue(new Callback<GetExpenSeReport>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetExpenSeReport> call, Response<GetExpenSeReport> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetExpenSeReport finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                       // binding.txtExpence.setText("Total Expenses-"+finallyPr.getTotalExpense());

                         totalInCOmes= finallyPr.getTotalIncome();
                         totalExpences= finallyPr.getTotalExpense();
                        binding.txtIncome.setText(totalInCOmes);
                        binding.txtExpence.setText(totalExpences);

                        modelList= (ArrayList<GetExpenSeReport.Result>) finallyPr.getResult();

                        setAdapter(modelList);

                    } else
                    {

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetExpenSeReport> call, Throwable t) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setUpNavigationDrawer() {

        binding.imgFilter.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);
        });

        binding.drawerFilterLayout.RRBack.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);
        });

        binding.drawerFilterLayout.llFrmDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);

                           // paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

                            paymentdate = (year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

                            frmDate  = paymentdate;

                            binding.drawerFilterLayout.txtDate.setText(paymentdate);
                        }

                    }, mYear, mMonth, mDay);

         //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();

        });

        binding.drawerFilterLayout.llToDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            //paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            paymentdate = (year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                            ToDate = paymentdate;
                            binding.drawerFilterLayout.txtToDAte.setText(paymentdate);
                        }
                    }, mYear, mMonth, mDay);
          //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();

        });

        binding.drawerFilterLayout.RRFilter.setOnClickListener(v -> {


            if(binding.drawerFilterLayout.RadbtnAll.isChecked())
            {
                Transaction_Type="";

                binding.txtAll.setText("All transactions");

            }else if(binding.drawerFilterLayout.RadbtnInCOme.isChecked())
            {
                Transaction_Type="income";
                binding.txtAll.setText("All income");

            }else if(binding.drawerFilterLayout.RadbtnExpence.isChecked())
            {
                Transaction_Type="expense";
                binding.txtAll.setText("All expense");
            }

            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);

             FrmAmt= binding.drawerFilterLayout.edtFrmAmt.getText().toString();
             ToAmt= binding.drawerFilterLayout.edtToAmt.getText().toString();

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                GetPaymentReportMethod();
            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });

        binding.drawerFilterLayout.txtReset.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);

            binding.drawerFilterLayout.txtDate.setText("00-00-00");
            binding.drawerFilterLayout.txtToDAte.setText("00-00-00");
            binding.drawerFilterLayout.edtFrmAmt.setText("00");
            binding.drawerFilterLayout.edtToAmt.setText("00");
            binding.txtAll.setText("All transactions");
            binding.drawerFilterLayout.RadbtnAll.setChecked(true);

             paymentdate="";
             ToDate="";
             frmDate="";
             FrmAmt="";
             ToAmt="";
             Transaction_Type="";

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                GetPaymentReportMethod();
            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });

       /* binding.drawerFilterLayout.txtAll.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                GetPaymentReportMethod();
            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

}