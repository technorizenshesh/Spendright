package com.my.spendright.act.ui.dashboard;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.my.spendright.R;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.AddTrasactionScreen;
import com.my.spendright.act.MyBudgets;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {

    FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard, container, false);

        binding.addImgAccount.setOnClickListener(v -> {
           startActivity(new Intent(getActivity(), AddActivity.class));
         //   startActivity(new Intent(getActivity(), MyBudgets.class));
        });

        binding.imgAddTrasaction.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddTrasactionScreen.class));
        });

        binding.payMentInformation.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), SchdulePayment.class));
        });

        return binding.getRoot();
    }

}