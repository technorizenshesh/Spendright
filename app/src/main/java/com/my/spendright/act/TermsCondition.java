package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityTermsConditionBinding;

public class TermsCondition extends AppCompatActivity {

    ActivityTermsConditionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_terms_condition);

        binding.imgBack.setOnClickListener(v -> {

            onBackPressed();

        });

    }
}