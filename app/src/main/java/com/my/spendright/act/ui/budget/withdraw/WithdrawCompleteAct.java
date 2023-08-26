package com.my.spendright.act.ui.budget.withdraw;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.databinding.ActivityPaymentCompleteBinding;

public class WithdrawCompleteAct extends AppCompatActivity {

    ActivityPaymentCompleteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_payment_complete);
        binding.tvStatus.setText(getString(R.string.withdraw_complete));

        binding.RRComplete.setOnClickListener(v -> {
            startActivity(new Intent(WithdrawCompleteAct.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
    }
}