package com.my.spendright.act.ui.budget.withdraw;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.budget.withdraw.adapter.UserArrayAdapter;
import com.my.spendright.act.ui.budget.withdraw.model.UserSuggModel;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.ActivityWithdrawAnotherWalletBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
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

public class WithdrawAnotherWalletAct extends AppCompatActivity {
    private String TAG = "WithdrawAnotherWalletAct";
    private SessionManager sessionManager;
    ActivityWithdrawAnotherWalletBinding binding;
    private String amount ="",catId="",selectedUserId="",userName="",firstName="",lastName="",mobile="";

    UserArrayAdapter adapter;
    ArrayList<UserSuggModel.Result>arrayList;
    boolean checkVal = false;
    GetProfileModel finallyPr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_withdraw_another_wallet);
        sessionManager = new SessionManager(WithdrawAnotherWalletAct.this);

        initView();
    }

    private void initView() {

        arrayList = new ArrayList<>();




        if(getIntent()!=null) {
            if(Double.parseDouble(getIntent().getStringExtra("amount"))< 1){
                amount = String.format("%.2f", Double.parseDouble(getIntent().getStringExtra("amount")));
            }
           else {
               amount = Preference.doubleToStringNoDecimal(Double.parseDouble(getIntent().getStringExtra("amount")));
            }

               binding.tvAmount.setText("₦"+ amount);
            catId = getIntent().getStringExtra("catId");
        }

     //   adapter = new UserArrayAdapter(WithdrawAnotherWalletAct.this, R.layout.simple_text_view, arrayList);
      //  binding.edUsername.setAdapter(adapter);
     //   binding.edUsername.setThreshold(1);






        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnContinue.setOnClickListener(view -> validation());


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


    }





    private void validation() {

        if(binding.edUsername.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(WithdrawAnotherWalletAct.this,getString(R.string.enter_username),Toast.LENGTH_LONG).show();
        else if(binding.edAmount.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(WithdrawAnotherWalletAct.this,getString(R.string.please_enter_amount),Toast.LENGTH_LONG).show();
        else if(Double.parseDouble(amount) < Double.parseDouble(binding.edAmount.getText().toString()))
            Toast.makeText(WithdrawAnotherWalletAct.this,getString(R.string.amount_must_be_lessthen_from_available_amount),Toast.LENGTH_LONG).show();
        else {


            startActivity(new Intent(WithdrawAnotherWalletAct.this,WithdrawOtherWalletConfirmAct.class)
                    .putExtra("catId",catId)
                    .putExtra("userName",userName)
                    .putExtra("name",firstName + " " + lastName)
                    .putExtra("mobile",mobile)
                    .putExtra("selectedUserId",selectedUserId)
                    .putExtra("amount",Double.parseDouble(binding.edAmount.getText().toString())+""));
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
                        Toast.makeText(WithdrawAnotherWalletAct.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
        PopupMenu popupMenu = new PopupMenu(WithdrawAnotherWalletAct.this, v);

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
                    binding.tvSelectedName.setText(userName);
                }
            }
            popupMenu.dismiss();
            checkVal = false;
            return true;
        });
        popupMenu.show();
    }






}
