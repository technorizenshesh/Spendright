package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityLoginSuccessBinding;

public class LoginSuccess extends AppCompatActivity {

    ActivityLoginSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login_success);

        binding.RRSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginSuccess.this,HomeActivity.class));
        });
    }
}