package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivitySelectAccountBinding;

public class SelectAccount extends AppCompatActivity {

    ActivitySelectAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_select_account);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRlogin.setOnClickListener(v -> {
           startActivity(new Intent(SelectAccount.this,LoginActivity.class));
        });

        binding.RRSignUp.setOnClickListener(v -> {
           startActivity(new Intent(SelectAccount.this,Registration.class));
        });

    }
}