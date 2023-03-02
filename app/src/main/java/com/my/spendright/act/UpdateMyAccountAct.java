package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.my.spendright.Model.AddAcountModel;
import com.my.spendright.Model.GetAccountCategory;
import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.adapter.CategoryAdapter;
import com.my.spendright.databinding.ActivityUpdateMyAccountBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMyAccountAct extends AppCompatActivity {

    ActivityUpdateMyAccountBinding binding;
    private ArrayList<GetAccountCategory.Result> modelListCategory = new ArrayList<>();
    private SessionManager sessionManager;
   private String AccountId="";
   private String mainId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_my_account);

        binding.edtHolderBalnce.addTextChangedListener(new NumberTextWatcher(binding.edtHolderBalnce,"#,###"));

        sessionManager=new SessionManager(UpdateMyAccountAct.this);


        if(getIntent()!=null)
        {
            mainId=getIntent().getStringExtra("AccountNameId").toString();
            String HOlderName=getIntent().getStringExtra("AccontHolderName").toString();
            String CurrentAmt=getIntent().getStringExtra("Amt").toString();

            binding.edtAccontHolderName.setText(HOlderName);
            binding.edtHolderBalnce.setText(CurrentAmt);
        }


        binding.RRSave.setOnClickListener(v -> {
            validation();

        });

       binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                AccountId = modelListCategory.get(pos).getId();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
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

                    CategoryAdapter customAdapter=new CategoryAdapter(UpdateMyAccountAct.this,modelListCategory);
                    binding.spinnerCategory.setAdapter(customAdapter);

                    if(AccountId.equalsIgnoreCase("1"))
                    {
                        binding.spinnerCategory.setSelection(0);
                    }

                    if(AccountId.equalsIgnoreCase("2"))
                    {
                        binding.spinnerCategory.setSelection(1);
                    }
                    if(AccountId.equalsIgnoreCase("3"))
                    {
                        binding.spinnerCategory.setSelection(2);
                    }

                }else {

                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(UpdateMyAccountAct.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetAccountCategory> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateMyAccountAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                UpdateAccountMethod();
            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UpdateAccountMethod(){
        Call<AddAcountModel> call = RetrofitClients.getInstance().getApi()
                .Api_editAccount(mainId,AccountId,sessionManager.getUserID(),binding.edtAccontHolderName.getText().toString(),binding.edtHolderBalnce.getText().toString());
        call.enqueue(new Callback<AddAcountModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<AddAcountModel> call, Response<AddAcountModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    AddAcountModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        finish();
                        Toast.makeText(UpdateMyAccountAct.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(UpdateMyAccountAct.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(UpdateMyAccountAct.this, "Don't match email/mobile password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<AddAcountModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateMyAccountAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}