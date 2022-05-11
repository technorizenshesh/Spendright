package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityRegistrationTwoBinding;

public class RegistrationTwo extends AppCompatActivity {

    ActivityRegistrationTwoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_registration_two);

       binding.RRNext.setOnClickListener(v -> {

          startActivity(new Intent(RegistrationTwo.this,RegistrationFinal.class));
           finish();
       });

    }
}