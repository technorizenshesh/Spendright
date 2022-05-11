package com.my.spendright.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.my.spendright.R;


public class InvestmentFragment extends Fragment {

    // FragmentInvestmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_investment, container, false);
       // binding = DataBindingUtil.inflate(inflater, R.layout.fragment_investment, container, false);


        return root;
    }
}