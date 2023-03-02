package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.spendright.Model.ForGotPassword;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityForogtPasswordBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForogtPassword extends AppCompatActivity {

    ActivityForogtPasswordBinding binding;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forogt_password);

        sessionManager = new SessionManager(ForogtPassword.this);

        binding.RRNext.setOnClickListener(v -> {

            if((binding.edtEmail.getText().toString().equalsIgnoreCase("")))
            {
                Toast.makeText(this, "PLease Enter Email", Toast.LENGTH_SHORT).show();
            }else
            {
                if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    ForogtPAsswordMethod();
                }else {
                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ForogtPAsswordMethod(){
        Call<ForGotPassword> call = RetrofitClients.getInstance().getApi()
                .Api_forgot_password(binding.edtEmail.getText().toString());
        call.enqueue(new Callback<ForGotPassword>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ForGotPassword> call, Response<ForGotPassword> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    ForGotPassword finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        Toast.makeText(ForogtPassword.this, "send mail successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForogtPassword.this,LoginActivity.class));

                    }else
                    {
                        Toast.makeText(ForogtPassword.this, "Please check email.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(ForogtPassword.this, "send mail successfull", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ForGotPassword> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ForogtPassword.this, "PLease Check Your NetWork.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}