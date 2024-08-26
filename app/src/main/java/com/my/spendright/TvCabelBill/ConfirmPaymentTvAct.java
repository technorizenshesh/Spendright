package com.my.spendright.TvCabelBill;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Broadband.ConfirmPaymentBroadBandAct;
import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetCommisionModel;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.ConfirmPaymentAct;
import com.my.spendright.act.FundAct;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.databinding.ActivityConfirmPaymentTvBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPaymentTvAct extends AppCompatActivity {
    public String TAG ="ConfirmPaymentTvAct";
    ActivityConfirmPaymentTvBinding binding;

    String Request_IDNew="";
    String ServicesId="";
    String ServicesName="";
    String billersCode="";
    String amount="";
    String phone="";
    String subscription_type="";
    String MyCuurentBlance="0.0";
    GetProfileModel finallyPr;
    int discountPercent =0;
    double discountAmount =0.0;

    double walletAmount ;
    private SessionManager sessionManager;

    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();
    String BudgetAccountId="",selectBugCategoryId="";
    ArrayList<IncomeExpenseCatModel.Category> arrayList = new ArrayList<>();;
    IncomeExpenseCatModel incomeExpenseCatModel;
    double FInalAmt = 0.0;
    boolean chkPayStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_payment_tv);

        sessionManager = new SessionManager(ConfirmPaymentTvAct.this);

        Log.d("Request_ID:>>",get_current_Time());

        Request_IDNew=get_current_Time();

        Intent intent=getIntent();

        if(intent !=null)
        {

            ServicesId =intent.getStringExtra("ServicesId");
            ServicesName =intent.getStringExtra("ServicesName");
            billersCode =intent.getStringExtra("billersCode");
            amount =intent.getStringExtra("amount");
            phone =intent.getStringExtra("phone");
            subscription_type =intent.getStringExtra("subscription_type");
            MyCuurentBlance =intent.getStringExtra("MyCuurentBlance");

            if(MyCuurentBlance==null || MyCuurentBlance.equalsIgnoreCase("")) MyCuurentBlance = "0.0";

             binding.meterNumber.setText(billersCode);
             binding.MyCuurentBlance.setText( "₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(MyCuurentBlance.replace(",",""))));
             binding.type.setText(subscription_type);
             binding.AmountPay.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(amount.replace(",",""))));
             binding.totalAmountPay.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(amount.replace(",",""))));
        }
        if(amount.contains(",")) amount = amount.replace(",","");
        Log.e("change value====",amount);


        binding.imgBack.setOnClickListener(v -> {
            finish();
        });

        binding.txtCancel.setOnClickListener(v -> {
           finish();
        });

        binding.edtDescription.setOnClickListener(v -> {
            if (arrayList.size()>0)showDropDownCategory(v,binding.edtDescription,arrayList);
        });


        binding.RRConfirm.setOnClickListener(v -> {
            if (selectBugCategoryId.equalsIgnoreCase("")) {
                Toast.makeText(this, "Please go to setting tab and add an expense category", Toast.LENGTH_SHORT).show();
            } else {
                if (sessionManager.isNetworkAvailable()) {

                    double t = 0.0;
                    if (!binding.tax.getText().toString().equalsIgnoreCase("₦0.00")) {
                        t = FInalAmt;  // Double.parseDouble(binding.tax.getText().toString().replace("₦","")) + Double.parseDouble(amount);
                    } else t = Double.parseDouble(amount);

                    if (walletAmount >= t) {
                        binding.RRConfirm.setClickable(false);
                        binding.RRConfirm.setFocusable(false);
                        binding.RRConfirm.setEnabled(false);
                        binding.RRConfirm.setBackground(getDrawable(R.drawable.btn_inactive_bg));
                        binding.txtCancel.setVisibility(View.GONE);
                        binding.imgBack.setEnabled(false);
                        binding.imgBack.setClickable(false);
                        chkPayStatus = false;
                        PyaAccoun();
                    }
                    else {
                        AlertDialogStatus(getString(R.string.your_wallet_bal_is_low));
                    }
                } else {
                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }

        });


        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetAccountBudgetMethod();
            GetCommisionValue();
            getAllBudgetCategories();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        binding.spinnerBudgetAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                BudgetAccountId = modelListCategory.get(pos).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void PyaAccoun() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_pay_tv(Preference.getHeader(ConfirmPaymentTvAct.this),sessionManager.getUserID(),Request_IDNew,ServicesId,billersCode,amount,
                phone,subscription_type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                 try {

                     String stringResponse = response.body().string();
                     JSONObject jsonObject = new JSONObject(stringResponse);
                     Log.e("Payment", "Payment Response :" + stringResponse);
                     if(jsonObject.getString("code").equals("000"))
                     {
                         // PayFinalModel finallyPr =  new Gson().fromJson(stringResponse,PayFinalModel.class); // response.body();
                         binding.progressBar.setVisibility(View.VISIBLE);
                         AddReportMethod(jsonObject.getString("response_description"),stringResponse);

                         Toast.makeText(ConfirmPaymentTvAct.this, "SuccessFully Bill pay", Toast.LENGTH_SHORT).show();

                     }


                     else if (jsonObject.has("status")) {

                         if (jsonObject.getString("status").equalsIgnoreCase("9")) {

                             sessionManager.logoutUser();
                             Toast.makeText(ConfirmPaymentTvAct.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(ConfirmPaymentTvAct.this, LoginActivity.class)
                                     .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                             finish();
                         }
                     }




                     else
                     {
                         binding.RRConfirm.setClickable(true);
                         binding.RRConfirm.setFocusable(true);
                         binding.RRConfirm.setEnabled(true);
                         binding.RRConfirm.setBackground(getDrawable(R.drawable.border_btn));
                         binding.txtCancel.setVisibility(View.VISIBLE);
                         binding.imgBack.setEnabled(true);
                         binding.imgBack.setClickable(true);
                         chkPayStatus = true;
                         Toast.makeText(ConfirmPaymentTvAct.this, jsonObject.getString("response_description"), Toast.LENGTH_SHORT).show();
                     }

                 }catch (Exception e){
                     e.printStackTrace();
                 }
                 }
                else {

                    Toast.makeText(ConfirmPaymentTvAct.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void AddReportMethod(String status,String response){
       String Current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_add_vtpass_book_payment(Preference.getHeader(ConfirmPaymentTvAct.this),sessionManager.getUserID(),Request_IDNew,amount,ServicesId,ServicesName,
                        "Tv Subscription",status,Current_date,BudgetAccountId,selectBugCategoryId,billersCode/*binding.edtDescription.getText().toString()*/,phone,binding.tax.getText().toString().replace("₦",""),response,String.valueOf(discountPercent),"","");
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                   String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e("Report", "Report Response :" + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        // AddReportModal finallyPr = response.body();
                        sessionManager.saveTransId(jsonObject.getString("transaction_id"));
                        startActivity(new Intent(ConfirmPaymentTvAct.this,PaymentComplete.class));
                        finish();

                    }

                    else if (jsonObject.has("status")) {

                        if (jsonObject.getString("status").equalsIgnoreCase("9")) {

                            sessionManager.logoutUser();
                            Toast.makeText(ConfirmPaymentTvAct.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConfirmPaymentTvAct.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    }





                    else
                    {
                        binding.imgBack.setEnabled(true);
                        binding.imgBack.setClickable(true);
                        chkPayStatus = true;
                        Toast.makeText(ConfirmPaymentTvAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


    public String get_current_Time() {
        TimeZone tz = TimeZone.getTimeZone("GMT+1");
        Calendar c = Calendar.getInstance(tz);
        String YEar = String.valueOf(c.get(Calendar.YEAR));
        int month =c.get(Calendar.MONTH);
        int finalNew=month+1;
        String monthFinal = String.valueOf(finalNew);
        String Day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));
        String SECOND = String.valueOf(c.get(Calendar.SECOND));

        if(monthFinal.length()==1)
        {
            monthFinal="0"+monthFinal;
        }

        if(mHour.length()==1)
        {
            mHour="0"+mHour;
        }

        if(Day.length()==1)
        {
            Day="0"+Day;
        }

        if(mMinute.length()==1)
        {
            mMinute="0"+mMinute;
        }

        return YEar+monthFinal+Day+mHour+mMinute+SECOND;

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

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(ConfirmPaymentTvAct.this,modelListCategory);
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


    private void GetCommisionValue(){
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .getPaymentCommission(sessionManager.getCatId(),ServicesId);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "TV Subscription commission  Response = " + stringResponse);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            GetCommisionModel finallyPr = new Gson().fromJson(stringResponse,GetCommisionModel.class);
                            String CommisionAmount = finallyPr.getResult().getCommisionAmount();
                            discountPercent = Integer.parseInt(finallyPr.getResult().getDiscount());

                            Double CmAmt= Double.valueOf(CommisionAmount);
                            Double TotalAmt= Double.valueOf(amount);
                            FInalAmt  = CmAmt+TotalAmt;
                            FInalAmt = FInalAmt - discountAmount;

                          //  binding.tax.setText(CommisionAmount+"");
                         //   binding.totalAmountPay.setText(FInalAmt+"");
                            binding.tvDiscountPercent.setText("Discount (" + discountPercent + "%)" );
                            if(discountAmount<10)  {
                                discountAmount = Double.parseDouble("0"+Preference.doubleToStringNoDecimal(discountAmount));
                                binding.tvDiscountAmount.setText(""+"₦"+"0"+Preference.doubleToStringNoDecimal(discountAmount)+"");
                            }
                            else  binding.tvDiscountAmount.setText(""+"₦"+Preference.doubleToStringNoDecimal(discountAmount)+"");

                            if(Double.parseDouble(CommisionAmount)<10)  {
                                CommisionAmount = "0" + Preference.doubleToStringNoDecimal(Double.parseDouble(CommisionAmount));
                                binding.tax.setText("₦"+ "0"+Preference.doubleToStringNoDecimal(Double.parseDouble(CommisionAmount))+"");
                            }
                            else binding.tax.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(CommisionAmount))+"");

                            if(FInalAmt<10){
                                FInalAmt = Double.parseDouble("0"+Preference.doubleToStringNoDecimal(FInalAmt));
                                binding.totalAmountPay.setText("₦"+ "0"+FInalAmt+"");
                            }
                            else binding.totalAmountPay.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(FInalAmt+""))+"");
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


    private void GetProfileMethod() {
        Call<GetProfileModel> call = RetrofitClients.getInstance ().getApi ()
                .Api_get_profile_data (sessionManager.getUserID ());
        call.enqueue (new Callback<GetProfileModel> () {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility (View.GONE);
                try {
                    finallyPr = response.body ();
                    if (finallyPr.getStatus ().equalsIgnoreCase ("1")) {
                        walletAmount =   Double.parseDouble(finallyPr.getResult().getPaymentWalletOriginal());
                    } else {
                        Toast.makeText (ConfirmPaymentTvAct.this, "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
                        binding.progressBar.setVisibility (View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility (View.GONE);
            }
        });

    }

    public void AlertDialogStatus(String msg){

        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ConfirmPaymentTvAct.this);
        builder1.setMessage(msg);
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                getString(R.string.go),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(ConfirmPaymentTvAct.this, FundAct.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        GetProfileMethod();
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
                        binding.edtDescription.setText(incomeExpenseCatModel.getCategories().get(0).getCatName());

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
        PopupMenu popupMenu = new PopupMenu(ConfirmPaymentTvAct.this, v);
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
       if(chkPayStatus == true) super.onBackPressed();
    }
}