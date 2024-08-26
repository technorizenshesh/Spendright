package com.my.spendright.TvCabelBill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.R;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.act.ui.education.EducationAct;
import com.my.spendright.act.ui.education.EducationModel;
import com.my.spendright.airetime.ConfirmPaymentAireTimeAct;
import com.my.spendright.airetime.PaymentBillAireTime;
import com.my.spendright.databinding.ActivityShowmaxBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowmaxAct extends AppCompatActivity {
    String TAG ="ShowmaxAct";
    ActivityShowmaxBinding binding;
    SessionManager sessionManager;
    String myWalletBalace="",ServicesSubscriptionId="",ServicesSubscriptionName="";

   ArrayList<EducationModel.Content.Variation>showMaxArrayList;
    String serviceID="",variation_code="",amount="",pck="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_showmax);
        sessionManager = new SessionManager(ShowmaxAct.this);

        initViews();
    }

    private void initViews() {
        showMaxArrayList = new ArrayList<>();

        if(getIntent()!=null)
        {
            ServicesSubscriptionId =getIntent().getStringExtra("ServicesSubscriptionId");
            ServicesSubscriptionName =getIntent().getStringExtra("ServicesSubscriptionName");
            binding.txtCountry.setCountryForPhoneCode(234);


        }


        binding.tvPackage.setOnClickListener(view -> showDropDownShowMax(ShowmaxAct.this,view,binding.tvPackage,showMaxArrayList));

        binding.RRPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if(pck.equalsIgnoreCase(""))
                    {
                        Toast.makeText(ShowmaxAct.this, "Please select package", Toast.LENGTH_SHORT).show();

                    }

                    else if(amount.equalsIgnoreCase(""))
                {
                    Toast.makeText(ShowmaxAct.this, "Please enter Amount", Toast.LENGTH_SHORT).show();

                }


                   else if(binding.edtPhone.getText().toString().equalsIgnoreCase(""))
                    {
                        Toast.makeText(ShowmaxAct.this, "Please enter phone Number", Toast.LENGTH_SHORT).show();

                    }


                    else
                    {

                        startActivity(new Intent(ShowmaxAct.this,  ConfirmPaymentShowMaxAct.class)
                                .putExtra("ServicesId",serviceID)
                                .putExtra("ServicesName",binding.tvPackage.getText().toString())
                                .putExtra("amount",amount)
                                .putExtra("phone",binding.txtCountry.getSelectedCountryCodeWithPlus()+binding.edtPhone.getText().toString())
                                .putExtra("MyCuurentBlance",myWalletBalace)
                                .putExtra("variationCode",variation_code)
                        );
                    }
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        showMaxMethod();
    }

    private void showMaxMethod() {
         binding.progressBar.setVisibility(View.VISIBLE);
         Call<ResponseBody> call = RetrofitClients.getInstance().getApi().ApiWAECRegistration(Preference.getHeader(ShowmaxAct.this),"showmax");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        JSONObject result = jsonObject.getJSONObject("result");
                        Log.e(TAG, "ShowMax Response = " + stringResponse);
                        if(result.getString("response_description").equalsIgnoreCase("000")){
                            EducationModel educationModel = new Gson().fromJson(result.toString(),EducationModel.class);
                            showMaxArrayList.clear();
                            showMaxArrayList.addAll(educationModel.getContent().getVariations());

                        }

                        else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                            sessionManager.logoutUser();
                            Toast.makeText(ShowmaxAct.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ShowmaxAct.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }


                        else Toast.makeText(ShowmaxAct.this,jsonObject.getJSONObject("content").getString("errors"),Toast.LENGTH_LONG).show();


                    }catch (Exception e){
                        e.printStackTrace();
                    }





                } else {
                    Toast.makeText(ShowmaxAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void showDropDownShowMax(Context mContext, View v, TextView textView, ArrayList<EducationModel.Content.Variation>mainList) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        for (int i = 0; i < mainList.size(); i++) {
            popupMenu.getMenu().add(mainList.get(i).getName());
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for(int j=0;j<mainList.size();j++){
                if(menuItem.getTitle().equals(mainList.get(j).getName())) {
                    serviceID = "showmax";
                    Log.e("serviceID===", serviceID);
                    pck = mainList.get(j).getName();
                    variation_code = mainList.get(j).getVariationCode();
                    amount = mainList.get(j).getVariationAmount();
                    binding.tvAmount.setText(amount);
                }

            }


            return true;
        });
        popupMenu.show();
    }



}
