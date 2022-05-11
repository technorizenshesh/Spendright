package com.my.spendright.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.my.spendright.R;
import com.my.spendright.databinding.FragmentGivingBinding;

public class GivingFragment extends Fragment {

    FragmentGivingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_giving, container, false);


        return binding.getRoot();
    }
}