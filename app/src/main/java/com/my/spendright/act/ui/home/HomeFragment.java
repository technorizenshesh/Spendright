package com.my.spendright.act.ui.home;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.spendright.ElectircalBill.PaymentBill;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.GetSetbudgetExpence;
import com.my.spendright.Model.HomeModal;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.EstimateExpenseBudget;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.act.LoginOne;
import com.my.spendright.act.MyBudgets;
import com.my.spendright.act.Notification;
import com.my.spendright.act.PaymentInformation;
import com.my.spendright.act.PaymentReport;
import com.my.spendright.act.ReminderScreen;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.act.SetBudget.SetBudgetActivity;
import com.my.spendright.act.VtpassActivityLogin;
import com.my.spendright.adapter.MyAccountHomeAdapter;
import com.my.spendright.adapter.SetBudgetAdapter;
import com.my.spendright.databinding.FragmentHomeBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private SessionManager sessionManager;
    MyAccountHomeAdapter mAdapter;
    ArrayList<GetProfileModel.AccountDetail> modelList=new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(getActivity());

       // binding.horizontalScrollView.scrollTo(2,1);

        binding.RRAddAccount.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), AddActivity.class));

        });

       /*  binding.RRAddLoanAccount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddActivity.class).putExtra("AccountId","2").putExtra("AccountName","Loan Accounts"));
        });

         binding.RRAddInvestment.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddActivity.class).putExtra("AccountId","1").putExtra("AccountName","Investment Accounts"));
        });

        binding.txtMybudget.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MyBudgets.class));
        });*/

        binding.LLelectCIty.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), VtpassActivityLogin.class).putExtra("Type","Electricity Bill"));
          //  startActivity(new Intent(getActivity(), PaymentBill.class));
           // startActivity(new Intent(getActivity(), PaymentInformation.class));
        });
        binding.bill2.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), VtpassActivityLogin.class).putExtra("Type","TVSubscription"));
          //  startActivity(new Intent(getActivity(), ReminderScreen.class));

        });

        binding.bill3.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), VtpassActivityLogin.class).putExtra("Type","airetime"));
        });
        binding.bill4.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), VtpassActivityLogin.class).putExtra("Type","data"));

            //  startActivity(new Intent(getActivity(), ReminderScreen.class));
        });

        binding.notification.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Notification.class));
        });

        binding.RRSchdule.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SchdulePayment.class));
        });

        binding.llReport.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), PaymentReport.class));
        });

     /*   binding.txtBudget.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SetBudgetActivity.class));
        });*/

      /*  binding.txtSetBudset.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SetBudgetActivity.class));
        });*/

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetProfileMethod();
        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }


    private void setAdapter(ArrayList<GetProfileModel.AccountDetail> modelList) {


        mAdapter = new MyAccountHomeAdapter(getActivity(),modelList);
        binding.recyclermyAccount.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclermyAccount.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recyclermyAccount.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MyAccountHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetProfileModel.AccountDetail model) {

            }
        });
    }


    private void GetProfileMethod(){
        Call<GetProfileModel> call = RetrofitClients.getInstance().getApi()
                .Api_get_profile_data(sessionManager.getUserID());
        call.enqueue(new Callback<GetProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetProfileModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        modelList= (ArrayList<GetProfileModel.AccountDetail>) finallyPr.getAccountDetail();

                        if(modelList.size()<=0)
                        {
                           binding.recyclermyAccount.setVisibility(View.GONE);
                           binding.RRadd.setVisibility(View.VISIBLE);

                        }else
                        {
                            binding.recyclermyAccount.setVisibility(View.VISIBLE);
                            binding.RRadd.setVisibility(View.GONE);

                            setAdapter(modelList);
                        }


                    } else {

                        Toast.makeText(getActivity(), ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    binding.recyclermyAccount.setVisibility(View.GONE);
                    binding.RRadd.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclermyAccount.setVisibility(View.GONE);
                binding.RRadd.setVisibility(View.VISIBLE);
            }
        });
    }


}