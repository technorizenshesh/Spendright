package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.my.spendright.Model.DeletePaymentModel;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetSchdulePaymentDetails;
import com.my.spendright.Model.GetSchdulepAymentModel;
import com.my.spendright.Model.SchdulepAymentModel;
import com.my.spendright.Model.UpdateSchdulepAymentModel;
import com.my.spendright.R;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.adapter.SchdulePaymenCategorytAdapter;
import com.my.spendright.adapter.SchdulePaymentAdapter;
import com.my.spendright.databinding.ActivitySchdulePaymentDetailsBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchdulePaymentDetails extends AppCompatActivity {

    ActivitySchdulePaymentDetailsBinding binding;
    String[] strAr1=new String[] {"Electical","Tv SubsCription","Local Airetime","Data SubsCription"};
    String[] strAr11=new String[] {"Investment","Giving","Subscription"};

    String AccountId="";
    String AccountType="";
    String AccountCategory="";
    String paymentId="";
    String amt="";

    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();
    private SessionManager sessionManager;
    private View promptsView;
    private AlertDialog alertDialog;

    private int mYear, mMonth,mDay;
    String paymentdate="";
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_schdule_payment_details);

        sessionManager = new SessionManager(SchdulePaymentDetails.this);

        Intent intent=getIntent();
        if(intent!=null)
        {
            paymentId = intent.getStringExtra("id").toString();
        }

        binding.RRBack.setOnClickListener(v -> {
            onBackPressed();
        });



        SchdulePaymenCategorytAdapter custo1mAdapter=new SchdulePaymenCategorytAdapter(this,strAr11);
        binding.spinnerCategory1.setAdapter(custo1mAdapter);



        binding.txtDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(SchdulePaymentDetails.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            binding.txtDate.setText(paymentdate);
                        }

                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();
        });

        binding.spinnerCategory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                AccountCategory=  strAr11[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                AccountId = modelListCategory.get(pos).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.RRDelete.setOnClickListener(v -> {

            AlertDaliogDelete();

        });

        binding.RRSave.setOnClickListener(v -> {

            amt= binding.edtAmt.getText().toString();
            AccountType= binding.edtPaymentType.getText().toString();

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                UpdateScdulePaymentMethos();
            }else {
                Toast.makeText(this,R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetAccountCategoryMethod();
            GetPaymentScdhuleDetails();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void GetAccountCategoryMethod()
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

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(SchdulePaymentDetails.this,modelListCategory);
                    binding.spinnerCategory.setAdapter(customAdapter);

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


    private void GetPaymentScdhuleDetails(){

        Call<GetSchdulePaymentDetails> call = RetrofitClients.getInstance().getApi()
                .getSchedulePaymentDetails(paymentId);
        call.enqueue(new Callback<GetSchdulePaymentDetails>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetSchdulePaymentDetails> call, Response<GetSchdulePaymentDetails> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetSchdulePaymentDetails finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                         amt=finallyPr.getResult().getAmount();

                        paymentdate=finallyPr.getResult().getScheduleDate();

                        binding.edtAmt.setText(amt);

                        binding.edtPaymentType.setText(finallyPr.getResult().getTypeOfPayment());

                        binding.txtDate.setText(paymentdate);

                    } else
                    {

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetSchdulePaymentDetails> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void deleteSchedulePayment(){
        Call<DeletePaymentModel> call = RetrofitClients.getInstance().getApi()
                .deleteSchedulePayment(paymentId);
        call.enqueue(new Callback<DeletePaymentModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<DeletePaymentModel> call, Response<DeletePaymentModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    DeletePaymentModel finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        finish();

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<DeletePaymentModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void UpdateScdulePaymentMethos(){
        String UserId=sessionManager.getUserID();
        Call<UpdateSchdulepAymentModel> call = RetrofitClients.getInstance().getApi()
                .updateSchedulePayment(paymentId,UserId,amt,AccountType,AccountCategory,paymentdate,AccountId);
        call.enqueue(new Callback<UpdateSchdulepAymentModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<UpdateSchdulepAymentModel> call, Response<UpdateSchdulepAymentModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    UpdateSchdulepAymentModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        finish();
                        Toast.makeText(SchdulePaymentDetails.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e)
                {
                    Toast.makeText(SchdulePaymentDetails.this, "UnSuccess", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<UpdateSchdulepAymentModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(SchdulePaymentDetails.this, "Please Check Your Network.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void AlertDaliogDelete() {

        LayoutInflater li;
        TextView txtSave;
        TextView txtCancel;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(SchdulePaymentDetails.this);
        promptsView = li.inflate(R.layout.alert_delete, null);
        txtSave = (TextView) promptsView.findViewById(R.id.txtSave);
        txtCancel = (TextView) promptsView.findViewById(R.id.txtCancel);
        alertDialogBuilder = new AlertDialog.Builder(SchdulePaymentDetails.this);
        alertDialogBuilder.setView(promptsView);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.progressBar.setVisibility(View.VISIBLE);
                deleteSchedulePayment();
                alertDialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


}