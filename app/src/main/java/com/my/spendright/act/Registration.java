package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.my.spendright.Model.GetAccountCategory;
import com.my.spendright.Model.GetCountryModel;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.adapter.CategoryAdapter;
import com.my.spendright.adapter.CountryAdapter;
import com.my.spendright.databinding.ActivityRegistrationBinding;
import com.my.spendright.databinding.DialogFullscreenBinding;
import com.my.spendright.utils.ApiClient;
import com.my.spendright.utils.ApiMonnify;
import com.my.spendright.utils.Constant;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class Registration extends AppCompatActivity {
    public String TAG = "Registration";
    ActivityRegistrationBinding binding;
    private int mYear, mMonth,mDay;
    String dob="";
    String Country="";
    private SessionManager sessionManager;
    String newToken="";
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";


    private ArrayList<GetCountryModel.Result> modelListCategory = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_registration);

        sessionManager = new SessionManager(Registration.this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(runnable -> {
            newToken = runnable.getToken();
            Log.e( "Tokennnn" ,newToken);
        });

        binding.txtCountry.setCountryForPhoneCode(234);


        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                Country = modelListCategory.get(pos).getName();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.layTerms.setOnClickListener(v -> {
          fullscreenDialog(Registration.this);
        });

        binding.txtLogin.setOnClickListener(v ->
        {
           startActivity(new Intent(Registration.this,LoginActivity.class));
           finish();
        });

        binding.llDob.setOnClickListener(v ->
        {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Registration.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            dob = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            binding.edtDateOFBirth.setText(dob);

                           /* String age = getAge(year,(monthOfYear+1),dayOfMonth);
                            String CalcuAge= getAge(year,monthOfYear,dayOfMonth);
                            binding.edtDateOFBirth.setText(CalcuAge+" Years");*/
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

         binding.RRRegis.setOnClickListener(v ->
        {
            Validation();
          // startActivity(new Intent(Registration.this,RegistrationOne.class));
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetCountryMethod();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void Validation() {

        if(binding.edtMobile.getText().toString().equalsIgnoreCase(""))
        {
           Toast.makeText(this, "Please Enter Mobile Number.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtemail.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Email.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtFName.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter First Name.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtLName.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Last Name.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtStreetAddress.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Street Address.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtCity.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter City.", Toast.LENGTH_SHORT).show();

        }else if(dob.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter dob.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtPassword.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();

        }else if(!binding.checEdTerms.isChecked())
        {
            Toast.makeText(this, "I accept Terms &amp; Conditions.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
               // generateToken();
                generateFlAccount();
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static String getCurrentTime123() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    private void signUpMethod(String batchId){


        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_signup(binding.edtFName.getText().toString(),binding.edtLName.getText().toString(),binding.edtemail.getText().toString(),binding.edtPassword.getText().toString(),
                        binding.edtMobile.getText().toString(),"75.00","75.00",binding.edtCity.getText().toString(),Country,dob,"True",newToken,binding.txtCountry.getSelectedCountryCodeWithPlus()
                        ,"","","","","","",batchId,getCurrentTime123());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Signup  Response :" + stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        LoginModel finallyPr = new Gson().fromJson(stringResponse,LoginModel.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                            sessionManager.saveUserId(finallyPr.getResult().getId());
                            sessionManager.saveAccountReference(finallyPr.getResult().getBatchId());

                        }

                        startActivity(new Intent(Registration.this,LoginOne.class).putExtra("mobile",/*finallyPr.getResult().getCountryCode()+*/finallyPr.getResult().getMobile())
                                .putExtra("countryCode",finallyPr.getResult().getCountryCode())
                                .putExtra("email",finallyPr.getResult().getEmail()));
                        finish();

                    } else {
                        Toast.makeText(Registration.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Registration.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void GetCountryMethod()
    {
        Call<GetCountryModel> call = RetrofitClients.getInstance().getApi()
                .Api_get_country();
        call.enqueue(new Callback<GetCountryModel>() {
            @Override
            public void onResponse(Call<GetCountryModel> call, Response<GetCountryModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                GetCountryModel finallyPr = response.body();

                if (finallyPr.getStatus().equalsIgnoreCase("1"))
                {
                    modelListCategory = (ArrayList<GetCountryModel.Result>) finallyPr.getResult();

                    CountryAdapter customAdapter=new CountryAdapter(Registration.this,modelListCategory);
                    binding.spinnerCategory.setAdapter(customAdapter);

                }else {

                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetCountryModel> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(Registration.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void generateFlAccount() {
        ApiMonnify apiInterface = ApiClient.getClient1().create(ApiMonnify.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + Constant.FL_LIVE_SECRET_KEY);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("tx_ref","reference"+getRandomNumberString());
      //  requestBody.put("accounts",2+"" );
        requestBody.put("email",binding.edtemail.getText().toString() );
        requestBody.put("is_permanent", String.valueOf(true));
        requestBody.put("bvn",binding.edtBvn.getText().toString().trim());
        requestBody.put("phonenumber",binding.edtMobile.getText().toString());
        requestBody.put("firstname",binding.edtFName.getText().toString());
        requestBody.put("lastname",binding.edtLName.getText().toString());
        requestBody.put("amount",1+"");

        requestBody.put("narration",/*"new account created"*/ binding.edtFName.getText().toString() + " " + binding.edtLName.getText().toString() );

        Log.e("FlutterwaveAccount",requestBody.toString());

        Call<ResponseBody> loginCall = apiInterface.Api_generate_account(headers,requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "FlutterwaveAccount Response = " + stringResponse);
                        Log.e(TAG, "code Response = " + response.code());

                    if(response.code() == 200) {
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            if (sessionManager.isNetworkAvailable()) {
                                binding.progressBar.setVisibility(View.VISIBLE);
                                //signUpMethod(object.getString("batch_id"));
                                signUpMethod(object.getString("order_ref"));


                            } else {
                                Toast.makeText(Registration.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Registration.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                    //    String stringResponse = response.body().string();
                   //     JSONObject jsonObject = new JSONObject(stringResponse);
                   //     Log.e(TAG, "FlutterwaveAccount Response = " + stringResponse);
                   //     Toast.makeText(Registration.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }





    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }



    public  String toBase64(String message) {
        byte[] data;
        // data = message.getBytes("UTF-8");
        String base64Sms = Base64.encodeToString(message.getBytes(), Base64.NO_WRAP);
        // Base64.encodeToString(data, Base64.NO_WRAP);
        return base64Sms;

       // return null;
    }


    public void fullscreenDialog(Context context){
        Dialog dialogFullscreen = new Dialog(context, WindowManager.LayoutParams.MATCH_PARENT);

       DialogFullscreenBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_fullscreen, null, false);
        dialogFullscreen.setContentView(dialogBinding.getRoot());

        WebSettings webSettings = dialogBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        dialogBinding.webView.loadUrl("https://spendright.ng/spendright/admin/termcondition");

        dialogBinding.tvCancel.setOnClickListener(v -> dialogFullscreen.dismiss());

        dialogBinding.tvOk.setOnClickListener(v -> dialogFullscreen.dismiss());


        dialogFullscreen.show();

    }


}