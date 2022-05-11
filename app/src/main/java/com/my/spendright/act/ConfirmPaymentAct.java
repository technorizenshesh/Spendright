package com.my.spendright.act;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.Broadband.ConfirmPaymentBroadBandAct;
import com.my.spendright.ElectircalBill.Model.PayFinalModel;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.Model.AddReportModal;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityConfirmPaymentBinding;
import com.my.spendright.databinding.ActivityConfirmPaymentNewBinding;
import com.my.spendright.utils.ApiNew;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPaymentAct extends AppCompatActivity {

    ActivityConfirmPaymentNewBinding binding;

    String Request_IDNew="";
    String Request_ID="";
    String ServicesId="";
    String ServicesName="";
    String billersCode="";
    String variation_code="";
    String amount="";
    String phone="";
    String paymentdate="";
    String MyCuurentBlance="";
    private SessionManager sessionManager;

    String Current_date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_payment_new);

        sessionManager = new SessionManager(ConfirmPaymentAct.this);

        Log.d("Request_ID:>>",get_current_Time());

        Request_IDNew=get_current_Time();

        paymentdate= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        Intent intent=getIntent();

        if(intent !=null)
        {
             Request_ID =intent.getStringExtra("Request_ID");
             ServicesId =intent.getStringExtra("ServicesId");
             billersCode =intent.getStringExtra("billersCode");
             variation_code =intent.getStringExtra("variation_code");
             amount =intent.getStringExtra("amount");
             phone =intent.getStringExtra("billersCode");
            // paymentdate =intent.getStringExtra("paymentdate");
             MyCuurentBlance =intent.getStringExtra("MyCuurentBlance");

             binding.meterNumber.setText(billersCode);
             binding.MyCuurentBlance.setText(MyCuurentBlance);
             binding.Date.setText(paymentdate);
             binding.AmountPay.setText(amount);
             binding.totalAmountPay.setText(amount);
        }

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtCancel.setOnClickListener(v -> {
           finish();
        });

        binding.RRConfirm.setOnClickListener(v -> {
            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                PyaAccoun("harshit.ixora89@gmail.com","harshit89@");
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* request_id:20220506100925
    serviceID:ikeja-electric
    billersCode:1111111111111
    variation_code:prepaid
    amount:852
    phone:1111111111111*/

    private void PyaAccoun(final String username, final String password) {

        ApiNew loginService = RetrofitSetup.createService(ApiNew.class, username, password);
        Call<PayFinalModel> call = loginService.Api_pay(Request_IDNew,ServicesId,billersCode,variation_code,amount,phone);
        call.enqueue(new Callback<PayFinalModel>() {
            @Override
            public void onResponse(@NonNull Call<PayFinalModel> call, @NonNull Response<PayFinalModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        // user object available
                        PayFinalModel finallyPr = response.body();

                        if(finallyPr.getCode().equals("000"))
                        {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            AddReportMethod(finallyPr.getResponseDescription());

                            Toast.makeText(ConfirmPaymentAct.this, "SuccessFully Bill pay", Toast.LENGTH_SHORT).show();

                        }else
                        {
                            Toast.makeText(ConfirmPaymentAct.this, finallyPr.getResponseDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ConfirmPaymentAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<PayFinalModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ConfirmPaymentAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void AddReportMethod(String status){
        Current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //sessionManager.getUserID();
        Call<AddReportModal> call = RetrofitClients.getInstance().getApi()
                .Api_add_vtpass_book_payment(sessionManager.getUserID(),Request_IDNew,amount,ServicesId,ServicesName,
                        "Electricity",status,Current_date);
        call.enqueue(new Callback<AddReportModal>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<AddReportModal> call, Response<AddReportModal> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    AddReportModal finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        startActivity(new Intent(ConfirmPaymentAct.this,PaymentComplete.class));

                    } else
                    {
                        Toast.makeText(ConfirmPaymentAct.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<AddReportModal> call, Throwable t) {
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
        String SECOND = String.valueOf(c.get(Calendar.SECOND));

        if(monthFinal.length()==1)
        {
            monthFinal="0"+monthFinal;
        }

        if(mHour.length()==1)
        {
            mHour="0"+mHour;
        }

        if(Day.length()==1)
        {
            Day="0"+Day;
        }

        if(mMinute.length()==1)
        {
            mMinute="0"+mMinute;
        }

        return YEar+monthFinal+Day+mHour+mMinute+SECOND;

    }

}