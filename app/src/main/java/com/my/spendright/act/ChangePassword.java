package com.my.spendright.act;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.Model.ChangePasswordModel;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityChangePasswordBinding;
import com.my.spendright.databinding.ActivityRegistrationOneBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    private SessionManager sessionManager;
    String CnewPassword="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password);

        sessionManager = new SessionManager(ChangePassword.this);

        binding.RRNext.setOnClickListener(v -> {

            String newPassword=binding.edtPassword.getText().toString();
            CnewPassword=binding.edtCPassword.getText().toString();

            if(newPassword.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();

            }else if(CnewPassword.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_SHORT).show();

            }else if(!newPassword.equalsIgnoreCase(CnewPassword))
            {
                Toast.makeText(this, "Don't match Password.", Toast.LENGTH_SHORT).show();

            }else
            {
                if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    ChnagePasswordMethod();
                }else {
                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void ChnagePasswordMethod(){

        Call<ChangePasswordModel> call = RetrofitClients.getInstance().getApi()
                .change_password(sessionManager.getUserID(),CnewPassword);
        call.enqueue(new Callback<ChangePasswordModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    ChangePasswordModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        startActivity(new Intent(ChangePassword.this,HomeActivity.class));
                        finish();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}