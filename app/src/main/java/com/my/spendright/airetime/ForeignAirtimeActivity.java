package com.my.spendright.airetime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.my.spendright.DataBinderMapperImpl;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.R;
import com.my.spendright.airetime.adapter.ServicesAdapterForienAmt;
import com.my.spendright.airetime.adapter.ServicesOperatorAdapter;
import com.my.spendright.airetime.adapter.ServicesProductTypeAdapter;
import com.my.spendright.airetime.model.GetAmountAirtimeModel;
import com.my.spendright.airetime.model.GetOperatorModel;
import com.my.spendright.airetime.model.GetProductTypeModel;
import com.my.spendright.databinding.ActivityForeignAirtimeBinding;
import com.my.spendright.utils.ApiNew;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForeignAirtimeActivity extends AppCompatActivity {

    ActivityForeignAirtimeBinding binding;
    private ArrayList<GetAmountAirtimeModel.Content.Variation> modelListAmt = new ArrayList<>();
    private SessionManager sessionManager;
     String name="";
     String variation_code="";
     String Amt="";
     String Flexible="";

    String myWalletBalace="";
    String Code="";
    String ProductId="";
    String OPerator_Id="";
    String Phone="";
    String serviceID="foreign-airtime";

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_foreign_airtime);

        sessionManager = new SessionManager(ForeignAirtimeActivity.this);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        Intent intent=getIntent();

        if(intent!=null)
        {
            myWalletBalace = intent.getStringExtra("Balance");
            Code = intent.getStringExtra("Code");
            ProductId = intent.getStringExtra("ProductId");
            OPerator_Id = intent.getStringExtra("OPerator_Id");
            Phone = intent.getStringExtra("Phone");
            binding.txtCurrentBalnce.setText(myWalletBalace);
        }


        binding.spinnerServiceAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                //ServicesId = modelListCategory.get(pos).getServiceID();
                name = modelListAmt.get(pos).getName();
                variation_code = modelListAmt.get(pos).getVariationCode();
                if(modelListAmt.get(pos).getFixedPrice().equalsIgnoreCase("Yes"))
                {
                    Amt = modelListAmt.get(pos).getVariationAmount();
                    binding.edtAmt.setText(Amt);
                    binding.txtFixedPrice.setText("fixedPrice : "+modelListAmt.get(pos).getFixedPrice());
                    binding.edtAmt.setEnabled(false);

                }else
                {
                    binding.txtFixedPrice.setText("fixedPrice : "+modelListAmt.get(pos).getFixedPrice());
                    binding.edtAmt.setText("");
                    binding.edtAmt.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.RRPay.setOnClickListener(v -> {

            String Amount=binding.edtAmt.getText().toString();
            String Email=binding.edtEmail.getText().toString();

            if(Amount.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Amount.", Toast.LENGTH_SHORT).show();

            }else
            {
                startActivity(new Intent(ForeignAirtimeActivity.this, ConfirmPaymentAireTimeForiegnAct.class)
                    .putExtra("ServicesId",serviceID)
                    .putExtra("billersCode",Phone)
                    .putExtra("variation_code",variation_code)
                    .putExtra("amount",Amount)
                    .putExtra("phone",Phone)
                    .putExtra("operator_id",OPerator_Id)
                    .putExtra("country_code",Code)
                    .putExtra("product_type_id",ProductId)
                    .putExtra("email",Email)
                    .putExtra("MyCuurentBlance",myWalletBalace));

            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi("harshit.ixora89@gmail.com","harshit89@");
        }else {
            Toast.makeText(ForeignAirtimeActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void ServiceApi(final String username, final String password) {
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetAmountAirtimeModel> call = loginService.Api_get_international_airtime_service_variations(serviceID,OPerator_Id,ProductId);
        call.enqueue(new Callback<GetAmountAirtimeModel>() {
            @Override
            public void onResponse(@NonNull Call<GetAmountAirtimeModel> call, @NonNull Response<GetAmountAirtimeModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    try {
                        GetAmountAirtimeModel finallyPr = response.body();
                        modelListAmt= (ArrayList<GetAmountAirtimeModel.Content.Variation>) finallyPr.getContent().getVariations();

                        if(finallyPr.getResponseDescription().equalsIgnoreCase("000"))
                        {
                            ServicesAdapterForienAmt customAdapter1=new ServicesAdapterForienAmt(ForeignAirtimeActivity.this,modelListAmt);
                            binding.spinnerServiceAmount.setAdapter(customAdapter1);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ForeignAirtimeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetAmountAirtimeModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


}