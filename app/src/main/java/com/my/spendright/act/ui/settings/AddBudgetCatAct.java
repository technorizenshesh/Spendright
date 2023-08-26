package com.my.spendright.act.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.R;
import com.my.spendright.databinding.ActivityAddBudgetCatBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBudgetCatAct extends AppCompatActivity {
    private String TAG = "AddBudgetCatAct";
    private SessionManager sessionManager;

    ActivityAddBudgetCatBinding binding;
    private String type ="";
    private String catEmoji ="";
     EmojiPopup popup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_budget_cat);
        sessionManager = new SessionManager(AddBudgetCatAct.this);

        initViews();
    }

    private void initViews() {
        if(getIntent()!=null) type = getIntent().getStringExtra("type");


         popup = EmojiPopup.Builder.fromRootView(binding.rootView   ).build(binding.etEmoji);

        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnSave.setOnClickListener(view -> validation());

        binding.rlEmoji.setOnClickListener(view -> {
            popup.toggle();
            binding.llTextViews.addView(getEmojiTextView());
            //etEmoji.getText().clear();
        });





    }

    private EmojiTextView getEmojiTextView() {
        EmojiTextView tvEmoji = (EmojiTextView) LayoutInflater
                .from(getApplicationContext())
                .inflate(R.layout.text_view_emoji, binding.llTextViews,false);
        tvEmoji.setText(binding.etEmoji.getText().toString());
        return tvEmoji;
    }



    private void validation() {
        catEmoji = binding.etEmoji.getText().toString();

      /*  if(binding.SitchBtn.isChecked()) type ="EXPENSE";
        else type ="INCOME";*/


        if (catEmoji.equalsIgnoreCase(""))
            Toast.makeText(AddBudgetCatAct.this, getString(R.string.please_select_emoji), Toast.LENGTH_LONG).show();
         else if (binding.edtCatName.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(AddBudgetCatAct.this, getString(R.string.please_enter_cat_name), Toast.LENGTH_LONG).show();

        else  {
            if (sessionManager.isNetworkAvailable()) createBudgetCat();
            else Toast.makeText(AddBudgetCatAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void createBudgetCat() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cat_name",binding.edtCatName.getText().toString());
        requestBody.put("cat_type",type);
        requestBody.put("cat_user_id", sessionManager.getUserID());
        requestBody.put("cat_emoji", Preference.encodeEmoji(binding.etEmoji.getText().toString()));

        Log.e(TAG,"add category BudgetRequest=="+requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_add_budget_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "add category BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(AddBudgetCatAct.this,getString(R.string.budget_category_created_successfully), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddBudgetCatAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}