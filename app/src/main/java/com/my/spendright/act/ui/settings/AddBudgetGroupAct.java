package com.my.spendright.act.ui.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityAddBudgetCatBinding;
import com.my.spendright.databinding.ActivityAddBudgetGrpBinding;

public class AddBudgetGroupAct extends AppCompatActivity {
    ActivityAddBudgetGrpBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_budget_grp);
        initViews();
    }

    private void initViews() {
        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnSave.setOnClickListener(view -> finish());

    }
}