package com.my.spendright.TvCabelBill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityPaymentInformationTvBinding;
import com.my.spendright.databinding.ActivityTvInformationBinding;
import com.my.spendright.utils.SessionManager;

public class PaymentInformationTvChangeInformationAct extends AppCompatActivity {

    ActivityTvInformationBinding binding;
    private SessionManager sessionManager;

    String myWalletBalace="";
    String Meter_Number="";
    String CustomerName="";
    String CustomerType="";
    String RenewalAmt="";
    String ServicesSubscriptionId="";
    String ServicesSubscriptionName="";

    String variation_code="";
    String variation_amount="";
    String variation_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_tv_information);

        sessionManager = new SessionManager(PaymentInformationTvChangeInformationAct.this);

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
            variation_code =intent.getStringExtra("variation_code");
            variation_amount =intent.getStringExtra("variation_amount");
            variation_name =intent.getStringExtra("variation_name");


            binding.edtBillNumber.setText(Meter_Number+"");
            binding.edtServicesId.setText(ServicesSubscriptionName+"");
            binding.edtCName.setText(CustomerName+"");
            binding.txtPlanname.setText(ServicesSubscriptionName+"");
            binding.txtPlanname.setText(variation_name+"");
            binding.txtAmout.setText(variation_amount+"");
            binding.edtAmount.setText(variation_amount+"");

        }

        binding.RRPay.setOnClickListener(v -> {

            String phone=binding.edtCMobile.getText().toString();
            if(phone.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "PLease Enter Mobile Number..", Toast.LENGTH_SHORT).show();

            }else
            {
                startActivity(new Intent(PaymentInformationTvChangeInformationAct.this, ConfirmPaymentTvChangeAct.class)
                                .putExtra("Meter_Number",Meter_Number+"")
                                .putExtra("CustomerName",CustomerName)
                                .putExtra("CustomerType",CustomerType)
                                .putExtra("RenewalAmt",RenewalAmt)
                                .putExtra("ServicesSubscriptionId",ServicesSubscriptionId)
                                .putExtra("ServicesSubscriptionName",ServicesSubscriptionName)
                                .putExtra("myWalletBalace",myWalletBalace)
                                .putExtra("variation_code",variation_code)
                                .putExtra("variation_amount",variation_amount)
                                .putExtra("variation_name",variation_name)
                                .putExtra("phone",phone)
                );
            }

        });
    }
}