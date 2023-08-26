package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.SchdulepAymentModel;
import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.adapter.SchdulePaymenCategorytAdapter;
import com.my.spendright.adapter.SchdulePaymentAdapter;
import com.my.spendright.databinding.ActivitySchdulePaymentBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchdulePayment extends AppCompatActivity {
    
    ActivitySchdulePaymentBinding binding;
    private int mYear, mMonth,mDay;
    String paymentdate="";

    String AccountId="";
    String AccountType="";
    String AccountCategory="";
    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();
    private SessionManager sessionManager;

    String[] strAr1=new String[] {"Electical","Tv SubsCription","Local Airetime","Data SubsCription"};
    String[] strAr11=new String[] {"Investment","Giving","Subscription"};
    String Amt="";
    String PaymentType="";
    int arr[];

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_schdule_payment);

            binding.edtAmt.addTextChangedListener(new NumberTextWatcher(binding.edtAmt,"#,###"));


            sessionManager = new SessionManager(SchdulePayment.this);

        SchdulePaymentAdapter customAdapter=new SchdulePaymentAdapter(this,strAr1);
        binding.spinnerTypePayment.setAdapter(customAdapter);

        SchdulePaymenCategorytAdapter custo1mAdapter=new SchdulePaymenCategorytAdapter(this,strAr11);
        binding.spinnerCategory1.setAdapter(custo1mAdapter);

       binding.imgBack.setOnClickListener(v -> {
           onBackPressed();
       });

            binding.spinnerTypePayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                    AccountType= strAr1[pos];
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
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


        binding.txtDate.setOnClickListener(v -> {

           final Calendar c = Calendar.getInstance();
           mYear = c.get(Calendar.YEAR);
           mMonth = c.get(Calendar.MONTH);
           mDay = c.get(Calendar.DAY_OF_MONTH);

           DatePickerDialog datePickerDialog = new DatePickerDialog(SchdulePayment.this,
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

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetAccountCategoryMethod();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


            binding.RRpay.setOnClickListener(v -> {

                 PaymentType =binding.edtPaymentType.getText().toString();
                  Amt =binding.edtAmt.getText().toString();

                 if(PaymentType.equalsIgnoreCase(""))
                 {
                     Toast.makeText(this, "Please Enter Payment Type", Toast.LENGTH_SHORT).show();

                 }else if(Amt.equalsIgnoreCase(""))
                 {
                     Toast.makeText(this, "Please Enter Payment Amount.", Toast.LENGTH_SHORT).show();

                 }else if(paymentdate.equalsIgnoreCase(""))
                 {
                     Toast.makeText(this, "Please Enter Payment Date.", Toast.LENGTH_SHORT).show();
                 }else
                 {
                     if (sessionManager.isNetworkAvailable()) {
                         binding.progressBar.setVisibility(View.VISIBLE);
                         AddScdulePaymentMethos();
                     }else {
                         Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                     }
                 }
            });
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

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(SchdulePayment.this,modelListCategory);
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


    private void AddScdulePaymentMethos(){
       String UserId=sessionManager.getUserID();
        Call<SchdulepAymentModel> call = RetrofitClients.getInstance().getApi()
                .add_schedule_payment(UserId,Amt,PaymentType,AccountCategory,paymentdate,AccountId);
        call.enqueue(new Callback<SchdulepAymentModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<SchdulepAymentModel> call, Response<SchdulepAymentModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    SchdulepAymentModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        finish();
                        Toast.makeText(SchdulePayment.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(SchdulePayment.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(SchdulePayment.this, "Don't match email/mobile password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SchdulepAymentModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}