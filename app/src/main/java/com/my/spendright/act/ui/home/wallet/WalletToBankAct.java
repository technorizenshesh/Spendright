package com.my.spendright.act.ui.home.wallet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.act.ui.budget.AddBeneficiaryBottomSheet;
import com.my.spendright.act.ui.budget.adapter.BeneficiaryBaseAdapter;
import com.my.spendright.act.ui.budget.listener.onGrpListener;
import com.my.spendright.act.ui.budget.model.BeneficiaryModel;
import com.my.spendright.act.ui.budget.model.BudgetGrpModel;
import com.my.spendright.act.ui.budget.model.MonnifyCommissionModel;
import com.my.spendright.act.ui.budget.withdraw.WithdrawPaymentConfirmAct;
import com.my.spendright.act.ui.budget.withdraw.WithdrawYourBankAct;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.ActivityWalletToBankBinding;
import com.my.spendright.databinding.ActivityWalletToWalletBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletToBankAct extends AppCompatActivity implements onGrpListener, CreateVirtualListener {
    ActivityWalletToBankBinding binding;
    private String TAG = "WalletToBankAct";
    ArrayList<BeneficiaryModel.Datum> beneficiaryArrayList;

    private String mainAmount="", bankCode="",amount="0.0",beneficiaryId="",beneficiaryAccount="",beneficiaryName="",beneficiaryBank="";
    SessionManager sessionManager;
    GetProfileModel finallyPr;

    Double CmAmt =0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_to_bank);
        sessionManager = new SessionManager(WalletToBankAct.this);

        initViews();
    }

    private void initViews() {

        if(getIntent()!=null){
            if(Double.parseDouble(getIntent().getStringExtra("mainBal").replace(",",""))< 1)  mainAmount = String.format("%.2f", Double.parseDouble(getIntent().getStringExtra("mainBal").replace(",","")));
            else mainAmount = Preference.doubleToStringNoDecimal(Double.parseDouble(getIntent().getStringExtra("mainBal").replace(",","")));
            binding.tvAmount.setText("₦" + mainAmount);
        }

        beneficiaryArrayList = new ArrayList<>();
        binding.imgBack.setOnClickListener(view -> finish());


        binding.btnContinue.setOnClickListener(view ->
        {         double amtt = 0.0;
                  if(!binding.edAmount.getText().toString().equalsIgnoreCase("")) amtt = CmAmt + Double.parseDouble(binding.edAmount.getText().toString());
                   else amtt =0.0;


            if(binding.edAmount.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(WalletToBankAct.this,getString(R.string.please_enter_amount),Toast.LENGTH_LONG).show();
            else if(CmAmt >= amtt)
                Toast.makeText(WalletToBankAct.this,getString(R.string.withraw_bal_more_then_fees) + " " + CmAmt + "(fees.)",Toast.LENGTH_LONG).show();
            else if(beneficiaryId.equalsIgnoreCase(""))
                Toast.makeText(WalletToBankAct.this,getString(R.string.select_beneficiary),Toast.LENGTH_LONG).show();
            else if(Double.parseDouble(mainAmount.replace(",","")) <= Double.parseDouble(binding.edAmount.getText().toString()))
                Toast.makeText(WalletToBankAct.this,getString(R.string.withraw_bal_then_then)  ,Toast.LENGTH_LONG).show();
            else {

                startActivity(new Intent(WalletToBankAct.this, WalletToBankConfirmAct.class)
                        .putExtra("beneficiaryId",beneficiaryId)
                        .putExtra("beneficiaryAccountNo",beneficiaryAccount)
                        .putExtra("beneficiaryName",beneficiaryName)
                        .putExtra("beneficiaryBank",beneficiaryBank)
                        .putExtra("amount",binding.edAmount.getText().toString())
                        .putExtra("mainBal",mainAmount+"")
                        .putExtra("ref",Preference.getAlphaNumericString(20)));
            }
        });







        binding.rlBeneficiary.setOnClickListener(view ->{
            if(beneficiaryArrayList.size()>0) {
                //showDropDownBeneficiaryList(view,binding.tvBeneficiary,beneficiaryArrayList);
                binding.imgState.setImageResource(R.drawable.arrow_black_bottom);
                binding.rvBeneficiary.setVisibility(View.VISIBLE);
            }
        } );


        binding.llBeneficiary.setOnClickListener(view -> callAddBene());

        binding.spinnerBeneficiary.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                view.findViewById(R.id.tvBankName).setVisibility(View.GONE);
                view.findViewById(R.id.tvAccountNumber).setVisibility(View.GONE);

                Log.e("======",i+"");
                if(i==0){
                    beneficiaryId = "";
                    callAddBene();
                }
                else {
                    beneficiaryId = beneficiaryArrayList.get(i).getId();
                    beneficiaryName = beneficiaryArrayList.get(i).getBankAccountName();
                    beneficiaryAccount = beneficiaryArrayList.get(i).getBankNumber();
                    beneficiaryBank = beneficiaryArrayList.get(i).getBankName();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("======1111",adapterView.getSelectedItemPosition()+"");

            }
        });







        GetProfileMethod();
        GetCommissionValue();


    }


    private void callAddBene() {
        if(finallyPr.getResult()!=null) new AddBeneficiaryBottomSheet(finallyPr.getResult().getBvnNumber()).callBack(this::onVirtual).show(getSupportFragmentManager(),"");

    }


    private void GetProfileMethod() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<GetProfileModel> call = RetrofitClients.getInstance().getApi()
                .Api_get_profile_data(sessionManager.getUserID());
        call.enqueue(new Callback<GetProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        Log.e("bvn number===", finallyPr.getResult().getBvnNumber() + "");
                        // getAllBanks();
                        getBeneficiary();
                    } else {

                        Toast.makeText(WalletToBankAct.this, "" + finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    //binding.RRadd.setVisibility (View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }




    private void getBeneficiary() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID()/*+"3"*/);
        Log.e(TAG, "get all Beneficiary account Request==" + requestBody.toString());
        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_get_beneficiary(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    Log.e(TAG, "URL = " + loginCall.request().url());

                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "get all Beneficiary account Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        BeneficiaryModel beneficiaryModel = new Gson().fromJson(stringResponse, BeneficiaryModel.class);
                        binding.spinnerBeneficiary.setVisibility(View.VISIBLE);
                        binding.llBeneficiary.setVisibility(View.GONE);
                        binding.imgState.setVisibility(View.GONE);

                        beneficiaryArrayList.clear();
                        beneficiaryArrayList.addAll(beneficiaryModel.getData());
                        //  binding.tvBeneficiary.setText(beneficiaryModel.getData().get(0).getBankNumber());
                        //  binding.rvBeneficiary.setAdapter(new BeneficiaryAdapter(WithdrawYourBankAct.this,beneficiaryArrayList,WithdrawYourBankAct.this));


                        binding.spinnerBeneficiary.setAdapter(new BeneficiaryBaseAdapter(WalletToBankAct.this,beneficiaryArrayList));


                    } else {
                        binding.spinnerBeneficiary.setVisibility(View.GONE);
                        binding.llBeneficiary.setVisibility(View.VISIBLE);
                        binding.imgState.setVisibility(View.VISIBLE);
                        // Toast.makeText(WithdrawYourBankAct.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onVirtual(String data, String tag) {
        getBeneficiary();
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
                                    if(Double.parseDouble(chkCommision[0]) <= Double.parseDouble(mainAmount.replace(",","")) && Double.parseDouble(mainAmount.replace(",","")) <= Double.parseDouble(chkCommision[1]))
                                        CommisionAmount = finallyPr.getResult().get(i).getAdminFee();

                                }
                                else {
                                    if(Double.parseDouble(finallyPr.getResult().get(i).getAmount()) <= Double.parseDouble(mainAmount)) CommisionAmount =  finallyPr.getResult().get(i).getAmount();
                                }
                            }

                            CmAmt= Double.valueOf(CommisionAmount);
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

    @Override
    public void onGrp(int position, BudgetGrpModel.Group group, String tag) {

    }
}

