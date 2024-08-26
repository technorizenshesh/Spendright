package com.my.spendright.act.ui.education;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityJambBinding;
import com.my.spendright.utils.Preference;

public class JAMBAct extends AppCompatActivity {
    ActivityJambBinding binding;
    String serviceID="",variationCode="",amount="",serviceName="",profileId="",customerName="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_jamb);
       initViews();
    }

    private void initViews() {
        if(getIntent()!=null){
            binding.tvTitle.setText(getIntent().getStringExtra("title"));
            serviceID = getIntent().getStringExtra("serviceID");
            variationCode = getIntent().getStringExtra("variation_code");
            amount = getIntent().getStringExtra("amount");
            serviceName = getIntent().getStringExtra("serviceName");
            profileId = getIntent().getStringExtra("profileId");
            customerName = getIntent().getStringExtra("customerName");

            binding.tvAmount.setText((Preference.doubleToStringNoDecimal(Double.parseDouble(amount))) + "");
            binding.txtCountry.setCountryForPhoneCode(234);

        }



        binding.RRPay.setOnClickListener(view -> {

            if(binding.edtPhone.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
             else {
                startActivity(new Intent(JAMBAct.this, ConfirmPaymentJAMBAct.class)
                        .putExtra("serviceId", serviceID)
                        .putExtra("serviceName", serviceName)
                        .putExtra("amount", amount)
                        .putExtra("phone", binding.txtCountry.getSelectedCountryCodeWithPlus() + binding.edtPhone.getText().toString())
                        .putExtra("variationCode", variationCode)
                        .putExtra("profileId", profileId)
                        .putExtra("customerName", customerName));


            }
        });



        binding.imgBack.setOnClickListener(view -> finish());

        binding.edtPhone.addTextChangedListener(mMoneyWatcher);

    }


    private TextWatcher mMoneyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!s.toString().isEmpty()) {
                binding.edtPhone.removeTextChangedListener(mMoneyWatcher);
                if (s.toString().charAt(0)=='0') {
                    binding.edtPhone.setText(s.toString().substring(1,s.length()));
                }

                binding.edtPhone.addTextChangedListener(mMoneyWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



}
