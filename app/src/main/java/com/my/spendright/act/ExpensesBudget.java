package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityExpensesBudgetBinding;

public class ExpensesBudget extends AppCompatActivity {

    ActivityExpensesBudgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_expenses_budget);

       binding.imgBack.setOnClickListener(v -> {
           onBackPressed();
       });

       binding.imgEdit.setOnClickListener(v -> {

           startActivity(new Intent(ExpensesBudget.this,EditExpensen.class));
       });
    }
}