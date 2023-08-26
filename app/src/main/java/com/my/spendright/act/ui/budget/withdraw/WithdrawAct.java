package com.my.spendright.act.ui.budget.withdraw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityWithdrawFundsBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawAct extends AppCompatActivity {
    ActivityWithdrawFundsBinding binding;
    SessionManager sessionManager;
    GetProfileModel finallyPr;
    String amount="",catId="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw_funds);
        sessionManager = new SessionManager(WithdrawAct.this);

        initViews();
    }

    private void initViews() {

        if(getIntent()!=null) {
            amount = getIntent().getStringExtra("amount");
            catId = getIntent().getStringExtra("catId");
        }

        binding.imgBack.setOnClickListener(view -> finish());

        binding.RRWallet.setOnClickListener(view -> startActivity(new Intent(this,WithdrawMyWalletAct.class)
                .putExtra("amount",amount)
                .putExtra("catId",catId)));

        binding.RROtherWallet.setOnClickListener(view -> startActivity(new Intent(this,WithdrawAnotherWalletAct.class)
                .putExtra("amount",amount)
                .putExtra("catId",catId)));

        binding.RRWithdrawBank.setOnClickListener(view -> startActivity(new Intent(this,WithdrawYourBankAct.class)
                .putExtra("amount",amount)
                .putExtra("catId",catId)));



    }

    @Override
    protected void onResume() {
        super.onResume();
        GetProfileMethod();
    }

    private void GetProfileMethod() {
        binding.progressBar.setVisibility (View.VISIBLE);
        Call<GetProfileModel> call = RetrofitClients.getInstance ().getApi ()
                .Api_get_profile_data (sessionManager.getUserID ());
        call.enqueue (new Callback<GetProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility (View.GONE);
                try {
                    finallyPr = response.body ();

                    if (finallyPr.getStatus ().equalsIgnoreCase ("1")) {
                        Log.e("refferece===",finallyPr.getResult()+"");

                    } else {

                      //  Toast.makeText (TransferFundAct.this, "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
                      //  binding.progressBar.setVisibility (View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    //binding.RRadd.setVisibility (View.VISIBLE);
                    e.printStackTrace ();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility (View.GONE);
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }
}