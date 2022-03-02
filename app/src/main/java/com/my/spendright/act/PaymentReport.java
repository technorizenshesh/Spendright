package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityPaymentReportBinding;

public class PaymentReport extends AppCompatActivity {

    ActivityPaymentReportBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_report);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}