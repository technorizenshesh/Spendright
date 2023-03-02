package com.my.spendright.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetCommisionModel;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.databinding.ActivityConfirmPaymentNewBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPaymentAct extends AppCompatActivity {
    public String TAG = "ConfirmPaymentAct";
    ActivityConfirmPaymentNewBinding binding;

    String Request_IDNew="";
    String Request_ID="";
    String ServicesId="";
    String ServicesName="";
    String billersCode="";
    String variation_code="";
    String amount="";
    String phone="";
    String paymentdate="";
    String MyCuurentBlance="";
    private SessionManager sessionManager;
    GetProfileModel finallyPr;
    double walletAmount ;
    String Current_date="";
    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();
     String BudgetAccountId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_payment_new);

        sessionManager = new SessionManager(ConfirmPaymentAct.this);

        Log.d("Request_ID:>>",get_current_Time());

        Request_IDNew=get_current_Time();

        paymentdate= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        Intent intent=getIntent();

        if(intent !=null)
        {
             Request_ID =intent.getStringExtra("Request_ID");
             ServicesId =intent.getStringExtra("ServicesId");
             billersCode =intent.getStringExtra("billersCode");
             variation_code =intent.getStringExtra("variation_code");
             amount =intent.getStringExtra("amount");
             phone =intent.getStringExtra("billersCode");
            // paymentdate =intent.getStringExtra("paymentdate");
             MyCuurentBlance =intent.getStringExtra("MyCuurentBlance");

             binding.meterNumber.setText(billersCode);
             binding.MyCuurentBlance.setText(MyCuurentBlance);
             binding.Date.setText(paymentdate);
             binding.AmountPay.setText(amount);
             binding.totalAmountPay.setText(amount);
        }

        if(amount.contains(",")) amount = amount.replace(",","");
        Log.e("change value====",amount);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtCancel.setOnClickListener(v -> {
           finish();
        });

        binding.RRConfirm.setOnClickListener(v -> {
            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);

                String UserName= Preference.get(this,Preference.KEY_VTPASS_UserName);
                String UserPassword= Preference.get(this,Preference.KEY_VTPASS_pass);
                double t=0.0;
                if(!binding.tax.getText().toString().equalsIgnoreCase("0.00"))
                {
                    t = Double.parseDouble(binding.tax.getText().toString()) + Double.parseDouble(amount);
                }
                else t =  Double.parseDouble(amount);

                if(walletAmount >= t ) PyaAccoun(UserName,UserPassword);
                else {
                    AlertDialogStatus(getString(R.string.your_wallet_bal_is_low));
                }              //  PyaAccoun("harshit.ixora89@gmail.com","harshit89@");

            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetAccountBudgetMethod();
            GetCommisionValue();
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

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(ConfirmPaymentAct.this,modelListCategory);
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

   /* request_id:20220506100925
    serviceID:ikeja-electric
    billersCode:1111111111111`
    variation_code:prepaid
    amount:852
    phone:1111111111111*/

    private void PyaAccoun(final String username, final String password) {

        Map<String,String> map = new HashMap<>();
        map.put("request_id",Request_IDNew);
        map.put("serviceID",ServicesId);
        map.put("billersCode",billersCode);
        map.put("variation_code",variation_code);
        map.put("amount",amount);
        map.put("phone",phone);

        Log.e("ElectricityBillRequest=",map.toString());
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_pay(Request_IDNew,ServicesId,billersCode,variation_code,amount,phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        // user object available
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e("Payment", "Payment Response :" + stringResponse);
                        if(jsonObject.getString("code").equals("000"))
                        {
                           // PayFinalModel finallyPr =  new Gson().fromJson(stringResponse,PayFinalModel.class); // response.body();
                            binding.progressBar.setVisibility(View.VISIBLE);
                            AddReportMethod(jsonObject.getString("response_description"),stringResponse);

                            Toast.makeText(ConfirmPaymentAct.this, "SuccessFully Bill pay", Toast.LENGTH_SHORT).show();

                        }else
                        {
                            Toast.makeText(ConfirmPaymentAct.this, jsonObject.getString("response_description"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ConfirmPaymentAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ConfirmPaymentAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void AddReportMethod(String status,String response){
        Current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //sessionManager.getUserID();
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_add_vtpass_book_payment11(sessionManager.getUserID(),Request_IDNew,amount,ServicesId,ServicesName,
                        "Electricity",status,Current_date,BudgetAccountId,"",billersCode/*binding.edtDescription.getText().toString()*/,phone,binding.tax.getText().toString(),response);
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
                        startActivity(new Intent(ConfirmPaymentAct.this,PaymentComplete.class));
                        finish();

                    } else
                    {
                        Toast.makeText(ConfirmPaymentAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Log.e(TAG, "Electricity commission  Response = " + stringResponse);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            GetCommisionModel finallyPr = new Gson().fromJson(stringResponse,GetCommisionModel.class);
                            String CommisionAmount = finallyPr.getResult().getCommisionAmount();

                            Double CmAmt= Double.valueOf(CommisionAmount);
                            Double TotalAmt= Double.valueOf(amount);

                            Double FInalAmt=CmAmt+TotalAmt;


                            binding.tax.setText(Preference.doubleToStringNoDecimal(Double.parseDouble(CommisionAmount))+"");
                            binding.totalAmountPay.setText(Preference.doubleToStringNoDecimal(Double.parseDouble(FInalAmt+""))+"");
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
                        walletAmount =   Double.parseDouble(finallyPr.getResult().getPaymentWallet());
                    } else {
                        Toast.makeText (ConfirmPaymentAct.this, "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
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

        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ConfirmPaymentAct.this);
        builder1.setMessage(msg);
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                getString(R.string.go),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(ConfirmPaymentAct.this, HomeActivity.class)
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

}
