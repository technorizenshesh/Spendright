package com.my.spendright.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.spendright.Model.ReportModal;
import com.my.spendright.R;
import com.my.spendright.adapter.PaymentReportAdapter;
import com.my.spendright.databinding.ActivityPaymentReportBinding;
import com.my.spendright.databinding.ActivityPaymentReportDateWiseBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentReportDateWiseAct extends AppCompatActivity {

    ActivityPaymentReportDateWiseBinding binding;

    PaymentReportAdapter mAdapter;
    ArrayList<ReportModal.Result> modelList=new ArrayList<>();
    private SessionManager sessionManager;
    private int mYear;
    private int mMonth;
    private int mDay;

    String FromDate="";
    String ToDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_report_date_wise);

        sessionManager = new SessionManager(PaymentReportDateWiseAct.this);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtSubmit.setOnClickListener(v -> {

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                GetPaymentReportMethod();
            }else {
                Toast.makeText(PaymentReportDateWiseAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        });


        binding.TxtFDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(PaymentReportDateWiseAct.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            // Display Selected date in textbox

                           int monthfinal=monthOfYear+1;

                            if(monthfinal<10)
                            {
                                FromDate=year + "-"+"0"+ (monthOfYear + 1)+"-";

                            }else
                            {
                                FromDate=year + "-"+(monthOfYear + 1)+"-";
                            }

                            if(dayOfMonth<10)
                            {
                                FromDate =FromDate+"0"+dayOfMonth;

                            }else
                            {
                                FromDate =FromDate+dayOfMonth;
                            }

                            binding.TxtFDate.setText(FromDate);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();

        });

        binding.Todate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(PaymentReportDateWiseAct.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            int monthfinal=monthOfYear+1;

                            if(monthfinal<10)
                            {
                                ToDate=year + "-"+"0"+ (monthOfYear + 1)+"-";

                            }else
                            {
                                ToDate=year + "-"+(monthOfYear + 1)+"-";
                            }

                            if(dayOfMonth<10)
                            {
                                ToDate =ToDate+"0"+dayOfMonth;

                            }else
                            {
                                ToDate =ToDate+dayOfMonth;
                            }

                            binding.Todate.setText(ToDate);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        });
    }


    private void setAdapter(ArrayList<ReportModal.Result> modelList)
    {
        mAdapter = new PaymentReportAdapter(PaymentReportDateWiseAct.this,modelList);
        binding.recycleViewReport.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentReportDateWiseAct.this);
        binding.recycleViewReport.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleViewReport.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new PaymentReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ReportModal.Result model) {

            }
        });
    }

    private void GetPaymentReportMethod(){
       // sessionManager.getUserID();
        modelList.clear();
        Call<ReportModal> call = RetrofitClients.getInstance().getApi()
                .get_stateMent(sessionManager.getUserID(),FromDate,ToDate);
        call.enqueue(new Callback<ReportModal>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ReportModal> call, Response<ReportModal> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    ReportModal finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        binding.txtExpence.setVisibility(View.VISIBLE);
                        binding.txtExpence.setText("Total Expenses-"+finallyPr.getTotalExpense());
                        modelList= (ArrayList<ReportModal.Result>) finallyPr.getResult();

                        setAdapter(modelList);

                    } else
                    {
                        binding.txtExpence.setVisibility(View.GONE);
                        Toast.makeText(PaymentReportDateWiseAct.this, "Data not Found.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    binding.txtExpence.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ReportModal> call, Throwable t) {
                binding.txtExpence.setVisibility(View.GONE);
            }
        });
    }

}