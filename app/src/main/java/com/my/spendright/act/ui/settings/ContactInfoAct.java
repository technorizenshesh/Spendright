package com.my.spendright.act.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.model.BankListModel;
import com.my.spendright.act.ui.settings.adapter.ContactUsAdapter;
import com.my.spendright.act.ui.settings.model.ContactUsModel;
import com.my.spendright.databinding.ActivityContactBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactInfoAct extends AppCompatActivity {
    public String TAG ="ContactInfoAct";
    ArrayList<ContactUsModel.Result>arrayList;
    ContactUsAdapter adapter;
    ActivityContactBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact);
        initViews();
    }

    private void initViews() {
        binding.imgBack.setOnClickListener(v -> finish());

        arrayList = new ArrayList<>();

        adapter = new ContactUsAdapter(ContactInfoAct.this,arrayList);
        binding.rvContact.setAdapter(adapter);

        getContactUs();
    }


    private void getContactUs() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> loginCall = RetrofitClients.getInstance().getApi().Api_contact_us();
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Contactus LIST Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        ContactUsModel contactUsModel = new Gson().fromJson(stringResponse, ContactUsModel.class);
                        arrayList.clear();
                        arrayList.addAll(contactUsModel.getResult());
                        adapter.notifyDataSetChanged();


                    } else {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
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
