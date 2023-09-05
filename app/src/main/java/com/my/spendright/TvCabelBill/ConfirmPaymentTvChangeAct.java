package com.my.spendright.TvCabelBill;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetCommisionModel;
import com.my.spendright.R;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.databinding.ActivityConfirmPaymentTvChangeBinding;
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

public class ConfirmPaymentTvChangeAct extends AppCompatActivity {
    public String TAG ="ConfirmPaymentTvChangeAct";
    ActivityConfirmPaymentTvChangeBinding binding;

    private SessionManager sessionManager;

    String Request_IDNew="";

    String myWalletBalace="";
    String Meter_Number="";
    String CustomerName="";
    String CustomerType="";
    String RenewalAmt="";
    String ServicesSubscriptionId="";
    String ServicesSubscriptionName="";

    String variation_code="";
    String variation_amount="";
    String variation_name="";
    String phone="",selectBugCategoryId="";

    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();
    String BudgetAccountId="";

    ArrayList<IncomeExpenseCatModel.Category> arrayList = new ArrayList<>();;
    IncomeExpenseCatModel incomeExpenseCatModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_payment_tv_change);

        sessionManager = new SessionManager(ConfirmPaymentTvChangeAct.this);

        Log.d("Request_ID:>>",get_current_Time());

        Request_IDNew=get_current_Time();

        Intent intent=getIntent();


        if(intent !=null)
        {
            Meter_Number =intent.getStringExtra("Meter_Number");
            CustomerName =intent.getStringExtra("CustomerName");
            CustomerType =intent.getStringExtra("CustomerType");
            RenewalAmt =intent.getStringExtra("RenewalAmt");
            ServicesSubscriptionId =intent.getStringExtra("ServicesSubscriptionId");
            ServicesSubscriptionName =intent.getStringExtra("ServicesSubscriptionName");
            myWalletBalace =intent.getStringExtra("myWalletBalace");
            variation_code =intent.getStringExtra("variation_code");
            variation_amount =intent.getStringExtra("variation_amount");
            variation_name =intent.getStringExtra("variation_name");
            phone =intent.getStringExtra("phone");


            binding.meterNumber.setText(Meter_Number);
            binding.MyCuurentBlance.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(myWalletBalace)));
            binding.type.setText(PayMentCabilBillAct.serviceId);
            binding.AmountPay.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(variation_amount)));
            binding.totalAmountPay.setText("₦"+Preference.doubleToStringNoDecimal(Double.parseDouble(variation_amount)));


        }

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtCancel.setOnClickListener(v -> {
           finish();
        });

        binding.edtDescription.setOnClickListener(v -> {
            if (arrayList.size()>0)showDropDownCategory(v,binding.edtDescription,arrayList);
        });


        binding.RRConfirm.setOnClickListener(v -> {
            if(selectBugCategoryId.equalsIgnoreCase("")){
                Toast.makeText(this, "Please go to setting tab and add an expense category", Toast.LENGTH_SHORT).show();
            }

            else {
                if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    PyaAccoun();
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

                    CategoryAdapterNew customAdapter=new CategoryAdapterNew(ConfirmPaymentTvChangeAct.this,modelListCategory);
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
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_pay_tv_change(sessionManager.getUserID(),Request_IDNew,ServicesSubscriptionId,Meter_Number,variation_code,variation_amount,
                phone,"change","1");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

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

                        Toast.makeText(ConfirmPaymentTvChangeAct.this, "SuccessFully Bill pay", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        Toast.makeText(ConfirmPaymentTvChangeAct.this, jsonObject.getString("response_description"), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                    Toast.makeText(ConfirmPaymentTvChangeAct.this, "Transaction Failed.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ConfirmPaymentTvChangeAct.this, response.message(), Toast.LENGTH_SHORT).show();
            }


            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void GetCommisionValue(){
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .getPaymentCommission(sessionManager.getCatId(),PayMentCabilBillAct.serviceId/*"dstv"*/);
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
                            Double CmAmt= Double.valueOf(CommisionAmount);
                            Double TotalAmt= Double.valueOf(variation_amount);
                            Double FInalAmt=CmAmt+TotalAmt;

                            //  binding.tax.setText(CommisionAmount+"");
                            //   binding.totalAmountPay.setText(FInalAmt+"");
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




    private void AddReportMethod(String status,String response){
        String Current_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_add_vtpass_book_payment(sessionManager.getUserID(),Request_IDNew,RenewalAmt,ServicesSubscriptionId,ServicesSubscriptionName,
                        "TVchange",status,Current_date,BudgetAccountId,selectBugCategoryId,binding.edtDescription.getText().toString(),phone,binding.tax.getText().toString(),response);
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
                        startActivity(new Intent(ConfirmPaymentTvChangeAct.this,PaymentComplete.class));
                        finish();

                    } else
                    {
                        Toast.makeText(ConfirmPaymentTvChangeAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
        PopupMenu popupMenu = new PopupMenu(ConfirmPaymentTvChangeAct.this, v);
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



}