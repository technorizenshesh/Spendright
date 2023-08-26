package com.my.spendright.act.ui.budget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.spendright.R;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.FragmentAddExpenseCateBinding;
import com.my.spendright.databinding.FragmentReferEarnBinding;
import com.my.spendright.utils.SessionManager;
import com.vanniktech.emoji.EmojiPopup;

public class ReferEarnBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "ReferEarnBottomSheet";
    BottomSheetDialog dialog;
    FragmentReferEarnBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public CreateVirtualListener listener;

    SessionManager sessionManager;

    private String bGrpId = "";


    public ReferEarnBottomSheet(String bGrpId) {
        this.bGrpId = bGrpId;
    }


    public ReferEarnBottomSheet callBack(CreateVirtualListener listener) {
        this.listener = listener;
        return this;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_refer_earn, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return dialog;
    }

    private void initBinding() {

        sessionManager = new SessionManager(getActivity());
        binding.edtReferral.setText(bGrpId);
        binding.btnSubmit.setOnClickListener(view -> {
            if (bGrpId.equalsIgnoreCase(""))
                Toast.makeText(getActivity(), getString(R.string.please_enter_referra_code), Toast.LENGTH_LONG).show();
            else  {
                listener.onVirtual(binding.edtReferral.getText().toString(),"referral");
                dialog.dismiss();
            }
        });


    }

}