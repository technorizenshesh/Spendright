package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityFilterBinding;

public class FIlterActivity extends AppCompatActivity {

    ActivityFilterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_filter);

       binding.imgBack.setOnClickListener(v -> {
           finish();
       });

       binding.RRFilter.setOnClickListener(v -> {
           finish();
       });
    }
}