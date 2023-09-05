package com.my.spendright.act;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.gson.Gson;
import com.my.spendright.Model.AccountModel;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.vAccountModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.home.wallet.TransferFundAct;
import com.my.spendright.act.ui.home.wallet.WalletToBankAct;
import com.my.spendright.adapter.AccountAdapter;
import com.my.spendright.databinding.ActivityWalletFundBinding;
import com.my.spendright.utils.ApiClient;
import com.my.spendright.utils.ApiMonnify;
import com.my.spendright.utils.Constant;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundAct extends AppCompatActivity {
    public String TAG = "FundAct";
    ActivityWalletFundBinding binding;
    private SessionManager sessionManager;
    ArrayList<AccountModel> arrayList;
    AccountAdapter adapter;
    GetProfileModel finallyPr;
    String fName = "", lName = "", email = "", amount, phoneNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_fund);
        initViews();

    }

    private void initViews() {

        if (getIntent() != null) amount = getIntent().getStringExtra("amount");
        // binding.edAmount.addTextChangedListener(new NumberTextWatcher(binding.edAmount,"#,###"));


        sessionManager = new SessionManager(FundAct.this);

        arrayList = new ArrayList<>();

        binding.imgBack.setOnClickListener(v -> finish());

        adapter = new AccountAdapter(FundAct.this, arrayList);
        binding.rvFund.setAdapter(adapter);

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            // getFLAccount();

        } else {
            Toast.makeText(FundAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        binding.RRKYC.setOnClickListener(view -> {
            if (finallyPr != null) {
                if (finallyPr.getResult().getKycStatus().equalsIgnoreCase("0")) {
                    startActivity(new Intent(this, KYCAct.class)
                            .putExtra("user_id", finallyPr.getResult().getId())
                            .putExtra("mobile", finallyPr.getResult().getMobile())
                            .putExtra("name", finallyPr.getResult().getLastName() + finallyPr.getResult().getOtherLegalName())
                            .putExtra("from", "FundScreen"));
                }

            }
        });

        binding.RRGenerateAcct.setOnClickListener(view -> {
            if (sessionManager.isNetworkAvailable()) generateMAccount();
            else Toast.makeText(FundAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        });


        binding.RRPay.setOnClickListener(v -> {
            if (binding.edAmount.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
            } else {


// 08060210309
                new RaveUiManager(FundAct.this).setAmount(Double.parseDouble(binding.edAmount.getText().toString().trim()))
                        .setCurrency("NGN")
                        .setEmail(email)
                        .setfName(fName)
                        .setlName(lName)
                        .setNarration(fName + " " + lName)
                        .setPublicKey(Constant.FL_LIVE_PUBLIC_KEY)
                        .setEncryptionKey(Constant.FL_LIVE_ENCRYPTION_KEY)
                        .setTxRef("reference" + getCurrentDate())
                        .setPhoneNumber(phoneNumber, true)
                        .setCountry("NG")
                        .acceptAccountPayments(true)
                        .acceptCardPayments(true)
                        .acceptMpesaPayments(true)
                        .acceptAchPayments(true)
                        // .acceptGHMobileMoneyPayments(true)
                        //  .acceptUgMobileMoneyPayments(true)
                        //  .acceptZmMobileMoneyPayments(true)
                        // .acceptRwfMobileMoneyPayments(true)
                        .acceptSaBankPayments(true)
                        .acceptUkPayments(true)
                        .acceptBankTransferPayments(true)
                        .acceptUssdPayments(true)
                        .acceptBarterPayments(true)
                        .allowSaveCardFeature(true)
                        .onStagingEnv(false)
                        .isPreAuth(true)
                        .shouldDisplayFee(true)
                        .showStagingLabel(true)
                        .allowSaveCardFeature(true)
                        .initialize();


            }

            // startActivity(new Intent(this, TransferFundAct.class));

        });

       /* binding.btnDone.setOnClickListener(view -> {
            startActivity(new Intent(FundAct.this,HomeActivity.class )
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Inte nt.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });*/


        binding.ivCopy1.setOnClickListener(view ->setClipboard(this,binding.tvAccountNo1.getText().toString().trim()));

        binding.ivCopy2.setOnClickListener(view ->setClipboard(this,binding.tvAccountNo2.getText().toString().trim()));

        binding.ivCopy3.setOnClickListener(view ->setClipboard(this,binding.tvAccountNo3.getText().toString().trim()));


        GetProfileMethod();

    }


    @Override
    protected void onResume() {
        super.onResume();
        GetProfileMethod();
    }

/*
    private void getFLAccount() {
        ApiMonnify apiInterface = ApiClient.getClient1().create(ApiMonnify.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + Constant.FL_LIVE_SECRET_KEY);
        Call<ResponseBody> loginCall = apiInterface.Api_get_account(headers, sessionManager.getAccountReference());
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "FLGetAccount Response = " + stringResponse);
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            //  JSONArray array = jsonObject.getJSONArray("data");
                            //  binding.llAccount.setVisibility(View.VISIBLE);
                            JSONObject object = jsonObject.getJSONObject("data");
                            Log.e("obj====", object.getString("account_number"));
                            Log.e("obj====", object.getString("bank_name"));
                            Log.e("obj====", object.getString("order_ref"));
                            Log.e("obj====", object.getString("amount"));

                            // binding.tvAccountNo.setText(object.getString("account_number"));
                            //   binding.tvBankName.setText(object.getString("bank_name"));
                        } else {
                            // arrayList.clear();
                            //   adapter.notifyDataSetChanged();
                            //  binding.llAccount.setVisibility(View.GONE);
                            Toast.makeText(FundAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "FlutterwaveAccount Response = " + stringResponse);
                        //  binding.llAccount.setVisibility(View.GONE);
                        Toast.makeText(FundAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
*/


    public String toBase64(String message) {
        byte[] data;
        // data = message.getBytes("UTF-8");
        String base64Sms = Base64.encodeToString(message.getBytes(), Base64.NO_WRAP);
        // Base64.encodeToString(data, Base64.NO_WRAP);
        return base64Sms;

        // return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");

            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                //  Toast.makeText(FundAct.this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
                Log.e("Payment response===", message);
                UpdateWalletAccount(message);
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                //  Toast.makeText(FundAct.this, "ERROR " + message, Toast.LENGTH_SHORT).show();
                Log.e("Payment response===", message);
                Toast.makeText(FundAct.this, "Payment failed error " + message, Toast.LENGTH_SHORT).show();

            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                //    Toast.makeText(FundAct.this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
                Toast.makeText(FundAct.this, "Payment Cancelled error ", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public static String getCurrentTime123() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        //   String datesplite[] = currentDateandTime.split(" ");
        //   Log.e("time====",datesplite[2]);
        // String time[] = datesplite[1].split(":");
        return currentDateandTime;
    }

    private void GetProfileMethod() {
        Call<GetProfileModel> call = RetrofitClients.getInstance().getApi()
                .Api_get_profile_data(sessionManager.getUserID());
        call.enqueue(new Callback<GetProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    finallyPr = response.body();
                    getAccount();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        fName = finallyPr.getResult().getFirstName();
                        lName = finallyPr.getResult().getLastName();
                        email = finallyPr.getResult().getEmail();
                        phoneNumber = "234" + finallyPr.getResult().getMobile();
                    } else {

                        Toast.makeText(FundAct.this, "" + finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
//                binding.recyclermyAccount.setVisibility(View.GONE);
            }
        });

    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context, getString(R.string.click_copy), Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, getString(R.string.click_copy), Toast.LENGTH_SHORT).show();
        }
    }



    private void UpdateWalletAccount(String js) {
        Map<String, String> map = new HashMap<>();
        map.put("user_payment_type", "add_payment_to_wallet_by_card");
        map.put("user_id", sessionManager.getUserID());
        map.put("json_response", js);
        Log.e(TAG, "Send Wallet Request  = " + map);

        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_updateWallet(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "Send Wallet Request Response = " + stringResponse);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                            startActivity(new Intent(FundAct.this, PaymentComplete.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();

                        } else {
                            Toast.makeText(FundAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //  startActivity(new Intent(FundAct.this,PaymentComplete.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            //   finish();
                        }
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


    public String getCurrentDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String todayString = formatter.format(todayDate);
        return todayString;

    }


    private void  getAccount() {
        binding.progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", sessionManager.getUserID());
        Log.e(TAG, "get account Request  = " + map);
        Call<ResponseBody> call = RetrofitClientsOne.getInstance().getApi().Api_get_account(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "get account Response = " + stringResponse);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            vAccountModel model = new Gson().fromJson(stringResponse, vAccountModel.class);
                            if (model.getResult().getBank().size() > 0) {
                                for (int i = 0; i < model.getResult().getBank().size(); i++) {
                                    if (model.getResult().getBank().get(i).getType().equalsIgnoreCase("monnify") && !model.getResult().getBank().get(i).getAccountNumber().equalsIgnoreCase("")) {
                                        if (i == 0) {
                                            binding.llOne.setVisibility(View.VISIBLE);
                                            binding.ll21.setVisibility(View.VISIBLE);
                                            binding.RRGenerateAcct.setVisibility(View.GONE);
                                            binding.tvAccountNo1.setText(model.getResult().getBank().get(i).getAccountNumber());
                                            binding.tvbankName1.setText(model.getResult().getBank().get(i).getBankName());
                                        } else {
                                            binding.llTwo.setVisibility(View.VISIBLE);
                                            binding.RRGenerateAcct.setVisibility(View.GONE);
                                            binding.tvAccountNo2.setText(model.getResult().getBank().get(i).getAccountNumber());
                                            binding.tvbankName2.setText(model.getResult().getBank().get(i).getBankName());
                                        }
                                    } else if (model.getResult().getBank().get(i).getType().equalsIgnoreCase("flutterwave")) {
                                        if (!model.getResult().getBank().get(i).getAccountNumber().equalsIgnoreCase("") && finallyPr.getResult().getKycStatus().equalsIgnoreCase("1")) {
                                            binding.rlFlutterWave.setVisibility(View.VISIBLE);
                                            binding.llThree.setVisibility(View.VISIBLE);
                                            binding.v1.setVisibility(View.VISIBLE);
                                            binding.ll41.setVisibility(View.VISIBLE);
                                            binding.RRKYC.setVisibility(View.GONE);
                                            binding.tvAccountNo3.setText(model.getResult().getBank().get(i).getAccountNumber());
                                            binding.tvbankName3.setText(model.getResult().getBank().get(i).getBankName());
                                        } else if (model.getResult().getBank().get(i).getAccountNumber().equalsIgnoreCase("") && finallyPr.getResult().getKycStatus().equalsIgnoreCase("0")) {
                                            binding.rlFlutterWave.setVisibility(View.VISIBLE);
                                            binding.llThree.setVisibility(View.GONE);
                                            binding.v1.setVisibility(View.GONE);
                                            binding.ll41.setVisibility(View.GONE);
                                            binding.RRKYC.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        binding.llOne.setVisibility(View.GONE);
                                        binding.ll21.setVisibility(View.GONE);
                                        binding.llTwo.setVisibility(View.GONE);
                                        binding.RRGenerateAcct.setVisibility(View.VISIBLE);

                                    }
                                }
                            }


                        } else {
                            Toast.makeText(FundAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            //  startActivity(new Intent(FundAct.this,PaymentComplete.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            //   finish();
                        }
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


    private void generateMAccount() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());

        Log.e(TAG, "Generate MAccount Request==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_for_exiting_user(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Generate MAccount Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        getAccount();
                    } else {
                        Toast.makeText(FundAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
