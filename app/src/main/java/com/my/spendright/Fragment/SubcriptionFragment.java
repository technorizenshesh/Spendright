package com.my.spendright.Fragment;

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

import com.my.spendright.Model.GetSchdulepAymentModel;
import com.my.spendright.R;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.act.SchdulePaymentDetails;
import com.my.spendright.adapter.GetSchdulePaymentAdapter;
import com.my.spendright.databinding.FragmentSubscriptionBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubcriptionFragment extends Fragment {

    FragmentSubscriptionBinding binding;
    GetSchdulePaymentAdapter mAdapter;
    ArrayList<GetSchdulepAymentModel.Result> modelList=new ArrayList<>();
    private SessionManager sessionManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //View root = inflater.inflate(R.layout.fragment_subscription, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscription, container, false);


        sessionManager = new SessionManager(getActivity());


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetPaymentMethod();
        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void setAdapter(ArrayList<GetSchdulepAymentModel.Result> modelList)
    {
        mAdapter = new GetSchdulePaymentAdapter(getActivity(), this.modelList);
        binding.recycleGiving.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleGiving.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleGiving.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new GetSchdulePaymentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetSchdulepAymentModel.Result model) {
                startActivity(new Intent(getActivity(), SchdulePaymentDetails.class).putExtra("id",model.getId()));
            }
        });
    }

    private void GetPaymentMethod(){
        modelList.clear();
        Call<GetSchdulepAymentModel> call = RetrofitClients.getInstance().getApi()
                .get_schedule_payment(sessionManager.getUserID(),"Subscription");
        call.enqueue(new Callback<GetSchdulepAymentModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetSchdulepAymentModel> call, Response<GetSchdulepAymentModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetSchdulepAymentModel finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        modelList= (ArrayList<GetSchdulepAymentModel.Result>) finallyPr.getResult();
                        setAdapter(modelList);

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetSchdulepAymentModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}