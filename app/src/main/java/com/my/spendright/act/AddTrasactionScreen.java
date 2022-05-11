package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityAddTrasactionScreenBinding;

public class AddTrasactionScreen extends AppCompatActivity {

    ActivityAddTrasactionScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_trasaction_screen);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRAdd.setOnClickListener(v -> {
            startActivity(new Intent(AddTrasactionScreen.this,SchdulePayment.class));
        });
    }

}