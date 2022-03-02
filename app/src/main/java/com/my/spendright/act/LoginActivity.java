package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_login);

       binding.imgBack.setOnClickListener(v ->
       {
           onBackPressed();

       });

       binding.RRlogin.setOnClickListener(v -> {
           startActivity(new Intent(LoginActivity.this,LoginOne.class));
       });

       binding.txtRegistration.setOnClickListener(v -> {
           startActivity(new Intent(LoginActivity.this,Registration.class));
       });
    }
}