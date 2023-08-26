package com.my.spendright.act.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.act.KYCAct;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.databinding.ActivityKycBinding;

public class KYCUpdateAct  extends AppCompatActivity {
    ActivityKycBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_kyc);
        initViews();


    }

    private void initViews() {
        binding.imgBack.setOnClickListener(v -> {
            finish();
        });


        binding.btnVerify.setOnClickListener(v -> {
            finish();
        });
    }
}
