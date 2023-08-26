package com.my.spendright.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.SetBudgetActivity;
import com.my.spendright.act.ui.budget.adapter.BeneficiaryBaseAdapter;
import com.my.spendright.act.ui.budget.model.BeneficiaryModel;
import com.my.spendright.act.ui.budget.withdraw.WithdrawYourBankAct;
import com.my.spendright.adapter.ManageBeneficiaryAdapter;
import com.my.spendright.databinding.ActivityManageBeneficiaryBinding;
import com.my.spendright.listener.BeneficiaryListener;
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

public class ManageBeneficiaryAct extends AppCompatActivity implements BeneficiaryListener {
    private String TAG = "ManageBeneficiaryAct";
    private SessionManager sessionManager;

    ActivityManageBeneficiaryBinding binding;

    ArrayList<BeneficiaryModel.Datum> beneficiaryArrayList;

    ManageBeneficiaryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_manage_beneficiary);
        sessionManager = new SessionManager(ManageBeneficiaryAct.this);
        initViews();
    }

    private void initViews() {
        beneficiaryArrayList = new ArrayList<>();

        binding.imgBack.setOnClickListener(view -> finish());

        adapter = new ManageBeneficiaryAdapter(ManageBeneficiaryAct.this,beneficiaryArrayList,ManageBeneficiaryAct.this);
        binding.rvBeneficiary.setAdapter(adapter);

        getBeneficiary();
    }


    private void getBeneficiary() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID()/*+"3"*/);
        Log.e(TAG, "get all Beneficiary account Request==" + requestBody.toString());
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_get_beneficiary(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    Log.e(TAG, "URL = " + loginCall.request().url());

                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "get all Beneficiary account Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        BeneficiaryModel beneficiaryModel = new Gson().fromJson(stringResponse, BeneficiaryModel.class);
                        binding.tvNotFound.setVisibility(View.GONE);

                        beneficiaryArrayList.clear();
                        for (int i =0;i<beneficiaryModel.getData().size();i++){
                            if(!beneficiaryModel.getData().get(i).getBankAccountName().equalsIgnoreCase("Add New Beneficiary")){
                                beneficiaryArrayList.add(beneficiaryModel.getData().get(i));

                            }
                        }
                        adapter.notifyDataSetChanged();



                    } else {
                        beneficiaryArrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotFound.setVisibility(View.VISIBLE);

                        // Toast.makeText(WithdrawYourBankAct.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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





    private void deleteBeneficiaryAct(String beneficiaryId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("beneficiary_id", beneficiaryId);
        requestBody.put("user_id", sessionManager.getUserID());
        Log.e(TAG, "delete Beneficiary AccountRequest==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_delete_beneficiary(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "delete Beneficiary AccountResponse = " + stringResponse);
                    if (sessionManager.isNetworkAvailable()) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        getBeneficiary();
                    } else {
                        Toast.makeText(ManageBeneficiaryAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                 /*  if (jsonObject.getString("status").equalsIgnoreCase("1")) {



                    } else {

                    }*/

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

    @Override
    public void OnBeneficiary(int position) {
        AlertDeleteBeneficiary(beneficiaryArrayList.get(position).getId());
    }


    public void AlertDeleteBeneficiary(String beneficiaryId){

        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ManageBeneficiaryAct.this);
        builder1.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_beneficiary_account));
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        deleteBeneficiaryAct(beneficiaryId);

                    }
                });


        builder1.setNegativeButton(
                getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
