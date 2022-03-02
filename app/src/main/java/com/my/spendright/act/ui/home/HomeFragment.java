package com.my.spendright.act.ui.home;

import android.content.Intent;
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
import com.my.spendright.act.Notification;
import com.my.spendright.act.PaymentInformation;
import com.my.spendright.act.PaymentReport;
import com.my.spendright.act.ReminderScreen;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.RRAddAccount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddActivity.class));
        });

        binding.LLelectCIty.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PaymentInformation.class));
          //  startActivity(new Intent(getActivity(), ReminderScreen.class));
        });
        binding.bill2.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PaymentInformation.class));
          //  startActivity(new Intent(getActivity(), ReminderScreen.class));
        });

        binding.bill3.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PaymentInformation.class));
          //  startActivity(new Intent(getActivity(), ReminderScreen.class));
        });
        binding.bill4.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PaymentInformation.class));
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
        
        return binding.getRoot();
    }
}