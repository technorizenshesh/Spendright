package com.my.spendright.act.ui.home.virtualcards;

import android.app.Dialog;
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
import com.my.spendright.databinding.FragmentCardDetailsBinding;

public class CardDetailBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "CardDetailBottomSheet";
    BottomSheetDialog dialog;
    FragmentCardDetailsBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public CreateVirtualListener listener;


    public CardDetailBottomSheet() {
    }


    public CardDetailBottomSheet callBack(CreateVirtualListener listener) {
        this.listener = listener;
        return this;
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_card_details, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return dialog;
    }

    private void initBinding() {

        binding.RRClose.setOnClickListener(view -> {
            listener.onVirtual("2","");
            dialog.dismiss();
        });
    }

}
