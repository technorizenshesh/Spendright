package com.my.spendright.act.ui.thismonth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.spendright.Fragment.BottomFragment;
import com.my.spendright.GraphAct;
import com.my.spendright.Model.GetMyAccountModel;
import com.my.spendright.R;
import com.my.spendright.act.MyAccountAct;
import com.my.spendright.adapter.MyAccountAdapter;
import com.my.spendright.databinding.ActivityExpensesBudgetBinding;
import com.my.spendright.databinding.ActivityMyBudgetsBinding;
import com.my.spendright.databinding.FragmentThisMonthBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ThisMonthFragment extends Fragment {
    ActivityMyBudgetsBinding binding;
    private SessionManager sessionManager;
    MyAccountAdapter mAdapter;
    ArrayList<GetMyAccountModel.AccountDetail> modelList=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_my_budgets, container, false);

        sessionManager=new SessionManager(getActivity());

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getMonthBugdet();
        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(ArrayList<GetMyAccountModel.AccountDetail> modelList) {


        mAdapter = new MyAccountAdapter(getActivity(),modelList);
        binding.recyclermyAccount.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recyclermyAccount.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MyAccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetMyAccountModel.AccountDetail model) {

                startActivity(new Intent(getActivity(), GraphAct.class).putExtra("month",model.getMonthName()));

            }
        });
    }

    private void getMonthBugdet(){
        Call<GetMyAccountModel> call = RetrofitClients.getInstance().getApi()
                .getMonthBugdet(sessionManager.getUserID());
        call.enqueue(new Callback<GetMyAccountModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetMyAccountModel> call, Response<GetMyAccountModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetMyAccountModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        modelList= (ArrayList<GetMyAccountModel.AccountDetail>) finallyPr.getAccountDetail();

                        setAdapter(modelList);
                    } else {

                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetMyAccountModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}