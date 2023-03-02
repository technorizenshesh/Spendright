package com.my.spendright.TvCabelBill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.spendright.Model.TvSuscriptionServiceModel;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.adapter.TvSusCriptionChnageAdapter;
import com.my.spendright.databinding.ActivityPaymentInformationTvChangeBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentInformationTvChangeAct extends AppCompatActivity {

    ActivityPaymentInformationTvChangeBinding binding;

    String billersCode="";
    private SessionManager sessionManager;

    TvSusCriptionChnageAdapter mAdapter;
    ArrayList<TvSuscriptionServiceModel.Content.Varation> modelList=new ArrayList<>();

    String myWalletBalace="";
    String Meter_Number="";
    String CustomerName="";
    String CustomerType="";
    String RenewalAmt="";
    String ServicesSubscriptionId="";
    String ServicesSubscriptionName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_information_tv_change);

        sessionManager = new SessionManager(PaymentInformationTvChangeAct.this);

        Intent intent=getIntent();

        if(intent !=null)
        {
            Meter_Number =intent.getStringExtra("Meter_Number");
            CustomerName =intent.getStringExtra("CustomerName");
            CustomerType =intent.getStringExtra("CustomerType");
            RenewalAmt =intent.getStringExtra("RenewalAmt");
            ServicesSubscriptionId =intent.getStringExtra("ServicesSubscriptionId");
            ServicesSubscriptionName =intent.getStringExtra("ServicesSubscriptionName");
            myWalletBalace =intent.getStringExtra("myWalletBalace");


            binding.edtBillNumber.setText(Meter_Number+"");
            binding.edtServicesId.setText(ServicesSubscriptionName+"");

        }



        binding.progressBar.setVisibility(View.VISIBLE);
        ServiceSubscriptionPlanApi();

    }

    private void setAdapter(ArrayList<TvSuscriptionServiceModel.Content.Varation> modelList) {


        mAdapter = new TvSusCriptionChnageAdapter(PaymentInformationTvChangeAct.this,modelList);
        binding.RecleviewPlanTVSuscription.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentInformationTvChangeAct.this);
        binding.RecleviewPlanTVSuscription.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.RecleviewPlanTVSuscription.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new TvSusCriptionChnageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, TvSuscriptionServiceModel.Content.Varation model) {

                startActivity(new Intent(PaymentInformationTvChangeAct.this, PaymentInformationTvChangeInformationAct.class)
                                .putExtra("Meter_Number",Meter_Number+"")
                                .putExtra("CustomerName",CustomerName)
                                .putExtra("CustomerType",CustomerType)
                                .putExtra("RenewalAmt",RenewalAmt)
                                .putExtra("ServicesSubscriptionId",ServicesSubscriptionId+"")
                                .putExtra("ServicesSubscriptionName",ServicesSubscriptionName+"")
                                .putExtra("myWalletBalace",myWalletBalace+"")
                                .putExtra("variation_code",model.getVariationCode()+"")
                                .putExtra("variation_amount",model.getVariationAmount()+"")
                                .putExtra("variation_name",model.getName()+"")
                );
            }
        });
    }

    private void ServiceSubscriptionPlanApi() {
        Call<TvSuscriptionServiceModel> call = RetrofitClients.getInstance().getApi().Api_service_tv_subscription_plan(ServicesSubscriptionId);
        call.enqueue(new Callback<TvSuscriptionServiceModel>() {
            @Override
            public void onResponse(@NonNull Call<TvSuscriptionServiceModel> call, @NonNull Response<TvSuscriptionServiceModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    TvSuscriptionServiceModel finallyPr = response.body();

                    if(finallyPr.getResponseDescription().equalsIgnoreCase("000"))
                    {
                        binding.edtServicesId.setText(finallyPr.getContent().getServiceName()+"");

                        modelList= (ArrayList<TvSuscriptionServiceModel.Content.Varation>) finallyPr.getContent().getVarations();

                        setAdapter(modelList);
                    }
                } else {
                    Toast.makeText(PaymentInformationTvChangeAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<TvSuscriptionServiceModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


}