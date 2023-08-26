package com.my.spendright.act.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.spendright.R;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.FragmentRateBinding;

public class RateBottomSheet extends BottomSheetDialogFragment {


    Context context;
    public String TAG = "CardDetailBottomSheet";
    BottomSheetDialog dialog;
    FragmentRateBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public CreateVirtualListener listener;

    public RateBottomSheet(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_rate, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return dialog;
    }

    private void initBinding() {

     /*   binding.RRClose.setOnClickListener(view -> {
            listener.onVirtual("2");
            dialog.dismiss();
        });*/
    }

}