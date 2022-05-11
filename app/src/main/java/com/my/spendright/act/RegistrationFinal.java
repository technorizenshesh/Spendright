package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityRegistrationFinalBinding;

public class RegistrationFinal extends AppCompatActivity {

    ActivityRegistrationFinalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_registration_final);

       binding.RRDone.setOnClickListener(v -> {

           startActivity(new Intent(RegistrationFinal.this,HomeActivity.class));
           finish();
       });

    }
}