package com.my.spendright.act.ui.budget;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.my.spendright.act.KYCAct;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.FragmentAddNewGrpBinding;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.defaults.colorpicker.ColorPickerPopup;

public class AddNewGrpBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "AddNewGrpBottomSheet";
    BottomSheetDialog dialog;
    FragmentAddNewGrpBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public CreateVirtualListener listener;
    private  String hexColor ="";
    SessionManager sessionManager;
    public AddNewGrpBottomSheet() {
    }


    public AddNewGrpBottomSheet callBack(CreateVirtualListener listener) {
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_add_new_grp, null, false);
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

        binding.RRAdd.setOnClickListener(view -> {
            validation();

        });

        binding.imgEdit.setOnClickListener(view -> {
            new ColorPickerPopup.Builder(getActivity())
                    .initialColor(Color.RED) // Set initial color
                    .enableBrightness(true) // Enable brightness slider or not
                    .enableAlpha(true) // Enable alpha slider or not
                    .okTitle("Choose")
                    .cancelTitle("Cancel")
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(view, new ColorPickerPopup.ColorPickerObserver() {
                        @Override
                        public void onColorPicked(int color) {
                            view.setBackgroundColor(color);
                            hexColor = String.format("#%06X", (0xFFFFFF & color));
                            Toast.makeText(getActivity(),hexColor+"",Toast.LENGTH_LONG).show();

                        }

                       /* @Override
                        public void onColor(int color, boolean fromUser) {
                            Toast.makeText(getActivity(),"",Toast.LENGTH_LONG).show();
                        }*/
                    });
        });

    }

    private void validation() {

        if (hexColor.equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_select_grp_color), Toast.LENGTH_LONG).show();
        else if (binding.edtGrpName.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_enter_grp_name), Toast.LENGTH_LONG).show();
        else if (binding.edtDescription.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_enter_grp_des), Toast.LENGTH_LONG).show();
        else  {


            if (sessionManager.isNetworkAvailable()) createBudgetGrp();
            else Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }


    private void createBudgetGrp() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("group_name",binding.edtGrpName.getText().toString());
        requestBody.put("group_description",binding.edtDescription.getText().toString() );
        requestBody.put("group_color",hexColor);
        requestBody.put("group_owner_id", (sessionManager.getUserID()));

        Log.e(TAG,"add grp BudgetRequest=="+requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_create_budget_grp(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "add grp BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(),getString(R.string.group_created_successfully), Toast.LENGTH_SHORT).show();
                        listener.onVirtual("1","");
                        dialog.dismiss();

                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


}