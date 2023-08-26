package com.my.spendright.Broadband;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.adapter.TvSusCriptionChnageAdapter;
import com.my.spendright.adapter.ServicesAdapter;
import com.my.spendright.databinding.ActivityPaymentBillBroadbandBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentBillBroadBandAct extends AppCompatActivity {

    ActivityPaymentBillBroadbandBinding binding;
    private ArrayList<GetServiceElectricialModel.Content> modelListCategory = new ArrayList<>();

    String ServicesId="";
    String ServicesName="";
    String phoneNumber="";

    String myWalletBalace="";
    String txtAmts="";
    String variation_code="";
    private SessionManager sessionManager;


    TvSusCriptionChnageAdapter mAdapter;
    ArrayList<TvSuscriptionServiceModel.Content.Varation> modelList=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_bill_broadband);



        sessionManager = new SessionManager(PaymentBillBroadBandAct.this);

        Intent intent=getIntent();

        if(intent!=null)
        {
             myWalletBalace = intent.getStringExtra("Balance");
            binding.txtCurrentBalnce.setText(myWalletBalace);
            binding.txtCountry.setCountryForPhoneCode(234);

        }



        binding.RRPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                     phoneNumber = binding.edtPhone.getText().toString();

                    if(phoneNumber.equalsIgnoreCase(""))
                    {
                        Toast.makeText(PaymentBillBroadBandAct.this, "Please Enter phone Number", Toast.LENGTH_SHORT).show();

                    }else if(txtAmts.equalsIgnoreCase(""))
                    {
                        Toast.makeText(PaymentBillBroadBandAct.this, "Please Please select Plan.", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        phoneNumber = binding.txtCountry.getSelectedCountryCodeWithPlus()+ binding.edtPhone.getText().toString();
                        startActivity(new Intent(PaymentBillBroadBandAct.this, ConfirmPaymentBroadBandAct.class)
                                .putExtra("ServicesId",ServicesId)
                                .putExtra("ServicesName",ServicesName)
                                .putExtra("amount",txtAmts)
                                .putExtra("billersCode",phoneNumber)
                                .putExtra("variation_code",variation_code)
                                .putExtra("phone",phoneNumber)
                                .putExtra("MyCuurentBlance",myWalletBalace)
                        );
                    }
                }
            }
        });


        binding.spinnerServicedata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
              ServicesId = modelListCategory.get(pos).getServiceID();
              ServicesName = modelListCategory.get(pos).getName();
              binding.progressBar.setVisibility(View.VISIBLE);
                ServiceSubscriptionPlanApi();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi();

        }else {
            Toast.makeText(PaymentBillBroadBandAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        binding.edtPhone.addTextChangedListener(mMoneyWatcher);

    }


    private TextWatcher mMoneyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!s.toString().isEmpty()) {
                binding.edtPhone.removeTextChangedListener(mMoneyWatcher);
                if (s.toString().charAt(0)=='0') {
                    binding.edtPhone.setText(s.toString().substring(1,s.length()));
                }

                binding.edtPhone.addTextChangedListener(mMoneyWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setAdapter(ArrayList<TvSuscriptionServiceModel.Content.Varation> modelList) {
        mAdapter = new TvSusCriptionChnageAdapter(PaymentBillBroadBandAct.this,modelList);
        binding.recycleViewPlan.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentBillBroadBandAct.this);
        binding.recycleViewPlan.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleViewPlan.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new TvSusCriptionChnageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, TvSuscriptionServiceModel.Content.Varation model) {
                txtAmts=model.getVariationAmount();
                variation_code=model.getVariationCode();
                binding.txtAmt.setText(model.getVariationAmount()+"");
            }
        });
    }




    private void ServiceApi() {
        Call<GetServiceElectricialModel> call = RetrofitClients.getInstance().getApi().Api_service_data();
        call.enqueue(new Callback<GetServiceElectricialModel>() {
            @Override
            public void onResponse(@NonNull Call<GetServiceElectricialModel> call, @NonNull Response<GetServiceElectricialModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    GetServiceElectricialModel finallyPr = response.body();
                    modelListCategory= (ArrayList<GetServiceElectricialModel.Content>) finallyPr.getContent();

                    ServicesAdapter customAdapter=new ServicesAdapter(PaymentBillBroadBandAct.this,modelListCategory);
                    binding.spinnerServicedata.setAdapter(customAdapter);

                } else {
                    Toast.makeText(PaymentBillBroadBandAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetServiceElectricialModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void ServiceSubscriptionPlanApi() {
        Call<TvSuscriptionServiceModel> call = RetrofitClients.getInstance().getApi().Api_service_data_plan(ServicesId);
        call.enqueue(new Callback<TvSuscriptionServiceModel>() {
            @Override
            public void onResponse(@NonNull Call<TvSuscriptionServiceModel> call, @NonNull Response<TvSuscriptionServiceModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    TvSuscriptionServiceModel finallyPr = response.body();

                    if(finallyPr.getResponseDescription().equalsIgnoreCase("000"))
                    {

                        modelList= (ArrayList<TvSuscriptionServiceModel.Content.Varation>) finallyPr.getContent().getVarations();

                        setAdapter(modelList);
                    }
                } else {
                    Toast.makeText(PaymentBillBroadBandAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<TvSuscriptionServiceModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}