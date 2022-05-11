package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.my.spendright.Model.AddAcountModel;
import com.my.spendright.Model.GetAccountCategory;
import com.my.spendright.R;
import com.my.spendright.act.SetBudget.SetBudgetActivity;
import com.my.spendright.adapter.CategoryAdapter;
import com.my.spendright.databinding.ActivityAddBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;
    private ArrayList<GetAccountCategory.Result> modelListCategory = new ArrayList<>();
    private SessionManager sessionManager;
    String CategoryAccountId="";
    String UserId;
    String AccountId="";
    String AccountName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add);

        sessionManager = new SessionManager(AddActivity.this);

         binding.imgBack.setOnClickListener(v -> {
           onBackPressed();
        });

         binding.RRAdd.setOnClickListener(v -> {
             validation();
         });

          binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

               CategoryAccountId = modelListCategory.get(pos).getId();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

      if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetAccountCategoryMethod();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void validation() {

        if(binding.edtAccontHolderName.getText().equals(""))
        {
            Toast.makeText(this, "Please Enter Account nicK name", Toast.LENGTH_SHORT).show();

        }else if(binding.edtHolderBalnce.getText().equals(""))
        {
            Toast.makeText(this, "Please Enter Account nicK name", Toast.LENGTH_SHORT).show();
        }else
        {
            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                AddAccountMethod();
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void GetAccountCategoryMethod()
    {

        Call<GetAccountCategory> call = RetrofitClients.getInstance().getApi()
                .Api_get_account_category();
        call.enqueue(new Callback<GetAccountCategory>() {
            @Override
            public void onResponse(Call<GetAccountCategory> call, Response<GetAccountCategory> response) {

                binding.progressBar.setVisibility(View.GONE);

                GetAccountCategory finallyPr = response.body();

                if (finallyPr.getStatus().equalsIgnoreCase("1"))
                {
                    modelListCategory = (ArrayList<GetAccountCategory.Result>) finallyPr.getResult();

                    CategoryAdapter customAdapter=new CategoryAdapter(AddActivity.this,modelListCategory);
                    binding.spinnerCategory.setAdapter(customAdapter);

                }else {

                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddActivity.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetAccountCategory> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AddAccountMethod(){
        UserId=sessionManager.getUserID();
        Call<AddAcountModel> call = RetrofitClients.getInstance().getApi()
                .Api_add_account(CategoryAccountId,UserId,binding.edtAccontHolderName.getText().toString(),binding.edtHolderBalnce.getText().toString());
        call.enqueue(new Callback<AddAcountModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<AddAcountModel> call, Response<AddAcountModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    AddAcountModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        sessionManager.saveAccountId(finallyPr.getResult().getId());
                        startActivity(new Intent(AddActivity.this, SetBudgetActivity.class));

                        Toast.makeText(AddActivity.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(AddActivity.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(AddActivity.this, "Don't match email/mobile password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<AddAcountModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}