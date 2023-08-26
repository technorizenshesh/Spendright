package com.my.spendright.act.ui.home.virtualcards;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;

import com.my.spendright.databinding.ActivityWithdrawVirtualBinding;

public class WithdrawVirtualAct extends AppCompatActivity {
    ActivityWithdrawVirtualBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_withdraw_virtual);
        initViews();
    }

    private void initViews() {
        binding.imgBack.setOnClickListener(view -> finish());

        binding.RRFundCard.setOnClickListener(view -> finish());

    }
}
