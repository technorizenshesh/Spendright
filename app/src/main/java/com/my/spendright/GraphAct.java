package com.my.spendright;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.adapter.MySetBudgetAdapter;
import com.my.spendright.databinding.ActivityGraphBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphAct extends AppCompatActivity {

    ActivityGraphBinding binding;
    private SessionManager sessionManager;
    ArrayList<GetSetbudgetExpence.Result> modelList=new ArrayList<>();
    private MySetBudgetAdapter mAdapter;
    String Month="";
    String Type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_graph);


        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        sessionManager = new SessionManager(GraphAct.this);

        if(getIntent()!=null)
        {
             Month = getIntent().getStringExtra("month");
            Type = getIntent().getStringExtra("Type");
        }

        if (sessionManager.isNetworkAvailable()) {
            getExpenceMethod();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

/*    private void setAdapter(ArrayList<GetSetbudgetExpence.Result> modelList) {
        mAdapter = new SetBudgetAdapter(GraphAct.this,modelList,sessionManager.getUserID(),sessionManager.getAccontId());
        binding.recyclerGrp.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GraphAct.this);
        binding.recyclerGrp.setLayoutManager(linearLayoutManager);
        binding.recyclerGrp.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new SetBudgetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetSetbudgetExpence.Result model) {

            }
        });
    }
    */
    private void setAdapter(ArrayList<GetSetbudgetExpence.Result> modelList) {
        mAdapter = new MySetBudgetAdapter(GraphAct.this,modelList,sessionManager.getUserID(),sessionManager.getAccontId());
        binding.recyclerGrp.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GraphAct.this);
        binding.recyclerGrp.setLayoutManager(linearLayoutManager);
        binding.recyclerGrp.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MySetBudgetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetSetbudgetExpence.Result model) {

            }
        });
    }

    public void getExpenceMethod(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<GetSetbudgetExpence> call = RetrofitClients.getInstance().getApi()
                .Api_get_group_expence_by_month(sessionManager.getUserID(),Month);
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


}