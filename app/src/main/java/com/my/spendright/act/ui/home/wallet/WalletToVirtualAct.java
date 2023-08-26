package com.my.spendright.act.ui.home.wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.databinding.ActivityWalletToVirtualBinding;

public class WalletToVirtualAct extends AppCompatActivity {
    ActivityWalletToVirtualBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_to_virtual);
        initViews();
    }

    private void initViews() {
        binding.imgBack.setOnClickListener(view -> finish());

        binding.RRFundCard.setOnClickListener(view -> startActivity(new Intent(this, PaymentComplete.class)));

    }
}