package com.my.spendright.act.ui.thismonth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.my.spendright.Fragment.BottomFragment;
import com.my.spendright.R;
import com.my.spendright.databinding.FragmentThisMonthBinding;


public class ThisMonthFragment extends Fragment {


    FragmentThisMonthBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_this_month, container, false);


       binding.llMore.setOnClickListener(view -> {

           BottomFragment bottomSheetFragment = new BottomFragment(getContext());
           bottomSheetFragment.show(getChildFragmentManager(), "ModalBottomSheet");

       });

       binding.llMoreOne.setOnClickListener(view -> {

           BottomFragment bottomSheetFragment = new BottomFragment(getContext());
           bottomSheetFragment.show(getChildFragmentManager(), "ModalBottomSheet");

       });

       binding.llMoreTwo.setOnClickListener(view -> {

           BottomFragment bottomSheetFragment = new BottomFragment(getContext());
           bottomSheetFragment.show(getChildFragmentManager(), "ModalBottomSheet");

       });


        return binding.getRoot();

    }
}