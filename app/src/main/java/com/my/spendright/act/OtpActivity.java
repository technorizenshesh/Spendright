package com.my.spendright.act;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.my.spendright.R;
import com.my.spendright.databinding.ActivityOtpBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class OtpActivity extends AppCompatActivity {

    Context mContext;
    String finalOtp = "";
    ActivityOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnSignup.setOnClickListener(v ->
                {
                    if (TextUtils.isEmpty(binding.et1.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    }  else if (TextUtils.isEmpty(binding.et3.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et4.getText().toString().trim())) {
                        Toast.makeText(mContext, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    }else {
                        finalOtp =
                                binding.et1.getText().toString().trim() +
                                        binding.et2.getText().toString().trim() +
                                        binding.et3.getText().toString().trim() +
                                        binding.et4.getText().toString().trim();
                    }
                }
        );


        init();

    }

    private void init() {
        binding.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et2.setText("");
                    binding.et2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et3.setText("");
                    binding.et3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et4.setText("");
                    binding.et4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }



}