package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityPaymentCompleteBinding;

public class PaymentComplete extends AppCompatActivity {

    ActivityPaymentCompleteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_complete);

        binding.RRComplete.setOnClickListener(v -> {
            startActivity(new Intent(PaymentComplete.this,HomeActivity.class));
            finish();
        });
    }
}