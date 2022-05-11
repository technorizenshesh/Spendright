package com.my.spendright.act.SetBudget;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.my.spendright.Model.CreateGroupModel;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.Model.HomeModal;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.act.LoginOne;
import com.my.spendright.adapter.SetBudgetAdapter;
import com.my.spendright.databinding.ActivitySetBudgetBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetBudgetActivity extends AppCompatActivity {

    ActivitySetBudgetBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    private SetBudgetAdapter mAdapter;
    ArrayList<GetSetbudgetExpence.Result> modelList=new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_set_budget);

        sessionManager = new SessionManager(SetBudgetActivity.this);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRAddNewGrp.setOnClickListener(v -> {
            AlertDaliogArea();
        });

        if (sessionManager.isNetworkAvailable()) {
            getExpenceMethod();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void setAdapter(ArrayList<GetSetbudgetExpence.Result> modelList) {

       /*     this.modelList.add(new HomeModal("Bills",1));
            this.modelList.add(new HomeModal("Frequent",1));
            this.modelList.add(new HomeModal("Goals",1));
            this.modelList.add(new HomeModal("Non Monthly\n",1));*/

        mAdapter = new SetBudgetAdapter(SetBudgetActivity.this,modelList,sessionManager.getUserID(),sessionManager.getAccontId());
        binding.recyclerGrp.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SetBudgetActivity.this);
        binding.recyclerGrp.setLayoutManager(linearLayoutManager);
        binding.recyclerGrp.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new SetBudgetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetSetbudgetExpence.Result model) {

            }
        });
    }

    private void AlertDaliogArea() {

        LayoutInflater li;
        RelativeLayout RRAdd;
        EditText edtName;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(SetBudgetActivity.this);
        promptsView = li.inflate(R.layout.alert_grp_add, null);
        RRAdd = (RelativeLayout) promptsView.findViewById(R.id.RRAdd);
        edtName = (EditText) promptsView.findViewById(R.id.edtName);
        alertDialogBuilder = new AlertDialog.Builder(SetBudgetActivity.this);
        alertDialogBuilder.setView(promptsView);

        RRAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GrpName=edtName.getText().toString();
                if(GrpName.equalsIgnoreCase(""))
                {
                    Toast.makeText(SetBudgetActivity.this, "Please Enter Group Name.", Toast.LENGTH_SHORT).show();

                }else
                {
                    alertDialog.dismiss();

                    if (sessionManager.isNetworkAvailable()) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        AddGrpMethod(GrpName);
                    }else {
                        Toast.makeText(SetBudgetActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void AddGrpMethod(String grpName){

        Call<CreateGroupModel> call = RetrofitClients.getInstance().getApi()
                .Api_create_group(sessionManager.getUserID(),grpName,"grpName.png",sessionManager.getAccontId());
        call.enqueue(new Callback<CreateGroupModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<CreateGroupModel> call, Response<CreateGroupModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    CreateGroupModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        Toast.makeText(SetBudgetActivity.this, "Add Group  Success", Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.VISIBLE);
                        getExpenceMethod();

                    } else {
                        Toast.makeText(SetBudgetActivity.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    Toast.makeText(SetBudgetActivity.this, "Don't match email/mobile password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CreateGroupModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(SetBudgetActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getExpenceMethod(){

        binding.progressBar.setVisibility(View.VISIBLE);
        Call<GetSetbudgetExpence> call = RetrofitClients.getInstance().getApi()
                .Api_get_group_expence(sessionManager.getUserID(),sessionManager.getAccontId());
        call.enqueue(new Callback<GetSetbudgetExpence>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetSetbudgetExpence> call, Response<GetSetbudgetExpence> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    GetSetbudgetExpence finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        binding.txtEmty.setVisibility(View.GONE);
                        modelList= (ArrayList<GetSetbudgetExpence.Result>) finallyPr.getResult();
                        setAdapter(modelList);
                    } else {
                        binding.txtEmty.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    binding.txtEmty.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetSetbudgetExpence> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtEmty.setVisibility(View.VISIBLE);
            }
        });
    }

    public void DeleteGrpMethod(String id){

        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_delete_group(id);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    Toast.makeText(SetBudgetActivity.this, "SuccessFully", Toast.LENGTH_SHORT).show();

                }catch (Exception e)
                {
                    binding.txtEmty.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtEmty.setVisibility(View.VISIBLE);
            }
        });
    }


}