package com.my.spendright.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.spendright.R;
import com.my.spendright.act.ExpensesBudget;
import com.my.spendright.act.SchdulePaymentMethod;
import com.my.spendright.databinding.BottomFragmentBinding;


public class BottomFragment extends BottomSheetDialogFragment {


    Context context;

    BottomFragmentBinding  binding;

    public BottomFragment(Context context) {
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setupDialog(Dialog dialog, int style) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        binding= DataBindingUtil.inflate(layoutInflater, R.layout.bottom_fragment, null, false);

        binding.RRExpenses.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ExpensesBudget.class));

        });

        binding.RRSchdule.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SchdulePaymentMethod.class));

        });

        dialog.setContentView(binding.getRoot());
    }

}
