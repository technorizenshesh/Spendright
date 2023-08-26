package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.Model.NotificationModel;
import com.my.spendright.R;
import com.my.spendright.adapter.NotificationAdapter;
import com.my.spendright.databinding.ActivityNotificationBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    public String TAG ="Notification";

    ActivityNotificationBinding binding;
    SessionManager sessionManager;
    NotificationAdapter adapter;
    ArrayList<NotificationModel.Result>arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_notification);
        sessionManager = new SessionManager(Notification.this);

        initViews();


    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new NotificationAdapter(Notification.this,arrayList);
        binding.rvNotification.setAdapter(adapter);


    /*    binding.llNoti.setOnClickListener(v -> {
            startActivity(new Intent(Notification.this,ReminderScreen.class));
        });*/

        binding.imgBack.setOnClickListener(v -> {
            finish();
        });

        getAllNotification();
    }


    private void getAllNotification() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_user_notification(sessionManager.getUserID());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Notification Response = " + stringResponse);


                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        NotificationModel finallyPr = new Gson().fromJson(stringResponse, NotificationModel.class);
                        arrayList.clear();
                        arrayList.addAll(finallyPr.getResult());
                        adapter.notifyDataSetChanged();


                    } else
                    {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();


                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}