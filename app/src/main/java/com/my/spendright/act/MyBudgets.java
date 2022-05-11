package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityMyBudgetsBinding;

public class MyBudgets extends AppCompatActivity {

    ActivityMyBudgetsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_budgets);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}