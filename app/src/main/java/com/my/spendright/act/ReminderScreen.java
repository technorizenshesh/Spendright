package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityReminderScreenBinding;

public class ReminderScreen extends AppCompatActivity {

    ActivityReminderScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_reminder_screen);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRpay.setOnClickListener(v -> {
            startActivity(new Intent(ReminderScreen.this,ConfirmPayment.class));
        });

    }

}