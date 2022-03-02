package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityLoginOneBinding;

public class LoginOne extends AppCompatActivity {

    ActivityLoginOneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login_one);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRNext.setOnClickListener(v -> {

          startActivity(new Intent(LoginOne.this,LoginSuccess.class));

        });

    }
}