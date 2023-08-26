package com.my.spendright.act.ui.budget;

import android.app.Dialog;
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
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.act.ui.settings.AddBudgetCatAct;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.FragmentAddExpenseCateBinding;
import com.my.spendright.databinding.FragmentAddNewGrpCateBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExpenseCateBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "AddExpenseCateBottomSheet";
    BottomSheetDialog dialog;
    FragmentAddExpenseCateBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public CreateVirtualListener listener;

    SessionManager sessionManager;

    private String type ="EXPENSE";
    private String catEmoji ="",bGrpId="";
    EmojiPopup popup;




    public AddExpenseCateBottomSheet(String bGrpId) {
        this.bGrpId = bGrpId;
    }


    public AddExpenseCateBottomSheet callBack(CreateVirtualListener listener) {
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_add_expense_cate, null, false);
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

        popup = EmojiPopup.Builder.fromRootView(binding.rootView   ).build(binding.etEmoji);


        binding.btnSave.setOnClickListener(view -> validation());

        binding.rlEmoji.setOnClickListener(view -> {
            popup.toggle();
            binding.llTextViews.addView(getEmojiTextView());
            //etEmoji.getText().clear();
        });
    }

    private EmojiTextView getEmojiTextView() {
        EmojiTextView tvEmoji = (EmojiTextView) LayoutInflater
                .from(getActivity())
                .inflate(R.layout.text_view_emoji, binding.llTextViews,false);
        tvEmoji.setText(binding.etEmoji.getText().toString());
        return tvEmoji;
    }



    private void validation() {
        catEmoji = binding.etEmoji.getText().toString();

      /*  if(binding.SitchBtn.isChecked()) type ="EXPENSE";
        else type ="INCOME";*/


        if (catEmoji.equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_select_emoji), Toast.LENGTH_LONG).show();
        else if (binding.edtCatName.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(getActivity(), getString(R.string.please_enter_cat_name), Toast.LENGTH_LONG).show();

        else  {
            if (sessionManager.isNetworkAvailable()) createBudgetCat();
            else Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void createBudgetCat() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cat_name",binding.edtCatName.getText().toString());
        requestBody.put("cat_type","EXPENSE");
        requestBody.put("cat_user_id", sessionManager.getUserID());
        requestBody.put("cat_emoji", Preference.encodeEmoji(binding.etEmoji.getText().toString()));

        Log.e(TAG,"add category BudgetRequest=="+requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_add_budget_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "add category BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(),getString(R.string.budget_category_created_successfully), Toast.LENGTH_SHORT).show();
                        listener.onVirtual("","");
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