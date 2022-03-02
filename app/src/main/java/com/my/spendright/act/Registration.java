package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityRegistrationBinding;

public class Registration extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_registration);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtLogin.setOnClickListener(v ->
        {
           startActivity(new Intent(Registration.this,LoginActivity.class));
        });

         binding.RRRegis.setOnClickListener(v ->
        {
           startActivity(new Intent(Registration.this,RegistrationOne.class));
        });

    }
}