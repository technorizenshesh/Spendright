package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.Model.GetCountryModel;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.adapter.CountryAdapter;
import com.my.spendright.databinding.ActivityMyProfileBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileAct extends AppCompatActivity {

    ActivityMyProfileBinding binding;
    SessionManager sessionManager;
    private ArrayList<GetCountryModel.Result> modelListCategory = new ArrayList<>();

    String Country="";
    private int mYear, mMonth,mDay;
    String dob="";
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_profile);

        binding.edtMobile.setEnabled(false);
        binding.edtemail.setEnabled(false);
        binding.edtFName.setEnabled(false);
        binding.edtCity.setEnabled(false);
        binding.edtDateOFBirth.setEnabled(false);
        binding.edtLName.setEnabled(false);
        binding.edtStreetAddress.setEnabled(false);
        binding.RRSave.setVisibility(View.GONE);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

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


        binding.imgEdit.setOnClickListener(v -> {
            binding.imgEdit.setVisibility(View.GONE);
            binding.edtMobile.setEnabled(true);
            binding.edtemail.setEnabled(false);
            binding.edtFName.setEnabled(true);
            binding.edtCity.setEnabled(true);
            binding.edtDateOFBirth.setEnabled(true);
            binding.edtLName.setEnabled(true);
            binding.edtStreetAddress.setEnabled(true);
            binding.RRSave.setVisibility(View.VISIBLE);

        });

        binding.RRSave.setOnClickListener(v -> {
            binding.imgEdit.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            UpdateProfileMethod();
        });

        binding.llDob.setOnClickListener(v ->
        {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MyProfileAct.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            dob = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            binding.edtDateOFBirth.setText(dob);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        sessionManager = new SessionManager(MyProfileAct.this);

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);

            GetCountryMethod();

            GetMyMethod();

        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void GetMyMethod(){
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_get_profile(Preference.getHeader(MyProfileAct.this),sessionManager.getUserID());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        LoginModel finallyPr = new Gson().fromJson(stringResponse, LoginModel.class); // response.body();
                        binding.edtMobile.setText(finallyPr.getResult().getMobile());
                        binding.edtemail.setText(finallyPr.getResult().getEmail());
                        binding.edtFName.setText(finallyPr.getResult().getFirstName());
                        binding.edtCity.setText(finallyPr.getResult().getCity());
                        binding.edtDateOFBirth.setText(finallyPr.getResult().getDob());
                        binding.edtLName.setText(finallyPr.getResult().getLastName());
                        binding.edtStreetAddress.setText(finallyPr.getResult().getCountryResidence());

                        int position=getSelected(finallyPr.getResult().getCountryResidence());

                        binding.spinnerCategory.setSelection(position);

                        binding.txtCountry.setCountryForPhoneCode(Integer.parseInt(finallyPr.getResult().getCountryCode()));

                        //binding.txtCountry.setCountryForPhoneCode(+93);

                    }


                    else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                        sessionManager.logoutUser();
                        Toast.makeText(MyProfileAct.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MyProfileAct.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    else {
                        Toast.makeText(MyProfileAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(MyProfileAct.this, "Don't match email/mobile password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MyProfileAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateProfileMethod(){
        Call<LoginModel> call = RetrofitClients.getInstance().getApi()
                .Api_update_profile( sessionManager.getUserID(),binding.edtFName.getText().toString(),binding.edtLName.getText().toString(),binding.edtemail.getText().toString(),
                        binding.edtMobile.getText().toString(),"75.00","75.00",binding.edtCity.getText().toString(),dob,Country,binding.txtCountry.getSelectedCountryCodeWithPlus());
        call.enqueue(new Callback<LoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    LoginModel finallyPr = response.body();
                    String status = finallyPr.getStatus();

                    if (status.equalsIgnoreCase("1")) {
                        sessionManager.saveUserId(finallyPr.getResult().getId());

                        binding.edtMobile.setText(finallyPr.getResult().getMobile());
                        binding.edtemail.setText(finallyPr.getResult().getEmail());
                        binding.edtFName.setText(finallyPr.getResult().getFirstName());
                        binding.edtCity.setText(finallyPr.getResult().getCity());
                        binding.edtDateOFBirth.setText(finallyPr.getResult().getDob());
                        binding.edtLName.setText(finallyPr.getResult().getLastName());
                        binding.edtStreetAddress.setText(finallyPr.getResult().getCountryResidence());


                        binding.imgEdit.setVisibility(View.VISIBLE);
                        binding.edtMobile.setEnabled(false);
                        binding.edtemail.setEnabled(false);
                        binding.edtFName.setEnabled(false);
                        binding.edtCity.setEnabled(false);
                        binding.edtDateOFBirth.setEnabled(false);
                        binding.edtLName.setEnabled(false);
                        binding.edtStreetAddress.setEnabled(false);
                        binding.RRSave.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(MyProfileAct.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MyProfileAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getSelected(String nm)
    {
        int i=0;
        for(GetCountryModel.Result result:modelListCategory)
        {
            if(nm.equalsIgnoreCase(result.getName()))
            {
                return i;
            }
            i++;
        }

        return -1;
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

                    CountryAdapter customAdapter=new CountryAdapter(MyProfileAct.this,modelListCategory);
                    binding.spinnerCategory.setAdapter(customAdapter);

                }else {

                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(MyProfileAct.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetCountryModel> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MyProfileAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}