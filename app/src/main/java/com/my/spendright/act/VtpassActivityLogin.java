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

import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.ElectircalBill.Model.GetVtsWalletBalnce;
import com.my.spendright.ElectircalBill.PaymentBill;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.ElectircalBill.UtilRetro.ServiceGenerator;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.PayMentCabilBillAct;
import com.my.spendright.airetime.PaymentBillAireTime;
import com.my.spendright.databinding.ActivityVtpassLoginBinding;
import com.my.spendright.utils.ApiNew;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsNew;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VtpassActivityLogin extends AppCompatActivity {

    ActivityVtpassLoginBinding binding;
    String Type="Electricity Bill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_vtpass_login);


        Intent intent=getIntent();
        if(intent!=null)
        {
            Type=intent.getStringExtra("Type").toString();
        }

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRlogin.setOnClickListener(v -> {

        // startActivity(new Intent(VtpassActivityLogin.this, PaymentBill.class));

            String UserName=binding.edtEmail.getText().toString();
            String Password=binding.edtPassword.getText().toString();
            if(UserName.equals(""))
            {
                Toast.makeText(this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
            }else  if(Password.equals(""))
            {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();

            }else
            {
                binding.progressBar.setVisibility(View.VISIBLE);
                loginWallet(UserName,Password);
            }
        });
    }


    private void loginWallet(final String username, final String password) {
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetVtsWalletBalnce> call = loginService.Api_balance();
        call.enqueue(new Callback<GetVtsWalletBalnce>() {
            @Override
            public void onResponse(@NonNull Call<GetVtsWalletBalnce> call, @NonNull Response<GetVtsWalletBalnce> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    GetVtsWalletBalnce finallyPr = response.body();

                    if(Type.equalsIgnoreCase("Electricity Bill"))
                    {
                        startActivity(new Intent(VtpassActivityLogin.this, PaymentBill.class).putExtra("Balance",""+finallyPr.getContents().getBalance()));
                        finish();

                    }else if(Type.equalsIgnoreCase("TVSubscription"))
                    {
                        startActivity(new Intent(VtpassActivityLogin.this, PayMentCabilBillAct.class).putExtra("Balance",""+finallyPr.getContents().getBalance()));
                        finish();

                    }else if(Type.equalsIgnoreCase("airetime"))
                    {
                        startActivity(new Intent(VtpassActivityLogin.this, PaymentBillAireTime.class).putExtra("Balance",""+finallyPr.getContents().getBalance()));
                        finish();
                    } else if(Type.equalsIgnoreCase("data"))
                    {
                        startActivity(new Intent(VtpassActivityLogin.this, PaymentBillBroadBandAct.class).putExtra("Balance",""+finallyPr.getContents().getBalance()));
                        finish();
                    }

                } else {
                    Toast.makeText(VtpassActivityLogin.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetVtsWalletBalnce> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(VtpassActivityLogin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* private void login_vts_Method(){
        Call<List<GetVtsWalletBalnce>> call = RetrofitClientsNew.getInstance().getApi()
                .Api_balance("harshit.ixora89@gmail.com","harshit89@");
        call.enqueue(new Callback<List<GetVtsWalletBalnce>>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<List<GetVtsWalletBalnce>> call, Response<List<GetVtsWalletBalnce>> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    List<GetVtsWalletBalnce> finallyPr = response.body();

                    if (finallyPr.get(0).getCode()==1) {
                        Toast.makeText(VtpassActivityLogin.this, "Success", Toast.LENGTH_SHORT).show();

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(VtpassActivityLogin.this, "error.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<GetVtsWalletBalnce>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(VtpassActivityLogin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
}