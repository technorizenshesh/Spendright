package com.my.spendright.airetime;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.airetime.adapter.ServicesAireAdapter;
import com.my.spendright.databinding.ActivityPaymentBillAiretimeBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentBillAireTime extends AppCompatActivity {

    ActivityPaymentBillAiretimeBinding binding;
    private ArrayList<GetServiceElectricialModel.Content> modelListCategory = new ArrayList<>();

    String ServicesId="";
    String ServicesName="";
    String phoneNumber="";
    String Amount="";

    String myWalletBalace="";
    private SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_bill_airetime);

      //  binding.RRPay.setOnClickListener(view ->   startActivity(new Intent(PaymentBillAireTime.this,PaymentBillInterNational.class)));

        binding.edtAmt.addTextChangedListener(new NumberTextWatcher(binding.edtAmt,"#,###"));

        sessionManager = new SessionManager(PaymentBillAireTime.this);

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

                    phoneNumber =  binding.edtPhone.getText().toString();
                    Amount = binding.edtAmt.getText().toString();

                    if(phoneNumber.equalsIgnoreCase(""))
                    {
                        Toast.makeText(PaymentBillAireTime.this, "Please Enter phone Number", Toast.LENGTH_SHORT).show();

                    }else if(Amount.equalsIgnoreCase(""))
                    {
                        Toast.makeText(PaymentBillAireTime.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        phoneNumber = binding.txtCountry.getSelectedCountryCodeWithPlus()+ binding.edtPhone.getText().toString();

                        startActivity(new Intent(PaymentBillAireTime.this, ConfirmPaymentAireTimeAct.class)
                                .putExtra("ServicesId",ServicesId)
                                .putExtra("ServicesName",ServicesName)
                                .putExtra("amount",Amount)
                                .putExtra("phone",phoneNumber)
                                .putExtra("MyCuurentBlance",myWalletBalace)
                        );
                    }
                }
            }
        });


        binding.spinnerServiceAirTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                if(modelListCategory.get(pos).getServiceID().equalsIgnoreCase("foreign-airtime"))
                {


                    Intent intent1=new Intent(PaymentBillAireTime.this,PaymentBillInterNational.class);
                    intent1.putExtra("MyCuurentBlance",myWalletBalace);
                    intent1.putExtra("SerVicesName",modelListCategory.get(pos).getName());
                    startActivity(intent1);
                    finish();

                }else
                {
                    ServicesId = modelListCategory.get(pos).getServiceID();
                    ServicesName = modelListCategory.get(pos).getName();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


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


    @Override
    protected void onResume() {
        super.onResume();

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);


            ServiceApi();
        }else {
            Toast.makeText(PaymentBillAireTime.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void ServiceApi() {
        Call<GetServiceElectricialModel> call = RetrofitClients.getInstance().getApi().Api_service_airtime();
        call.enqueue(new Callback<GetServiceElectricialModel>() {
            @Override
            public void onResponse(@NonNull Call<GetServiceElectricialModel> call, @NonNull Response<GetServiceElectricialModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    GetServiceElectricialModel finallyPr = response.body();
                    modelListCategory= (ArrayList<GetServiceElectricialModel.Content>) finallyPr.getContent();

                    ServicesAireAdapter customAdapter=new ServicesAireAdapter(PaymentBillAireTime.this,modelListCategory);
                    binding.spinnerServiceAirTime.setAdapter(customAdapter);


                } else {
                    Toast.makeText(PaymentBillAireTime.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetServiceElectricialModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }

}