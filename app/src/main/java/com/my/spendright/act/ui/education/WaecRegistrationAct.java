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
import com.my.spendright.databinding.ActivityWaecRegistrationBinding;
import com.my.spendright.utils.Preference;

public class WaecRegistrationAct extends AppCompatActivity {
    ActivityWaecRegistrationBinding binding;
    String serviceID = "", variationCode = "", unitPrice = "", amount = "", serviceName = "";
    int i = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_waec_registration);
        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            binding.tvTitle.setText(getIntent().getStringExtra("title"));
            serviceID = getIntent().getStringExtra("serviceID");
            variationCode = getIntent().getStringExtra("variation_code");
            unitPrice = getIntent().getStringExtra("amount");
            serviceName = getIntent().getStringExtra("serviceName");
            binding.tvUnitPrice.setText(Preference.doubleToStringNoDecimal(Double.parseDouble(unitPrice)));
            amount =  Preference.doubleToStringNoDecimal(i * Double.parseDouble(unitPrice));
            binding.tvAmount.setText((Preference.doubleToStringNoDecimal(i * Double.parseDouble(unitPrice))) + "");
            binding.txtCountry.setCountryForPhoneCode(234);

        }

        binding.tvPlus.setOnClickListener(view -> {
            i++;
            amount = String.valueOf(Double.parseDouble(unitPrice) * i);
            binding.tvQuantity.setText(i + "");
            binding.tvAmount.setText(amount);
            binding.tvAmount.setText((Preference.doubleToStringNoDecimal(Double.parseDouble(amount))) + "");


        });

        binding.tvMinus.setOnClickListener(view -> {
            if (i > 1) {
                i--;
                amount = String.valueOf(Double.parseDouble(unitPrice) * i);
                binding.tvQuantity.setText(i + "");
                binding.tvAmount.setText((Preference.doubleToStringNoDecimal(Double.parseDouble(amount))) + "");

            }

        });

        binding.RRPay.setOnClickListener(view -> {
            if (binding.edtPhone.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();

            else {
                startActivity(new Intent(WaecRegistrationAct.this, ConfirmPaymentWAECAct.class)
                        .putExtra("serviceId", serviceID)
                        .putExtra("serviceName", serviceName)
                        .putExtra("amount", amount)
                        .putExtra("phone", binding.txtCountry.getSelectedCountryCodeWithPlus() + binding.edtPhone.getText().toString())
                        .putExtra("variationCode", variationCode)
                        .putExtra("quantity", binding.tvQuantity.getText().toString()));
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
