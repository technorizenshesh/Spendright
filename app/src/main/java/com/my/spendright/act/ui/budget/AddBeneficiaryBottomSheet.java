package com.my.spendright.act.ui.budget;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.model.BankListModel;
import com.my.spendright.act.ui.budget.withdraw.WithdrawYourBankAct;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.FragmentAddBeneficiaryBinding;
import com.my.spendright.databinding.FragmentAddExpenseCateBinding;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBeneficiaryBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "AddBeneficiaryBottomSheet";
    BottomSheetDialog dialog;
    FragmentAddBeneficiaryBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public CreateVirtualListener listener;

    SessionManager sessionManager;

    private String bvnNumber="";
    private String bankCode="";

    ArrayList<BankListModel.Result>arrayList;



    public AddBeneficiaryBottomSheet(String bvnNumber) {
        this.bvnNumber = bvnNumber;
    }


    public AddBeneficiaryBottomSheet callBack(CreateVirtualListener listener) {
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_add_beneficiary, null, false);
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


        arrayList = new ArrayList<>();

        binding.edAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==10) {
                    Log.e("onTextChange===",charSequence.length()+"");
                    checkBeneficiary();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged===","======");
            }
        });



        binding.tvBankName.setOnClickListener(view ->{
            if(arrayList.size()>0) showDropDownBankList(view,binding.tvBankName,arrayList);
        } );


        binding.btnBeneficiary.setOnClickListener(view ->
        {
                if(binding.edAccountNumber.getText().toString().equalsIgnoreCase(""))
                    Toast.makeText(getActivity(), getString(R.string.please_enter_account_number), Toast.LENGTH_SHORT).show();
                else   addBeneficiary();
        });



        getAllBanks();
    }

    private void getAllBanks() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_for_bank_list();
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "get BANK LIST Response = " + stringResponse);
                    if (jsonObject.getBoolean("requestSuccessful") == true) {
                        BankListModel bankListModel = new Gson().fromJson(stringResponse, BankListModel.class);
                        arrayList.clear();
                        arrayList.addAll(bankListModel.getResult());
                        binding.tvBankName.setText(bankListModel.getResult().get(0).getBank());
                        bankCode = arrayList.get(0).getBankCode();


                    } else {

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

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownBankList(View v, TextView textView, List<BankListModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getBank());
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).getBank().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    bankCode = stringList.get(i).getBankCode();
                    textView.setText(menuItem.getTitle());
                }
            }
            return true;
        });
        popupMenu.show();
    }

    private void checkBeneficiary() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());
        requestBody.put("bankCode",bankCode);
        requestBody.put("accountNumber", binding.edAccountNumber.getText().toString());
        requestBody.put("bvn",bvnNumber );
        Log.e(TAG, "check Beneficiary Request==" + requestBody.toString());
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_check_beneficiary(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "check Beneficiary Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONObject messageObj = jsonObject.getJSONObject("message");
                        JSONObject responseBodyObj = messageObj.getJSONObject("responseBody");
                        binding.tvAccountName.setText(responseBodyObj.getString("accountName"));
                        binding.tvAccountName.setTextColor(getResources().getColor(R.color.green));
                       // binding.chkAddBeneficiary.setVisibility(View.VISIBLE);
                      //  binding.btnContinue.setVisibility(View.VISIBLE);

                    } else {
                      //  binding.chkAddBeneficiary.setVisibility(View.GONE);
                      //  binding.btnContinue.setVisibility(View.GONE);

                        binding.tvAccountName.setTextColor(getResources().getColor(R.color.red));
                        // binding.tvAccountName.setText(jsonObject.getString("message"));
                        binding.tvAccountName.setText(getString(R.string.invalid_account_number));

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


    private void addBeneficiary() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());
        requestBody.put("bankCode",bankCode);
        requestBody.put("accountNumber", binding.edAccountNumber.getText().toString());
        requestBody.put("bvn",bvnNumber );
        requestBody.put("beneficiary_user_id","");


        Log.e(TAG, "add Beneficiary account Request==" + requestBody.toString());
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_add_beneficiary(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "get Beneficiary account Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), getString(R.string.beneficiary_add_successfully), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        listener.onVirtual("Beneficiary Added","1");

                    } else {
                        Toast.makeText(getActivity(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
