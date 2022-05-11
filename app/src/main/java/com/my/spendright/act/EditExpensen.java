package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityEditExpensenBinding;

public class EditExpensen extends AppCompatActivity {

    ActivityEditExpensenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil.setContentView(this,R.layout.activity_edit_expensen);

      binding.imgBack.setOnClickListener(v -> {
          onBackPressed();
      });

    }
}