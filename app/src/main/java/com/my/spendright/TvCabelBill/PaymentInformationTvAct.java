package com.my.spendright.TvCabelBill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.widget.Toast;

import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.act.ConfirmPaymentAct;
import com.my.spendright.act.PaymentInformation;
import com.my.spendright.databinding.ActivityPaymentInformationTvBinding;
import com.my.spendright.utils.SessionManager;

public class PaymentInformationTvAct extends AppCompatActivity {

    ActivityPaymentInformationTvBinding binding;

    String myWalletBalace="";
    String Meter_Number="";
    String CustomerName="";
    String CustomerType="";
    String RenewalAmt="";
    String ServicesSubscriptionId="";
    String ServicesSubscriptionName="";


    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_information_tv);

        sessionManager = new SessionManager(PaymentInformationTvAct.this);

        Intent intent=getIntent();

        if(intent !=null)
        {
             Meter_Number =intent.getStringExtra("Meter_Number");
             CustomerName =intent.getStringExtra("CustomerName");
             CustomerType =intent.getStringExtra("CustomerType");
            RenewalAmt =intent.getStringExtra("RenewalAmt");
            ServicesSubscriptionId =intent.getStringExtra("ServicesSubscriptionId");
            ServicesSubscriptionName =intent.getStringExtra("ServicesSubscriptionName");


            binding.edtBillNumber.setText(Meter_Number+"");
            binding.txtCustomerType.setText(CustomerType+"");
            binding.edtCName.setText(CustomerName+"");
            binding.txtAmout.setText(RenewalAmt+"");
            binding.edtAmount.setText(RenewalAmt+"");

            binding.edtServicesId.setText(ServicesSubscriptionId+"");
            binding.txtCountry.setCountryForPhoneCode(234);


        }

        binding.RRPay.setOnClickListener(v -> {

            String phone =  binding.edtCMobile.getText().toString();

            if(phone.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Mobile Number..", Toast.LENGTH_SHORT).show();

            }else
            {
                 phone= binding.txtCountry.getSelectedCountryCodeWithPlus()+ binding.edtCMobile.getText().toString();

                startActivity(new Intent(PaymentInformationTvAct.this, ConfirmPaymentTvAct.class)
                        .putExtra("ServicesId",ServicesSubscriptionId)
                        .putExtra("ServicesName",ServicesSubscriptionName)
                        .putExtra("billersCode",Meter_Number)
                        .putExtra("amount",RenewalAmt)
                        .putExtra("phone",phone)
                        .putExtra("subscription_type","renew")
                        .putExtra("MyCuurentBlance",myWalletBalace)
                );
            }

        });
    }
}