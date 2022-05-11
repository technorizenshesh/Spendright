package com.my.spendright.airetime;

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

import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.Model.AddReportModal;
import com.my.spendright.R;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.airetime.model.PayAirtimeModel;
import com.my.spendright.databinding.ActivityConfirmForiengnBinding;
import com.my.spendright.databinding.ActivityConfirmPaymentAirtimeBinding;
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

public class ConfirmPaymentAireTimeForiegnAct extends AppCompatActivity {

    ActivityConfirmForiengnBinding binding;

    String Request_IDNew="";
    private SessionManager sessionManager;
    String serviceID="";
    //String serviceID_sadBox="airtel-data";
    String billersCode="";
    String variation_code="";
    String Amount="";
    String phone="";
    String operator_id="";
    String country_code="";
    String product_type_id="";
    String Email="";
    String myWalletBalace="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_foriengn);

        sessionManager = new SessionManager(ConfirmPaymentAireTimeForiegnAct.this);

        Log.d("Request_ID:>>",get_current_Time());

        Request_IDNew=get_current_Time();

        Intent intent=getIntent();

        if(intent !=null)
        {
            serviceID =intent.getStringExtra("ServicesId");
            billersCode =intent.getStringExtra("billersCode");
            variation_code =intent.getStringExtra("variation_code");
            Amount =intent.getStringExtra("amount");
            phone =intent.getStringExtra("phone");
            operator_id =intent.getStringExtra("operator_id");
            country_code =intent.getStringExtra("country_code");
            product_type_id =intent.getStringExtra("product_type_id");
            Email =intent.getStringExtra("email");
            myWalletBalace =intent.getStringExtra("myWalletBalace");

             binding.MyCuurentBlance.setText(myWalletBalace);
             binding.AmountPay.setText(Amount);
             binding.totalAmountPay.setText(Amount);
             binding.txtMobile.setText(phone);
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

    private void PyaAccoun(final String username, final String password) {

        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<PayAirtimeModel> call = loginService.Api_pay_airtime_forin(Request_IDNew,serviceID,billersCode
        ,variation_code,Amount,phone,operator_id,country_code,product_type_id,Email);
        call.enqueue(new Callback<PayAirtimeModel>() {
            @Override
            public void onResponse(@NonNull Call<PayAirtimeModel> call, @NonNull Response<PayAirtimeModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    PayAirtimeModel finallyPr = response.body();

                    if(finallyPr.getCode().equals("000"))
                    {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        AddReportMethod(finallyPr.getResponseDescription());
                        Toast.makeText(ConfirmPaymentAireTimeForiegnAct.this, "SuccessFully Bill pay", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        Toast.makeText(ConfirmPaymentAireTimeForiegnAct.this,finallyPr.getResponseDescription(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(ConfirmPaymentAireTimeForiegnAct.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<PayAirtimeModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }



    private void AddReportMethod(String status){

        String Current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //sessionManager.getUserID();
        Call<AddReportModal> call = RetrofitClients.getInstance().getApi()
                .Api_add_vtpass_book_payment(sessionManager.getUserID(),Request_IDNew,Amount,serviceID,"foreign-airtime",
                        "airtime_forin",status,Current_date);
        call.enqueue(new Callback<AddReportModal>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<AddReportModal> call, Response<AddReportModal> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    AddReportModal finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        startActivity(new Intent(ConfirmPaymentAireTimeForiegnAct.this, PaymentComplete.class));
                                     finish();
                    } else
                    {
                        Toast.makeText(ConfirmPaymentAireTimeForiegnAct.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
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