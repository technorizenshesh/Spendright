package com.my.spendright.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityForogtPasswordBinding;

public class ForogtPassword extends AppCompatActivity {

    ActivityForogtPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forogt_password);

        binding.RRNext.setOnClickListener(v -> {

            Toast.makeText(this, "Please check your mail.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ForogtPassword.this,LoginActivity.class));

        });
    }
}