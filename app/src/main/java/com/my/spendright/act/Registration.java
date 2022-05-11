package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityRegistrationBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private int mYear, mMonth,mDay;
    String dob="";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_registration);

        sessionManager = new SessionManager(Registration.this);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
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
                            dob = (dayOfMonth+"-"+(monthOfYear)+"-"+year);
                            binding.edtDateOFBirth.setText(dob);

                           /* String age = getAge(year,(monthOfYear+1),dayOfMonth);
                            String CalcuAge= getAge(year,monthOfYear,dayOfMonth);
                            binding.edtDateOFBirth.setText(CalcuAge+" Years");*/
                        }
                    }, mYear, mMonth, mDay);
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

         binding.RRRegis.setOnClickListener(v ->
        {
            Validation();
          // startActivity(new Intent(Registration.this,RegistrationOne.class));
        });

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

        }else if(binding.edtCountry.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Country.", Toast.LENGTH_SHORT).show();

        }else if(dob.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter dob.", Toast.LENGTH_SHORT).show();

        }else if(binding.edtPassword.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                signUpMethod();
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signUpMethod(){
        Call<LoginModel> call = RetrofitClients.getInstance().getApi()
                .Api_signup(binding.edtFName.getText().toString(),binding.edtLName.getText().toString(),binding.edtemail.getText().toString(),binding.edtPassword.getText().toString(),
                        binding.edtMobile.getText().toString(),"75.00","75.00",binding.edtCity.getText().toString(),binding.edtCountry.getText().toString(),dob,"True","nncbnv");
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    LoginModel finallyPr = response.body();
                    String status = finallyPr.getStatus();

                    if (status.equalsIgnoreCase("1")) {
                        sessionManager.saveUserId(finallyPr.getResult().getId());
                        startActivity(new Intent(Registration.this,RegistrationOne.class));
                        finish();
                    } else {
                        Toast.makeText(Registration.this, ""+finallyPr.getStatus(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Registration.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}