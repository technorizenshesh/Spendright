package com.my.spendright.airetime;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.airetime.adapter.ServicesAireAdapter;
import com.my.spendright.airetime.adapter.ServicesAireAdapterCountry;
import com.my.spendright.airetime.adapter.ServicesOperatorAdapter;
import com.my.spendright.airetime.adapter.ServicesProductTypeAdapter;
import com.my.spendright.airetime.model.GetInternationalModel;
import com.my.spendright.airetime.model.GetOperatorModel;
import com.my.spendright.airetime.model.GetProductTypeModel;
import com.my.spendright.databinding.ActivityAiretimeInternationalBinding;
import com.my.spendright.databinding.ActivityPaymentBillAiretimeBinding;
import com.my.spendright.utils.ApiNew;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentBillInterNational extends AppCompatActivity {

    ActivityAiretimeInternationalBinding binding;
    private ArrayList<GetInternationalModel.Content.Country> modelListCountry = new ArrayList<>();
    private ArrayList<GetProductTypeModel.Content> modelListProductType = new ArrayList<>();
    private ArrayList<GetOperatorModel.Content> modelListOperator = new ArrayList<>();
    private SessionManager sessionManager;

    String Code="";
    String ProductId="";
    String OPerator_Id="";
    String serviceID="foreign-airtime";
     String myWalletBalace="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_airetime_international);

        sessionManager = new SessionManager(PaymentBillInterNational.this);

        Intent intent=getIntent();

        if(intent!=null)
        {
            myWalletBalace = intent.getStringExtra("Balance");
            binding.txtCurrentBalnce.setText(myWalletBalace);
        }

        binding.spinnerServiceCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                //ServicesId = modelListCategory.get(pos).getServiceID();
                Code = modelListCountry.get(pos).getCode();
                ServiceProductTypesApi("harshit.ixora89@gmail.com","harshit89@");
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.spinnerServiceProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                ProductId = String.valueOf(modelListProductType.get(pos).getProductTypeId());
               // binding.progressBar.setVisibility(View.VISIBLE);
                ServiceOperatorApi("harshit.ixora89@gmail.com","harshit89@");
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.spinnerServiceOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                OPerator_Id = String.valueOf(modelListOperator.get(pos).getOperatorId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.RRPay.setOnClickListener(v -> {

            String Phone=binding.edtPhone.getText().toString();
            if(Phone.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Amount.", Toast.LENGTH_SHORT).show();
            }else
            {
                startActivity(new Intent(PaymentBillInterNational.this,ForeignAirtimeActivity.class)
                        .putExtra("myWalletBalace",myWalletBalace)
                        .putExtra("Code",Code)
                        .putExtra("ProductId",ProductId)
                        .putExtra("Phone",Phone)
                        .putExtra("OPerator_Id",OPerator_Id));
                           finish();
            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi("harshit.ixora89@gmail.com","harshit89@");
        }else {
            Toast.makeText(PaymentBillInterNational.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }


    private void ServiceApi(final String username, final String password) {
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetInternationalModel> call = loginService.Api_get_international_airtime_countries();
        call.enqueue(new Callback<GetInternationalModel>() {
            @Override
            public void onResponse(@NonNull Call<GetInternationalModel> call, @NonNull Response<GetInternationalModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    GetInternationalModel finallyPr = response.body();
                    modelListCountry= (ArrayList<GetInternationalModel.Content.Country>) finallyPr.getContent().getCountries();

                    ServicesAireAdapterCountry customAdapter=new ServicesAireAdapterCountry(PaymentBillInterNational.this,modelListCountry);
                    binding.spinnerServiceCountry.setAdapter(customAdapter);


                } else {
                    Toast.makeText(PaymentBillInterNational.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetInternationalModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void ServiceProductTypesApi(final String username, final String password) {
        binding.progressBar.setVisibility(View.VISIBLE);
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetProductTypeModel> call = loginService.Api_get_international_airtime_product_types(Code);
        call.enqueue(new Callback<GetProductTypeModel>() {
            @Override
            public void onResponse(@NonNull Call<GetProductTypeModel> call, @NonNull Response<GetProductTypeModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    try {
                        GetProductTypeModel finallyPr = response.body();
                        modelListProductType= (ArrayList<GetProductTypeModel.Content>) finallyPr.getContent();

                        if(finallyPr.getResponseDescription().equalsIgnoreCase("000"))
                        {
                            ServicesProductTypeAdapter customAdapter1=new ServicesProductTypeAdapter(PaymentBillInterNational.this,modelListProductType);
                            binding.spinnerServiceProductType.setAdapter(customAdapter1);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PaymentBillInterNational.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetProductTypeModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void ServiceOperatorApi(final String username, final String password) {
       binding.progressBar.setVisibility(View.VISIBLE);
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetOperatorModel> call = loginService.Api_get_international_airtime_operator(Code,ProductId);
        call.enqueue(new Callback<GetOperatorModel>() {
            @Override
            public void onResponse(@NonNull Call<GetOperatorModel> call, @NonNull Response<GetOperatorModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    try {
                        GetOperatorModel finallyPr = response.body();
                        modelListOperator= (ArrayList<GetOperatorModel.Content>) finallyPr.getContent();

                        if(finallyPr.getResponseDescription().equalsIgnoreCase("000"))
                        {
                            ServicesOperatorAdapter customAdapter1=new ServicesOperatorAdapter(PaymentBillInterNational.this,modelListOperator);
                            binding.spinnerServiceOperator.setAdapter(customAdapter1);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PaymentBillInterNational.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetOperatorModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


}