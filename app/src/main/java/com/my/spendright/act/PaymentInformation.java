package com.my.spendright.act;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.my.spendright.ElectircalBill.Model.GetMerchatAcocunt;
import com.my.spendright.ElectircalBill.Model.PayFinalModel;
import com.my.spendright.ElectircalBill.PaymentBill;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.Model.GetAccountCategory;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.R;
import com.my.spendright.adapter.CategoryAdapter;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.databinding.ActivityPaymentInformationBinding;
import com.my.spendright.databinding.ActivityRegistrationBinding;
import com.my.spendright.utils.ApiNew;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentInformation extends AppCompatActivity {

    ActivityPaymentInformationBinding binding;
    private int mYear, mMonth,mDay;
    String paymentdate="";
    private SessionManager sessionManager;
    String AccountId="";
    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();

    String myWalletBalace="";
    String Request_ID="";
    String ServicesId="";
    String billersCode="";
    String variation_code="";
    String amount="";
    String phone="";

    String ServicesName="";

    String miniAmt="";
    String maxAmt="";

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_information);

        Log.d("Request_ID:>>",get_current_Time());
        Request_ID=get_current_Time();
        //Toast.makeText(this, ""+get_current_Time(), Toast.LENGTH_SHORT).show();
        sessionManager = new SessionManager(PaymentInformation.this);

        Intent intent=getIntent();
        if(intent !=null)
        {
            String Customer_name =intent.getStringExtra("Customer_Name");
             billersCode =intent.getStringExtra("Meter_Number");
            String Address =intent.getStringExtra("Address");
             variation_code =intent.getStringExtra("type");
             ServicesId =intent.getStringExtra("ServicesId");
            myWalletBalace =intent.getStringExtra("myWalletBalace");
            ServicesName =intent.getStringExtra("ServicesName");
            miniAmt =intent.getStringExtra("miniAmt");
            maxAmt =intent.getStringExtra("maxAmt");

            binding.edtBillNumber.setText(billersCode+"");
            binding.edtCName.setText(Customer_name+"");
            binding.txtMinimum.setText(miniAmt+"");
            binding.txtMaximum.setText(maxAmt+"");

            binding.edtServicesId.setText(ServicesId+"");

        }

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
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

        binding.llPaymentDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(PaymentInformation.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            view.setVisibility(View.VISIBLE);
                            paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            binding.txtPaymentDate.setText(paymentdate);
                        }

                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


            datePickerDialog.show();
        });

        binding.RRPay.setOnClickListener(v -> {

            amount=binding.edtAmount.getText().toString();
            phone=binding.edtCMobile.getText().toString();

            if(phone.equalsIgnoreCase("")){

                Toast.makeText(this, "PLease enter phone Number.", Toast.LENGTH_SHORT).show();

            }else if(amount.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "PLease enter Amoun", Toast.LENGTH_SHORT).show();

            } else {

                startActivity(new Intent(PaymentInformation.this, ConfirmPaymentAct.class)
                        .putExtra("Request_ID",Request_ID)
                        .putExtra("ServicesId",ServicesId)
                        .putExtra("ServicesName",ServicesName)
                        .putExtra("billersCode",billersCode)
                        .putExtra("variation_code",variation_code)
                        .putExtra("amount",amount)
                        .putExtra("phone",phone)
                        .putExtra("paymentdate",paymentdate)
                        .putExtra("MyCuurentBlance",myWalletBalace)
                        );
/*
                if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    PyaAccoun("harshit.ixora89@gmail.com","harshit89@");
                }else {
                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
                */
            }
        });


       /* if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetAccountCategoryMethod();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }*/
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

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(PaymentInformation.this,modelListCategory);
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


    public String get_current_Time() {
        TimeZone tz = TimeZone.getTimeZone("GMT+1");
        Calendar c = Calendar.getInstance(tz);
        String YEar = String.valueOf(c.get(Calendar.YEAR));
        int month =c.get(Calendar.MONTH);
        int finalNew=month+1;
        String monthFinal = String.valueOf(finalNew);
        String Day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));

        if(monthFinal.length()==1)
        {
            monthFinal="0"+monthFinal;
        }

        if(Day.length()==1)
        {
            Day="0"+Day;
        }

        if(mMinute.length()==1)
        {
            mMinute="0"+mMinute;
        }

        if(mHour.length()==1)
        {
            mHour="0"+mHour;
        }

        return YEar+"0"+monthFinal+Day+mHour+mMinute;
    }

}