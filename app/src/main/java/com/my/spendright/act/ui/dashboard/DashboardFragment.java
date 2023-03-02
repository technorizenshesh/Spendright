package com.my.spendright.act.ui.dashboard;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.AddTrasactionScreen;
import com.my.spendright.act.MyBudgets;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.databinding.FragmentDashboardBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment
{
    FragmentDashboardBinding binding;
    private SessionManager sessionManager;
    ArrayList<GetBudgetActTransaction.AccountDetail> modelList=new ArrayList<>();
    ArrayList<GetProfileModel.AccountDetail> modelListTransaction=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard, container, false);

        sessionManager = new SessionManager(getActivity());

        binding.RRaddImgAccount.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), AddActivity.class));
        });



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetTransactionMethod();
        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


    }

    private void GetTransactionMethod(){
        Call<GetBudgetActTransaction> call = RetrofitClients.getInstance().getApi()
                .get_budget_by_transaction(sessionManager.getUserID());
        call.enqueue(new Callback<GetBudgetActTransaction>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetBudgetActTransaction> call, Response<GetBudgetActTransaction> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetBudgetActTransaction finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        modelList= (ArrayList<GetBudgetActTransaction.AccountDetail>) finallyPr.getAccountDetail();
                        binding.viewpager.setAdapter(new CustomPagerAdapter(getActivity(),modelList));
                        
                    } else {
                        Toast.makeText(getActivity(), ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetBudgetActTransaction> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}