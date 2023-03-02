package com.my.spendright.airetime;

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
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.databinding.ActivityConfirmPaymentAirtimeBinding;
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

public class ConfirmPaymentAireTimeAct extends AppCompatActivity {
    public String TAG = "ConfirmPaymentAireTimeAct";
    ActivityConfirmPaymentAirtimeBinding binding;

    String Request_IDNew="";
    String ServicesId="";
    String amount="";
    String phone="";
    String MyCuurentBlance="";
    String ServicesName="";
    GetProfileModel finallyPr;
    double walletAmount ;

    private SessionManager sessionManager;
    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();
    String BudgetAccountId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_payment_airtime);

        sessionManager = new SessionManager(ConfirmPaymentAireTimeAct.this);

        Log.d("Request_ID:>>",get_current_Time());

        Request_IDNew=get_current_Time();

        Intent intent=getIntent();
        if(intent !=null)
        {
            ServicesId =intent.getStringExtra("ServicesId");
            ServicesName =intent.getStringExtra("ServicesName");
            amount =intent.getStringExtra("amount");
            phone =intent.getStringExtra("phone");
            MyCuurentBlance =intent.getStringExtra("MyCuurentBlance");

          //   phone="+234"+phone1;
             binding.MyCuurentBlance.setText(MyCuurentBlance);
             binding.ServiceName.setText(ServicesName);
             binding.AmountPay.setText(amount);
             binding.totalAmountPay.setText(amount);
             binding.txtMobile.setText(phone);
            if(amount.contains(",")) amount = amount.replace(",","");
             Log.e("change value====",amount);

        }

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtCancel.setOnClickListener(v -> {
           finish();
        });

        binding.RRConfirm.setOnClickListener(v -> {
           if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                double t=0.0;
               if(!binding.tax.getText().toString().equalsIgnoreCase("0.00"))
               {
                   t = Double.parseDouble(binding.tax.getText().toString()) + Double.parseDouble(amount);
               }
               else t =  Double.parseDouble(amount);

               if(walletAmount >= t ) PyaAccoun();                else {
                    AlertDialogStatus(getString(R.string.your_wallet_bal_is_low));
                }
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

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(ConfirmPaymentAireTimeAct.this,modelListCategory);
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

    private void PyaAccoun() {
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_pay_airtime(Request_IDNew,ServicesId,amount,phone);
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

                             Toast.makeText(ConfirmPaymentAireTimeAct.this, "SuccessFully Bill pay", Toast.LENGTH_SHORT).show();

                         }else
                         {
                             Toast.makeText(ConfirmPaymentAireTimeAct.this, jsonObject.getString("response_description"), Toast.LENGTH_SHORT).show();
                         }

                     }catch (Exception e){
                         e.printStackTrace();
                     }
                }

 else {


                    Toast.makeText(ConfirmPaymentAireTimeAct.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        GetProfileMethod();
    }

    private void AddReportMethod(String status,String response){
        String Current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_add_vtpass_book_payment(sessionManager.getUserID(),Request_IDNew,amount,ServicesId,ServicesName,
                        "airtime",status,Current_date,BudgetAccountId,"",phone/*binding.edtDescription.getText().toString()*/,phone,binding.tax.getText().toString(),response);
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
                        startActivity(new Intent(ConfirmPaymentAireTimeAct.this, PaymentComplete.class));
                        finish();

                    } else {
                        Toast.makeText(ConfirmPaymentAireTimeAct.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
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
                        Log.e(TAG, "airtime commission  Response = " + stringResponse);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            GetCommisionModel finallyPr = new Gson().fromJson(stringResponse,GetCommisionModel.class);
                            String CommisionAmount = finallyPr.getResult().getCommisionAmount();

                            Double CmAmt= Double.valueOf(CommisionAmount);
                            Double TotalAmt= Double.valueOf(amount);

                            Double FInalAmt=CmAmt+TotalAmt;

                          //  binding.tax.setText(CommisionAmount+"");
                         //   binding.totalAmountPay.setText(FInalAmt+"");
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
                        Toast.makeText (ConfirmPaymentAireTimeAct.this, "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
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

        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ConfirmPaymentAireTimeAct.this);
        builder1.setMessage(msg);
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                getString(R.string.go),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(ConfirmPaymentAireTimeAct.this, HomeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


}
