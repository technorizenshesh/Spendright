package com.my.spendright.act.ui.home.virtualcards;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.databinding.ActivityCardCreateCompleteBinding;
import com.my.spendright.databinding.ActivityCreateVirtualBinding;
import com.my.spendright.databinding.ActivityPaymentCompleteBinding;

public class CardCreateSuccessAct extends AppCompatActivity {
    ActivityCardCreateCompleteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_card_create_complete);

        binding.RRComplete.setOnClickListener(v -> {
            finish();
        });
    }
}
