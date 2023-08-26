package com.my.spendright.act.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityBudgetGrpBinding;

public class BudgetGroupAct extends AppCompatActivity {
    ActivityBudgetGrpBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_budget_grp);
        initViews();
    }

    private void initViews() {
        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnAdd.setOnClickListener(view -> startActivity(new Intent(this,AddBudgetGroupAct.class)));

    }
}