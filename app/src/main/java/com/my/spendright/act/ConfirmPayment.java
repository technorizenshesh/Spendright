package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityConfirmPaymentBinding;

public class ConfirmPayment extends AppCompatActivity {

    ActivityConfirmPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_payment);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}