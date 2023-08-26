package com.my.spendright.act.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.databinding.ActivityKycSuccessBinding;

public class KycSuccessfulAct extends AppCompatActivity {
    ActivityKycSuccessBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_success);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_kyc_success);

        binding.RRDone.setOnClickListener(v -> {
            finish();
        });
    }
}
