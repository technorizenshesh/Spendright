package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityEstimateExpenseBudgetBinding;

public class EstimateExpenseBudget extends AppCompatActivity {

    ActivityEstimateExpenseBudgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          binding= DataBindingUtil.setContentView(this,R.layout.activity_estimate_expense_budget);

       binding.RRAddNewGrp.setOnClickListener(v -> {
           startActivity(new Intent(EstimateExpenseBudget.this,MyBudgets.class));
       });

       binding.imgBack.setOnClickListener(v -> {
           onBackPressed();
       });

    }
}