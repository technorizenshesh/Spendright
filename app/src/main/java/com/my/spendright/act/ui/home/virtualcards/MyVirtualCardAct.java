package com.my.spendright.act.ui.home.virtualcards;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityMyVirtualCardBinding;

public class MyVirtualCardAct extends AppCompatActivity implements CreateVirtualListener {
    ActivityMyVirtualCardBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_virtual_card);
        initViews();
    }

    private void initViews() {

       // binding.rvFund.setAdapter(new TransactionAdapter(MyVirtualCardAct.this));

        binding.ivAdd.setOnClickListener(view -> {
            new CreateVirtualBottomSheet().callBack(this::onVirtual).show(getSupportFragmentManager(),"");

        });

        binding.imgBack.setOnClickListener(view -> finish());

        binding.llTopUp.setOnClickListener(view -> startActivity(new Intent(this,TopupVirtualAct.class)));

        binding.llWithdraw.setOnClickListener(view ->startActivity(new Intent(this,WithdrawVirtualAct.class)));


        binding.ivVirtual.setOnClickListener(view -> {
            new CardDetailBottomSheet().callBack(this::onVirtual).show(getSupportFragmentManager(),"");
        });
    }

    @Override
    public void onVirtual(String data,String tag) {
       /* if(data.equals("1")) startActivity(new Intent(this,CardCreateSuccessAct.class));
        else {

        }*/
    }
}
