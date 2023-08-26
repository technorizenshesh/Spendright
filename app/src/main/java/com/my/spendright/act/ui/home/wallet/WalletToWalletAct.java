package com.my.spendright.act.ui.home.wallet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.R;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.act.ui.budget.withdraw.WithdrawAnotherWalletAct;
import com.my.spendright.act.ui.budget.withdraw.WithdrawCompleteAct;
import com.my.spendright.act.ui.budget.withdraw.WithdrawYourBankAct;
import com.my.spendright.act.ui.budget.withdraw.model.UserSuggModel;
import com.my.spendright.databinding.ActivityWalletToWalletBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletToWalletAct extends AppCompatActivity {
    private String TAG = "WalletToWalletAct";
    private SessionManager sessionManager;
    ActivityWalletToWalletBinding binding;
    ArrayList<UserSuggModel.Result> arrayList;
    boolean checkVal = false;
    private String mainAmount ="",catId="",selectedUserId="",userName="",firstName="",lastName="",mobile="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_wallet_to_wallet);
        sessionManager = new SessionManager(WalletToWalletAct.this);

        initViews();
    }

    private void initViews() {

        if(getIntent()!=null){
            if(Double.parseDouble(getIntent().getStringExtra("mainBal"))< 1)  mainAmount = String.format("%.2f", Double.parseDouble(getIntent().getStringExtra("mainBal")));
            else mainAmount = Preference.doubleToStringNoDecimal(Double.parseDouble(getIntent().getStringExtra("mainBal")));

            binding.tvAmount.setText("₦" + mainAmount);
        }

        arrayList = new ArrayList<>();
        binding.imgBack.setOnClickListener(view -> finish());

        binding.RRPayNow.setOnClickListener(view -> startActivity(new Intent(this, PaymentComplete.class)));

        binding.edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkVal = true;
                if(binding.edUsername.getText().length()>=3)findUsers(binding.edUsername.getText().toString(),binding.edUsername);
                else if(binding.edUsername.getText().length()==0) binding.tvSelectedName.setVisibility(View.GONE);
            }
        });


        binding.edUsername.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) { return false; }
            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) { return false; }
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) { return false; }
            @Override
            public void onDestroyActionMode(ActionMode actionMode) { }
        });

        binding.RRPayNow.setOnClickListener(view -> {
            validation();
        });

    }



    private void validation() {

        if(binding.edUsername.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(WalletToWalletAct.this,getString(R.string.enter_username),Toast.LENGTH_LONG).show();
        else if(binding.edAmount.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(WalletToWalletAct.this,getString(R.string.please_enter_amount),Toast.LENGTH_LONG).show();
        else if(Double.parseDouble(mainAmount) <= Double.parseDouble(binding.edAmount.getText().toString()))
            Toast.makeText(WalletToWalletAct.this,getString(R.string.withraw_bal_then_then)  ,Toast.LENGTH_LONG).show();

        else {
           // transferToAnotherWallet(Double.parseDouble(binding.edAmount.getText().toString()));
            startActivity(new Intent(WalletToWalletAct.this, WalletToWalletConfirmAct.class)
                    .putExtra("userName",userName)
                    .putExtra("name",firstName + " " + lastName)
                    .putExtra("mobile",mobile)
                    .putExtra("selectedUserId",selectedUserId)
                    .putExtra("amount",binding.edAmount.getText().toString()));

        }

    }





    private void findUsers(String query,View v) {
        binding.progressBar.setVisibility(View.GONE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("search",query);
        Log.e(TAG, "User Search Request==" + requestBody.toString());
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_suggestion_user(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    Log.e(TAG, "URL = " + loginCall.request().url());
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "User Search Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        UserSuggModel userSuggModel = new Gson().fromJson(stringResponse,UserSuggModel.class);
                        arrayList.clear();
                        arrayList.addAll(userSuggModel.getResult());
                        if(checkVal) {
                            showDropDownUser(v,binding.edUsername,arrayList);
                        }

                    } else {
                        arrayList.clear();
                        //   adapter.notifyDataSetChanged();
                        Toast.makeText(WalletToWalletAct.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
    private void showDropDownUser(View v, EditText textView, List<UserSuggModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(WalletToWalletAct.this, v);

        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getUserName());
        }

        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).getUserName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    selectedUserId = stringList.get(i).getId();
                    userName = stringList.get(i).getOtherLegalName() + stringList.get(i).getLastLegalLame();
                    firstName = stringList.get(i).getFirstName() ;
                    lastName = stringList.get(i).getLastName();
                    mobile = "0"+stringList.get(i).getMobile();
                    textView.setText(menuItem.getTitle());
                    binding.tvSelectedName.setVisibility(View.VISIBLE);
                    binding.tvSelectedName.setText(stringList.get(i).getOtherLegalName() + stringList.get(i).getLastLegalLame());
                }
            }
            popupMenu.dismiss();
            checkVal = false;
            return true;
        });
        popupMenu.show();
    }

}
