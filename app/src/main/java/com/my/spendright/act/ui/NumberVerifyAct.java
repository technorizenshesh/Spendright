package com.my.spendright.act.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.act.ui.settings.ProfileUpdateAct;
import com.my.spendright.databinding.ActivityLoginOneBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberVerifyAct  extends AppCompatActivity {
    public String TAG = "NumberVerifyAct";
    ActivityLoginOneBinding binding;
    private SessionManager sessionManager;

    String email="",userEmail="",otp="";
    Context mContext;
    String countryCode="",phone="",lastName="",otherName="",address="",state="";
    TextView tv_email;
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login_one);

        tv_email = findViewById (R.id.tv_email);

        mContext= NumberVerifyAct.this;

        sessionManager = new SessionManager(NumberVerifyAct.this);

        if(getIntent()!=null){
            countryCode = getIntent().getStringExtra("countryCode");
            phone = getIntent().getStringExtra("phoneNumber");
            lastName = getIntent().getStringExtra("lastName");
            otherName = getIntent().getStringExtra("otherName");
            address = getIntent().getStringExtra("address");
            state = getIntent().getStringExtra("state");




            map = (HashMap<String, String>) getIntent().getExtras().get("hashMaps");



        }

        sendOtp();

     

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.tvResend.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            sendOtp();

        });

        binding.RRNext.setOnClickListener(v -> {
            if(binding.Otp.getOTP().equals("")) Toast.makeText(NumberVerifyAct.this,"Please enter Otp",Toast.LENGTH_SHORT).show();
            else if(!binding.Otp.getOTP().equals(otp)) Toast.makeText(NumberVerifyAct.this,"Wrong Otp",Toast.LENGTH_SHORT).show();
            else UpdateProfileMethod();
        });

       




    }

    private void UpdateProfileMethod(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_user_update_profile(sessionManager.getUserID(),
                        lastName,
                        otherName,
                        phone,
                        countryCode,
                        address,
                        state);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Update profile  Response :" + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(NumberVerifyAct.this, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show();
                         finish();
                    } else {
                        Toast.makeText(NumberVerifyAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(NumberVerifyAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void sendOtp(){
        Call<ResponseBody> call = RetrofitClientsOne.getInstance().getApi()
                .Api_send_otp(countryCode+phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Signup two  Response :" + stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        otp =  jsonObject.getString("result");
                        Toast.makeText(NumberVerifyAct.this, getString(R.string.an_otp_has_been_sent_phone_number), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(NumberVerifyAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(NumberVerifyAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





}
