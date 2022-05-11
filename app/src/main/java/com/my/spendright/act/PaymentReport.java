package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.HomeModal;
import com.my.spendright.Model.ReportModal;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.adapter.TvSusCriptionChnageAdapter;
import com.my.spendright.adapter.PaymentReportAdapter;
import com.my.spendright.databinding.ActivityPaymentReportBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentReport extends AppCompatActivity {

    ActivityPaymentReportBinding binding;

    PaymentReportAdapter mAdapter;
    ArrayList<ReportModal.Result> modelList=new ArrayList<>();
    private SessionManager sessionManager;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_report);

        sessionManager = new SessionManager(PaymentReport.this);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtDate.setOnClickListener(v -> {

            startActivity(new Intent(PaymentReport.this,PaymentReportDateWiseAct.class));

        });

        binding.TxtFDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(PaymentReport.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            binding.TxtFDate.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();

        });

        binding.TxtFDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(PaymentReport.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            binding.Todate.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        });


        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetPaymentReportMethod();
        }else {
            Toast.makeText(PaymentReport.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }


    private void setAdapter(ArrayList<ReportModal.Result> modelList)
    {
        mAdapter = new PaymentReportAdapter(PaymentReport.this, this.modelList);
        binding.recycleViewReport.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentReport.this);
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
        //sessionManager.getUserID();
        Call<ReportModal> call = RetrofitClients.getInstance().getApi()
                .get_vtpass_book_payment(sessionManager.getUserID());
        call.enqueue(new Callback<ReportModal>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ReportModal> call, Response<ReportModal> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    ReportModal finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        binding.txtExpence.setText("Total Expenses-"+finallyPr.getTotalExpense());

                        modelList= (ArrayList<ReportModal.Result>) finallyPr.getResult();
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
            public void onFailure(Call<ReportModal> call, Throwable t) {
            }
        });
    }

}