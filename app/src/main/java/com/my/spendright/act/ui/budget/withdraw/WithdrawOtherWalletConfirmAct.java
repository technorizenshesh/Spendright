package com.my.spendright.act.ui.budget.withdraw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.ConfirmPaymentTvAct;
import com.my.spendright.act.ui.budget.model.MonnifyCommissionModel;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.databinding.ActivityWithdrawPaymentConfirmBinding;
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

public class WithdrawOtherWalletConfirmAct extends AppCompatActivity {
    private String TAG = "WithdrawMyWalletConfirmAct";
    private SessionManager sessionManager;
    String catId="",ref="",selectBugCategoryId = "",userName ="",name="",mobile="",selectedUserId="",amount="",BudgetAccountId="";

    ActivityWithdrawPaymentConfirmBinding binding;

    private ArrayList<GetCategoryModelNew.Result> modelListCategory;
    ArrayList<IncomeExpenseCatModel.Category> arrayList;
    IncomeExpenseCatModel incomeExpenseCatModel;
    boolean chkPayStatus = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw_payment_confirm);
        sessionManager = new SessionManager(WithdrawOtherWalletConfirmAct.this);
        initViews();
    }



    private void initViews() {
        modelListCategory = new ArrayList<>();
        arrayList =new ArrayList<>();
        if(getIntent()!=null){
            catId = getIntent().getStringExtra("catId");
            userName = getIntent().getStringExtra("userName");
            name = getIntent().getStringExtra("name");
            mobile = getIntent().getStringExtra("mobile");
            selectedUserId = getIntent().getStringExtra("selectedUserId");
            amount = getIntent().getStringExtra("amount");
            ref = getIntent().getStringExtra("ref");

            binding.tvBeneficiaryName.setText(name);
            binding.tvBeneficiaryUserName.setText(userName);
            binding.tvBeneficiaryPhone.setText(mobile);
            //  binding..setText(beneficiaryAccountNo);

            binding.AmountPay.setText("₦"+ Preference.doubleToStringNoDecimal(Double.parseDouble(amount))+"");
            binding.totalAmountPay.setText("₦"+ Preference.doubleToStringNoDecimal(Double.parseDouble(amount+""))+"");

        }

        binding.rlCategory.setOnClickListener(view -> {
            if(arrayList.size()>0) showDropDownCategory(view,binding.tvCategoryName,arrayList);

        });


        binding.imgBack.setOnClickListener(v -> {
            finish();
        });

        binding.txtCancel.setOnClickListener(v -> {
            finish();
        });


        binding.RRConfirm.setOnClickListener(v -> {
            if(selectBugCategoryId.equalsIgnoreCase("")){
                Toast.makeText(this, "Please go to setting tab and add an expense category", Toast.LENGTH_SHORT).show();
            }
          else{
              if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    double t=0.0;
               /* if(!binding.tax.getText().toString().equalsIgnoreCase("0.00"))
                {
                    t = Double.parseDouble(binding.tax.getText().toString()) + Double.parseDouble(amount);
                }
                else*/ t =  Double.parseDouble(amount);

                      binding.RRConfirm.setClickable(false);
                      binding.RRConfirm.setFocusable(false);
                      binding.RRConfirm.setEnabled(false);
                  binding.RRConfirm.setBackground(getDrawable(R.drawable.btn_inactive_bg));
                  binding.txtCancel.setVisibility(View.GONE);
                  binding.imgBack.setEnabled(false);
                  binding.imgBack.setClickable(false);
                  chkPayStatus = false;
                  transferToAnotherWallet(t);

                }else {
                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.spinnerBudgetAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                BudgetAccountId = modelListCategory.get(pos).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetAccountBudgetMethod();
            // GetCommissionValue();
            getAllBudgetCategories();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }




    private void GetCommissionValue(){
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_monnify_commission();
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);

                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "Electricity commission  Response = " + stringResponse);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            MonnifyCommissionModel finallyPr = new Gson().fromJson(stringResponse, MonnifyCommissionModel.class);

                            String CommisionAmount ="0";
                            for (int i=0;i<finallyPr.getResult().size();i++){
                                String chkCommision[];
                                if(finallyPr.getResult().get(i).getAmount().contains("-")){
                                    chkCommision =  finallyPr.getResult().get(i).getAmount().split("-");
                                    Log.e("commission==1",chkCommision[0]);
                                    Log.e("commission==2",chkCommision[1]);
                                    if(Double.parseDouble(chkCommision[0]) <= Double.parseDouble(amount) && Double.parseDouble(amount) <= Double.parseDouble(chkCommision[1]))
                                        CommisionAmount = finallyPr.getResult().get(i).getAdminFee();

                                }
                                else {
                                    if(Double.parseDouble(finallyPr.getResult().get(i).getAmount()) <= Double.parseDouble(amount)) CommisionAmount =  finallyPr.getResult().get(i).getAmount();
                                }
                            }

                            Double CmAmt= Double.valueOf(CommisionAmount);
                            Double TotalAmt= (Double.valueOf(amount) - CmAmt );
                            amount = String.valueOf(TotalAmt);
                            Double FInalAmt=CmAmt+TotalAmt;


                            binding.AmountPay.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(amount)));


                            binding.tax.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(CommisionAmount))+"");
                            binding.totalAmountPay.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(FInalAmt+""))+"");
                        }

                        else {

                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void GetAccountBudgetMethod()
    {
        Call<GetCategoryModelNew> call = RetrofitClients.getInstance().getApi()
                .Api_get_account_detail(sessionManager.getUserID());
        call.enqueue(new Callback<GetCategoryModelNew>() {
            @Override
            public void onResponse(Call<GetCategoryModelNew> call, Response<GetCategoryModelNew> response) {

                binding.progressBar.setVisibility(View.GONE);

                GetCategoryModelNew finallyPr = response.body();

                if (finallyPr.getStatus().equalsIgnoreCase("1"))
                {
                    modelListCategory = (ArrayList<GetCategoryModelNew.Result>) finallyPr.getResult();

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(WithdrawOtherWalletConfirmAct.this,modelListCategory);
                    binding.spinnerBudgetAct.setAdapter(customAdapter);

                }else {

                    binding.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<GetCategoryModelNew> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void transferToAnotherWallet(Double transferAmt) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());
        requestBody.put("bcat_id",catId);
        requestBody.put("amount",transferAmt+"");
        requestBody.put("other_user_id",selectedUserId);
        requestBody.put("expense_traking_account_id",BudgetAccountId);
        requestBody.put("expense_traking_category_id",selectBugCategoryId);
        requestBody.put("reff_id",ref);

        Log.e(TAG, "Transfer to Another Wallet Request==" + requestBody.toString());
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_withdraw_my_wallet_to_another_wallet(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    Log.e(TAG, "URL = " + loginCall.request().url());
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Transfer to Another Wallet Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        startActivity(new Intent(WithdrawOtherWalletConfirmAct.this,WithdrawCompleteAct.class));
                        finish();

                    } else {
                        binding.RRConfirm.setClickable(true);
                        binding.RRConfirm.setFocusable(true);
                        binding.RRConfirm.setEnabled(true);
                        binding.RRConfirm.setBackground(getDrawable(R.drawable.border_btn));
                        binding.txtCancel.setVisibility(View.VISIBLE);
                        binding.imgBack.setEnabled(true);
                        binding.imgBack.setClickable(true);
                        chkPayStatus = true;
                        Toast.makeText(WithdrawOtherWalletConfirmAct.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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





    private void getAllBudgetCategories() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cat_user_id", sessionManager.getUserID());
        requestBody.put("cat_type","EXPENSE");
        Log.e(TAG, "getAll category BudgetRequest==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_get_budget_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "getAll category BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        incomeExpenseCatModel = new Gson().fromJson(stringResponse, IncomeExpenseCatModel.class);
                        arrayList.clear();
                        arrayList.addAll(incomeExpenseCatModel.getCategories());
                        selectBugCategoryId = incomeExpenseCatModel.getCategories().get(0).getCatId();
                        binding.tvCategoryName.setText(incomeExpenseCatModel.getCategories().get(0).getCatName());

                    } else {
                        arrayList.clear();
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
    private void showDropDownCategory(View v, TextView textView, List<IncomeExpenseCatModel.Category> stringList) {
        PopupMenu popupMenu = new PopupMenu(WithdrawOtherWalletConfirmAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getCatName());
        }

        // popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).getCatName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    selectBugCategoryId = stringList.get(i).getCatId();
                    textView.setText(menuItem.getTitle());

                }
            }


            return true;
        });
        popupMenu.show();
    }

    @Override
    public void onBackPressed() {
      if(chkPayStatus == true)  super.onBackPressed();
    }
}