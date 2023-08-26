package com.my.spendright.act.ui.settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.spendright.Model.GetAllAccountModel;
import com.my.spendright.R;
import com.my.spendright.adapter.MyAccoutAllAdapter;
import com.my.spendright.databinding.ActivityMyAccountBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountAct extends AppCompatActivity {

    ActivityMyAccountBinding binding;
    private SessionManager sessionManager;
    MyAccoutAllAdapter mAdapter;
    ArrayList<GetAllAccountModel.Result> modelList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_account);


        sessionManager=new SessionManager(MyAccountAct.this);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetProfileMethod();
        }else {
            Toast.makeText(MyAccountAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }


    private void setAdapter(ArrayList<GetAllAccountModel.Result> modelList) {
        mAdapter = new MyAccoutAllAdapter(MyAccountAct.this,modelList);
        binding.recyCleMyAccount.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyAccountAct.this);
        binding.recyCleMyAccount.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recyCleMyAccount.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MyAccoutAllAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetAllAccountModel.Result model) {

            }
        });
    }


    private void GetProfileMethod(){
        Call<GetAllAccountModel> call = RetrofitClients.getInstance().getApi()
                .get_getAllAccount(sessionManager.getUserID());
        call.enqueue(new Callback<GetAllAccountModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetAllAccountModel> call, Response<GetAllAccountModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetAllAccountModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        modelList= (ArrayList<GetAllAccountModel.Result>) finallyPr.getResult();
                        setAdapter(modelList);

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetAllAccountModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void DeleteGrpMethod(String id){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_deleteAccount(id);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    Toast.makeText(MyAccountAct.this, "SuccessFully", Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    //binding.txtEmty.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
               // binding.txtEmty.setVisibility(View.VISIBLE);
            }
        });
    }





}