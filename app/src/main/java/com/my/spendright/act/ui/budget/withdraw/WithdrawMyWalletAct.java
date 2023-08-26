package com.my.spendright.act.ui.budget.withdraw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityWithdrawAnotherWalletBinding;
import com.my.spendright.databinding.ActivityWithdrawToWalletBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawMyWalletAct extends AppCompatActivity {
    private String TAG = "WithdrawMyWalletAct";
    private SessionManager sessionManager;
    ActivityWithdrawToWalletBinding binding;
    private String amount ="",catId="";
    GetProfileModel finallyPr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw_to_wallet);
        sessionManager = new SessionManager(WithdrawMyWalletAct.this);
        initView();
        GetProfileMethod();
    }

    private void initView() {

        if(getIntent()!=null) {
            binding.tvAmount.setText("₦"+ Preference.doubleToStringNoDecimal(Double.parseDouble(getIntent().getStringExtra("amount"))));
            catId = getIntent().getStringExtra("catId");
            amount = Preference.doubleToStringNoDecimal(Double.parseDouble(getIntent().getStringExtra("amount")));

        }





        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnContinue.setOnClickListener(view -> validation());

    }

    private void validation() {
        if(binding.edAmount.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(WithdrawMyWalletAct.this,getString(R.string.please_enter_amount),Toast.LENGTH_LONG).show();
        else if(Double.parseDouble(amount) < Double.parseDouble(binding.edAmount.getText().toString()))
            Toast.makeText(WithdrawMyWalletAct.this,getString(R.string.amount_must_be_lessthen_from_available_amount),Toast.LENGTH_LONG).show();
         else{
           //  transferToMyWallet(Double.parseDouble(binding.edAmount.getText().toString()));
             startActivity(new Intent(WithdrawMyWalletAct.this,WithdrawMyWalletConfirmAct.class)
                     .putExtra("catId",catId)
                     .putExtra("userName",finallyPr.getResult().getUserName())
                     .putExtra("name",finallyPr.getResult().getFirstName()+ " " + finallyPr.getResult().getLastName())
                     .putExtra("mobile",finallyPr.getResult().getMobile())
                     .putExtra("amount",Double.parseDouble(binding.edAmount.getText().toString())+""));
        }

    }



    private void GetProfileMethod() {
        Call<GetProfileModel> call = RetrofitClients.getInstance ().getApi ()
                .Api_get_profile_data (sessionManager.getUserID ());
        call.enqueue (new Callback<GetProfileModel> () {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility (View.GONE);
                try {
                    finallyPr = response.body ();

                    if (finallyPr.getStatus ().equalsIgnoreCase ("1")) {
                        sessionManager.saveAccountReference(finallyPr.getResult().getBatchId());
                        Log.e("refferece===",finallyPr.getResult().getUserName());

                    } else {

                        Toast.makeText (WithdrawMyWalletAct.this, "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
                        binding.progressBar.setVisibility (View.GONE);
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
//                binding.recyclermyAccount.setVisibility(View.GONE);
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }



/*    private void transferToMyWallet(Double transferAmt) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());
        requestBody.put("bcat_id",catId);
        requestBody.put("amount",transferAmt+"");
        requestBody.put("expense_traking_account_id",BudgetAccountId);
        requestBody.put("expense_traking_category_id",selectBugCategoryId);
        Log.e(TAG, "Transfer to My Wallet Request==" + requestBody.toString());
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_withdraw_to_my_wallet(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    Log.e(TAG, "URL = " + loginCall.request().url());
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Transfer to My Wallet Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        startActivity(new Intent(WithdrawMyWalletAct.this,WithdrawCompleteAct.class));
                        finish();

                    } else {

                        Toast.makeText(WithdrawMyWalletAct.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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




    }*/


}
