package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityPaymentReportPageBinding;

public class PaymentReportPageAct extends AppCompatActivity {

    ActivityPaymentReportPageBinding binding;
    String RequestId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_report_page);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        if (getIntent()!=null)
        {
            String RequestId=getIntent().getStringExtra("RequestId").toString();

        }

    }



}