package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.spendright.Broadband.ConfirmPaymentBroadBandAct;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetMainGrpCategory;
import com.my.spendright.Model.SchdulepAymentModel;
import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.adapter.GetCategoryGrpAdapter;
import com.my.spendright.databinding.ActivityAddTrasactionScreenBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTrasactionScreen extends AppCompatActivity
{
    private String TAG = "AddTrasactionScreen";

    ActivityAddTrasactionScreenBinding binding;
    private int mYear, mMonth,mDay;

    String paymentdate="";
    String account_budget_id="";
    String TypePAyment="";

    String Amt="";
    String description ="";
    String mainCategoryId="",emoji="";
    String mainCategoryName="";

    private SessionManager sessionManager;

    private ArrayList<GetMainGrpCategory.Result> modelListCategory = new ArrayList<>();

    ArrayList<IncomeExpenseCatModel.Category> arrayList = new ArrayList<>();
    IncomeExpenseCatModel incomeExpenseCatModel;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_trasaction_screen);

        binding.edtAmt.addTextChangedListener(new NumberTextWatcher(binding.edtAmt,"#,###"));

        sessionManager = new SessionManager(AddTrasactionScreen.this);

        if(getIntent()!=null)account_budget_id=getIntent().getStringExtra("account_budget_id").toString();

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddTrasactionScreen.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            binding.txtDate.setText(paymentdate);
                        }

                    }, mYear, mMonth, mDay);

          //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();

        });

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRSave.setOnClickListener(v -> {

             Amt=binding.edtAmt.getText().toString();
            // PayName=binding.edtName.getText().toString();
            description =binding.edtFlags.getText().toString();

            if(Amt.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Amt.", Toast.LENGTH_SHORT).show();

            }else if(paymentdate.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Select Date.", Toast.LENGTH_SHORT).show();

            }/*else if(description .equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Select description.", Toast.LENGTH_SHORT).show();

            }*/else
            {
                if(binding.SitchBtn.isChecked())
                {
                    TypePAyment ="expense";
                }else
                {
                    TypePAyment ="income";
                }

                if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    AddAccountInfoPaymentMethos();
                }else {
                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }

        });

        binding.spinnerBudgetAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                mainCategoryId = arrayList.get(pos).getCatId();
                mainCategoryName = arrayList.get(pos).getCatName();
                emoji = arrayList.get(pos).getCatEmoji();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getAllBudgetCategories("EXPENSE");
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        binding.SitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b== true){
                    if (sessionManager.isNetworkAvailable()) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        getAllBudgetCategories("EXPENSE");
                    }else {
                        Toast.makeText(AddTrasactionScreen.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (sessionManager.isNetworkAvailable()) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        getAllBudgetCategories("INCOME");
                    }else {
                        Toast.makeText(AddTrasactionScreen.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


/*
    private void GetGrpCategoryMethod(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<GetMainGrpCategory> call = RetrofitClients.getInstance().getApi()
                .GetGrpCategory();
        call.enqueue(new Callback<GetMainGrpCategory>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetMainGrpCategory> call, Response<GetMainGrpCategory> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetMainGrpCategory finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        modelListCategory = (ArrayList<GetMainGrpCategory.Result>) finallyPr.getResult();

                        GetCategoryGrpAdapter customAdapter=new GetCategoryGrpAdapter(AddTrasactionScreen.this,modelListCategory);
                        binding.spinnerBudgetAct.setAdapter(customAdapter);

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetMainGrpCategory> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
*/


    private void AddAccountInfoPaymentMethos(){

        String UserId=sessionManager.getUserID();

        Call<SchdulepAymentModel> call = RetrofitClients.getInstance().getApi()
                .add_account_info(UserId,Amt,TypePAyment,mainCategoryId,account_budget_id,
                        paymentdate,description,"",mainCategoryName,emoji,"MANUAL");
        call.enqueue(new Callback<SchdulepAymentModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<SchdulepAymentModel> call, Response<SchdulepAymentModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    SchdulepAymentModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        finish();
                        Toast.makeText(AddTrasactionScreen.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SchdulepAymentModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getAllBudgetCategories(String type) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cat_user_id", sessionManager.getUserID());
        requestBody.put("cat_type",type);
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
                      //  selectBugCategoryId = incomeExpenseCatModel.getCategories().get(0).getCatId();
                    //    binding.tvCategoryName.setText(incomeExpenseCatModel.getCategories().get(0).getCatName());

                        mainCategoryId = arrayList.get(0).getCatId();
                        mainCategoryName = arrayList.get(0).getCatName();
                        emoji = arrayList.get(0).getCatEmoji();

                        GetCategoryGrpAdapter customAdapter=new GetCategoryGrpAdapter(AddTrasactionScreen.this,arrayList);
                        binding.spinnerBudgetAct.setAdapter(customAdapter);


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



}