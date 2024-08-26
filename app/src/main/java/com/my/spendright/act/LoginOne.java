package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.Model.ResendOtpModel;
import com.my.spendright.Notification.MySMSBroadcastReceiver;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityLoginOneBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOne extends AppCompatActivity implements MySMSBroadcastReceiver.onSmsReadListener {
    public String TAG = "LoginOne";
    ActivityLoginOneBinding binding;
    private SessionManager sessionManager;

    String email="",userEmail="",otp="";
    Context mContext;
    String userId = "",countryCode="",phone="";
    TextView tv_email;
    HashMap<String,String> map;
    private String name="";
    private MySMSBroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login_one);

        tv_email = findViewById (R.id.tv_email);

        mContext=LoginOne.this;

        sessionManager = new SessionManager(LoginOne.this);

        if(getIntent()!=null){
           // userId = getIntent().getStringExtra("user_id");
            countryCode = getIntent().getStringExtra("countryCode");
            phone = getIntent().getStringExtra("phone_number");
            map = (HashMap<String, String>) getIntent().getExtras().get("hashMaps");


        }

      /*  // Register the SMS BroadcastReceiver
        smsReceiver = new MySMSBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);*/


        sendOtp();

       /* if(getIntent()!=null)
        {
            //  tv_email.setText(email);

            email= getIntent().getStringExtra("mobile").toString();
            countryCode = getIntent().getStringExtra("countryCode").toString();
            userEmail= getIntent().getStringExtra("email").toString();

            tv_email.setText(*//*"+"+*//*email);
              //.txtEmail.setText("The code has been sent to "+email+".Please enter the code below.");
        }*/

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.tvResend.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            sendOtp();

        });

        binding.RRNext.setOnClickListener(v -> {
            if(binding.Otp.getOTP().equals("")) Toast.makeText(LoginOne.this,"Please enter Otp",Toast.LENGTH_SHORT).show();
            else if(!binding.Otp.getOTP().equals(otp)) Toast.makeText(LoginOne.this,"Wrong Otp",Toast.LENGTH_SHORT).show();
            else signupUser();
        });

      //  init();
       // timer();



        binding.Otp.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                if ( binding.Otp.getOTP().length() == 4) {
                    // Call your API method here
                      if(!binding.Otp.getOTP().equals(otp)) Toast.makeText(LoginOne.this,"Wrong Otp",Toast.LENGTH_SHORT).show();
                    else signupUser();
                }
            }

            @Override
            public void onOTPComplete(String otp) {

            }
        });


    }

    public void timer(){
        new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                 binding.tvResend.setText( String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                binding.tvResend.setEnabled(false);
            }
            @Override
            public void onFinish() {
                binding.tvResend.setText(getString(R.string.resend));
                binding.tvResend.setEnabled(true);
                //  binding.llTimer.setVisibility(View.GONE);
                // binding.tvReSend.setVisibility(View.VISIBLE);
            }
        }.start();

    }





    private void CheckOtpMethod(String finalOtp){
        String UsrId=sessionManager.getUserID();
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_check_otp(UsrId,finalOtp,getCurrentTime123());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "ChkOtp   Response :" + stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        startActivity(new Intent(LoginOne.this,HomeActivity.class));
                        finish();

                    } else {
                        Toast.makeText(LoginOne.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginOne.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String getCurrentTime123() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
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
                        Toast.makeText(LoginOne.this, getString(R.string.an_otp_has_been_sent_phone_number), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoginOne.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginOne.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void signUpMethod_three(){
        Call<ResponseBody> call = RetrofitClientsOne.getInstance().getApi()
                .Api_signup_three(userId, binding.Otp.getOTP());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Signup two  Response :" + stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                        startActivity(new Intent(LoginOne.this,KYCAct.class)
                                .putExtra("user_id",jsonObject.getJSONObject("result").getString("id")));
                        finish();

                    } else {
                        Toast.makeText(LoginOne.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginOne.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signupUser(){
         binding.progressBar.setVisibility(View.VISIBLE);
         Call<ResponseBody> call = RetrofitClientsOne.getInstance().getApi()
                .Api_signup_user(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Signup Response :" + stringResponse);

                   /* if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        startActivity(new Intent(LoginOne.this,KYCAct.class)
                                .putExtra("user_id",jsonObject.getJSONObject("result").getString("id"))
                                .putExtra("mobile", jsonObject.getJSONObject("result").getString("mobile")*//*countryCode+phone*//*)
                                .putExtra("name",jsonObject.getJSONObject("result").getString("last_name")+jsonObject.getJSONObject("result").getString("other_legal_name"))
                                .putExtra("from","otpScreen"));

                    } else {
                        Toast.makeText(LoginOne.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }*/


                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        LoginModel finallyPr = new Gson().fromJson(stringResponse,LoginModel.class);
                        sessionManager.saveUserId(finallyPr.getResult().getId());
                      //  sessionManager.saveUserToken(finallyPr.getResult().getToken());
                     //   sessionManager.saveUserPas(binding.edtPassword.getText().toString());
                        Log.e("user_id=====",finallyPr.getResult().getId());
                        startActivity(new Intent(LoginOne.this,HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginOne.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginOne.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onSms(String otp) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver when no longer needed
     //   unregisterReceiver(smsReceiver);
    }

}
