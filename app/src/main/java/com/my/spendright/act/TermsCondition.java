package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.my.spendright.Model.ChangePasswordModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityTermsConditionBinding;
import com.my.spendright.utils.RetrofitClients;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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