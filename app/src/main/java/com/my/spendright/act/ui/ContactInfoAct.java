package com.my.spendright.act.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityContactBinding;

public class ContactInfoAct extends AppCompatActivity {
    ActivityContactBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact);
        initViews();
    }

    private void initViews() {
        binding.imgBack.setOnClickListener(v -> finish());

    }
}
