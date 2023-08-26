package com.my.spendright.airetime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.airetime.adapter.ServicesAdapterForienAmt;
import com.my.spendright.airetime.model.GetAmountAirtimeModel;
import com.my.spendright.databinding.ActivityForeignAirtimeBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
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
     String variation_rate="";
     String Charge_amount="";
     String Amt="";
     String Flexible="";

    String myWalletBalace="";
    String Code="";
    String ProductId="";
    String OPerator_Id="";
    String Phone="";
    String CurrenyAmt="";
    String serviceID="foreign-airtime";
    String SerVicesName="";
    String CurrenCy="";
    double FinalAmt=0.0;
    boolean isPayment =false;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_foreign_airtime);
        binding.edtAmt.addTextChangedListener(new NumberTextWatcher(binding.edtAmt,"#,###"));

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
            CurrenyAmt = intent.getStringExtra("CurrenyAmt");
            SerVicesName = intent.getStringExtra("SerVicesName");
            CurrenCy = intent.getStringExtra("CurrenCy");
            binding.txtCurrentBalnce.setText(myWalletBalace);
            binding.txtCurrency.setText(CurrenCy);
        }

        binding.edtAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(isPayment)
                {
                    Amt= binding.edtAmt.getText().toString();
                    if(!Amt.equalsIgnoreCase(""))
                    {
                        Double courrecyAmt = Double.valueOf(CurrenyAmt);
                        Double Ammt = Double.valueOf(Amt);

                        FinalAmt=courrecyAmt*Ammt;

                        binding.edtAmtNaira.setText(String.format("%.2f",FinalAmt)+"");
                    }
                }else {

                    String Amount1 = binding.edtAmt.getText().toString();

                    if(!Amount1.equalsIgnoreCase(""))
                    {
                        if(variation_rate!=null)
                        {
                            if(!variation_rate.equalsIgnoreCase(""))
                            {
                                Double NewAmt= Double.valueOf(Amount1);
                                Double NewAmt1= Double.valueOf(variation_rate);
                                Double UserPay =NewAmt*NewAmt1;

                             //   binding.edtAmtNaira.setText(String.format("%.2f",UserPay)+"");
                                binding.edtAmtNaira.setText(Preference.doubleToStringNoDecimal(UserPay));

                            }
                        }

                    }

                }

                // TODO Auto-generated method stub
            }
        });

        binding.spinnerServiceAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                //ServicesId = modelListCategory.get(pos).getServiceID();

                name = modelListAmt.get(pos).getName();
                variation_code = modelListAmt.get(pos).getVariationCode();
                variation_rate = modelListAmt.get(pos).getVariationRate();
                Charge_amount = modelListAmt.get(pos).getChargedAmount();

                if(modelListAmt.get(pos).getFixedPrice().equalsIgnoreCase("Yes"))
                {

                    Amt = modelListAmt.get(pos).getVariationAmount();
                    binding.edtAmt.setText(Preference.doubleToStringNoDecimal(Double.parseDouble(Amt)));
                    binding.txtFixedPrice.setText("fixedPrice : "+modelListAmt.get(pos).getFixedPrice());
                    binding.edtAmt.setEnabled(false);

                    FinalAmt= Double.valueOf(Charge_amount);



                 //   binding.edtAmtNaira.setText(String.format("%.2f",FinalAmt)+"");

                    binding.edtAmtNaira.setText(Preference.doubleToStringNoDecimal(FinalAmt));


                   // binding.txtAmtMinMax.setText("Amount between "+modelListAmt.get(pos).getVariationAmountMin()+" - "+modelListAmt.get(pos).getVariationAmountMax()+" "+CurrenCy);


                    isPayment =true;

                   // binding.edtAmtNaira.setText(String.format("%.2f",Charge_amount)+"");
                    if(!Amt.equalsIgnoreCase(""))
                    {
                        Double courrecyAmt = Double.valueOf(CurrenyAmt);
                        Double Ammt = Double.valueOf(Amt);
                        FinalAmt=courrecyAmt*Ammt;
                       // binding.edtAmtNaira.setText(FinalAmt+"");
                    }
                }else
                {
                    binding.txtAmtMinMax.setText("Amount between "+modelListAmt.get(pos).getVariationAmountMin()+" - "+modelListAmt.get(pos).getVariationAmountMax()+" "+CurrenCy);

                    isPayment =false;

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

         //   String Amount=binding.edtAmt.getText().toString();
            String Amount=binding.edtAmt.getText().toString();
            String Amount1=binding.edtAmtNaira.getText().toString();
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
                    .putExtra("Finalamount",Amount1)
                    .putExtra("phone",Phone)
                    .putExtra("operator_id",OPerator_Id)
                    .putExtra("country_code",Code)
                    .putExtra("product_type_id",ProductId)
                    .putExtra("email",Email)
                    .putExtra("SerVicesName",SerVicesName)
                    .putExtra("MyCuurentBlance",myWalletBalace));

            }
         //   startActivity(new Intent(ForeignAirtimeActivity.this, ConfirmPaymentAireTimeForiegnAct.class));

        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi();
        }else {
            Toast.makeText(ForeignAirtimeActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void ServiceApi() {
        Call<GetAmountAirtimeModel> call = RetrofitClients.getInstance().getApi().Api_get_international_airtime_service_variations(serviceID,OPerator_Id,ProductId);
        call.enqueue(new Callback<GetAmountAirtimeModel>() {
            @Override
            public void onResponse(@NonNull Call<GetAmountAirtimeModel> call, @NonNull Response<GetAmountAirtimeModel> response) {

                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        // user object available

                        GetAmountAirtimeModel finallyPr = response.body();
                        modelListAmt = (ArrayList<GetAmountAirtimeModel.Content.Variation>) finallyPr.getContent().getVariations();

                        if (finallyPr.getResponseDescription().equalsIgnoreCase("000")) {
                            ServicesAdapterForienAmt customAdapter1 = new ServicesAdapterForienAmt(ForeignAirtimeActivity.this, modelListAmt);
                            binding.spinnerServiceAmount.setAdapter(customAdapter1);
                        }
                    } else {
                        Toast.makeText(ForeignAirtimeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetAmountAirtimeModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


}