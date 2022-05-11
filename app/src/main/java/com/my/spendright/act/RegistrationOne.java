package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityRegistrationOneBinding;

public class RegistrationOne extends AppCompatActivity {

    ActivityRegistrationOneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_registration_one);


        binding.RRNext.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationOne.this,RegistrationTwo.class));
            finish();
        });
    }
}