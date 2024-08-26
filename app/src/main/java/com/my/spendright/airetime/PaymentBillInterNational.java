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

import com.google.gson.Gson;
import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.Model.CurencyModel;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.airetime.adapter.ServicesAireAdapterCountry;
import com.my.spendright.airetime.adapter.ServicesOperatorAdapter;
import com.my.spendright.airetime.adapter.ServicesProductTypeAdapter;
import com.my.spendright.airetime.model.GetInternationalModel;
import com.my.spendright.airetime.model.GetOperatorModel;
import com.my.spendright.airetime.model.GetProductTypeModel;
import com.my.spendright.databinding.ActivityAiretimeInternationalBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
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
    String prefix="";
    String CurrenCy="";
    String ProductId="";
    String OPerator_Id="";
    String serviceID="foreign-airtime";
     String myWalletBalace="";
    String CurrenyAmt="";
    String SerVicesName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_airetime_international);

       sessionManager = new SessionManager(PaymentBillInterNational.this);

        Intent intent=getIntent();

        if(intent!=null)
        {
            myWalletBalace = intent.getStringExtra("Balance");
            SerVicesName = intent.getStringExtra("SerVicesName");
            binding.txtCurrentBalnce.setText(myWalletBalace);
        }

        binding.spinnerServiceCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                //ServicesId = modelListCategory.get(pos).getServiceID();
                Code = modelListCountry.get(pos).getCode();
                prefix = modelListCountry.get(pos).getPrefix();
                binding.txtCountryCode.setText("+"+prefix);
                CurrenCy = modelListCountry.get(pos).getCurrency();
                String Currency = modelListCountry.get(pos).getCurrency();
                ChangeCourancyCOder(CurrenCy);
                ServiceProductTypesApi();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.spinnerServiceProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                ProductId = String.valueOf(modelListProductType.get(pos).getProductTypeId());
                ServiceOperatorApi();
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
            public void onNothingSelected(AdapterView<?> arg0)
            {
                    // TODO Auto-generated method stub
              }
         });

        binding.RRPay.setOnClickListener(v -> {

            String Phone=prefix+binding.edtPhone.getText().toString();

            if(Phone.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Phone Number.", Toast.LENGTH_SHORT).show();

            }else
            {
                startActivity(new Intent(PaymentBillInterNational.this,ForeignAirtimeActivity.class)
                        .putExtra("myWalletBalace",myWalletBalace)
                        .putExtra("Code",Code)
                        .putExtra("ProductId",ProductId)
                        .putExtra("Phone",Phone)
                        .putExtra("OPerator_Id",OPerator_Id)
                        .putExtra("CurrenyAmt",CurrenyAmt)
                        .putExtra("SerVicesName",SerVicesName)
                        .putExtra("CurrenCy",CurrenCy)
                );
            }
            //startActivity(new Intent(PaymentBillInterNational.this,ForeignAirtimeActivity.class));

        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi();

        }else {
            Toast.makeText(PaymentBillInterNational.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }


    private void ServiceApi() {
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_get_international_airtime_countries(Preference.getHeader(PaymentBillInterNational.this));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    try {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);

                        if (jsonObject.getString("response_description").equalsIgnoreCase("000")) {
                            GetInternationalModel finallyPr = new Gson().fromJson(stringResponse, GetInternationalModel.class); // response.body();
                           // GetInternationalModel finallyPr = response.body();
                            modelListCountry = (ArrayList<GetInternationalModel.Content.Country>) finallyPr.getContent().getCountries();
                            ServicesAireAdapterCountry customAdapter = new ServicesAireAdapterCountry(PaymentBillInterNational.this, modelListCountry);
                            binding.spinnerServiceCountry.setAdapter(customAdapter);
                        }
                        else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                            sessionManager.logoutUser();
                            Toast.makeText(PaymentBillInterNational.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentBillInterNational.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }

                        else {
                            Toast.makeText(PaymentBillInterNational.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PaymentBillInterNational.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void ServiceProductTypesApi() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_get_international_airtime_product_types(Preference.getHeader(PaymentBillInterNational.this),Code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    try {
                        // user object available
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);

                        if (jsonObject.getString("response_description").equalsIgnoreCase("000")) {
                            GetProductTypeModel finallyPr = new Gson().fromJson(stringResponse, GetProductTypeModel.class); // response.body();
                            modelListProductType= (ArrayList<GetProductTypeModel.Content>) finallyPr.getContent();
                            ServicesProductTypeAdapter customAdapter1=new ServicesProductTypeAdapter(PaymentBillInterNational.this,modelListProductType);
                            binding.spinnerServiceProductType.setAdapter(customAdapter1);

                        }

                        else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                            sessionManager.logoutUser();
                            Toast.makeText(PaymentBillInterNational.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentBillInterNational.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }


                        else {
                            Toast.makeText(PaymentBillInterNational.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PaymentBillInterNational.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void ServiceOperatorApi() {
       binding.progressBar.setVisibility(View.VISIBLE);
       Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_get_international_airtime_operator(Preference.getHeader(PaymentBillInterNational.this),Code,ProductId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    try {
                        // user object available
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);

                        if (jsonObject.getString("response_description").equalsIgnoreCase("000")) {
                            GetOperatorModel finallyPr = new Gson().fromJson(stringResponse, GetOperatorModel.class); // response.body();
                            modelListOperator= (ArrayList<GetOperatorModel.Content>) finallyPr.getContent();
                            ServicesOperatorAdapter customAdapter1=new ServicesOperatorAdapter(PaymentBillInterNational.this,modelListOperator);
                            binding.spinnerServiceOperator.setAdapter(customAdapter1);

                        }

                        else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                            sessionManager.logoutUser();
                            Toast.makeText(PaymentBillInterNational.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentBillInterNational.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }


                        else {
                            Toast.makeText(PaymentBillInterNational.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PaymentBillInterNational.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }



    private void ChangeCourancyCOder(String CurrenCy){
        Call<CurencyModel> call = RetrofitClients.getInstance().getApi()
                .change_currency(CurrenCy);
        call.enqueue(new Callback<CurencyModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<CurencyModel> call, Response<CurencyModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    CurencyModel finallyPr = response.body();

                    if (finallyPr.getSuccess()) {

                        Double  Curency_value=finallyPr.getResult();
                         CurrenyAmt = String.valueOf(Curency_value);

                    } else {

                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CurencyModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


}