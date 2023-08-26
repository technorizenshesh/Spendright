package com.my.spendright.act.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.R;
import com.my.spendright.act.ui.settings.adapter.IncomeExpenseCatAdapter;
import com.my.spendright.act.ui.settings.listener.onCategoryClickListener;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.ActvityBudgetCatBinding;
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

public class ExpenseCatAct extends AppCompatActivity implements onCategoryClickListener {
    private String TAG = "ExpenseCatAct";
    private SessionManager sessionManager;

    ActvityBudgetCatBinding binding;
    ArrayList<IncomeExpenseCatModel.Category>arrayList;
    IncomeExpenseCatAdapter adapter;
    private static int tabPos=2;
    boolean editDoneChk = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.actvity_budget_cat);
        sessionManager = new SessionManager(ExpenseCatAct.this);

        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new IncomeExpenseCatAdapter(ExpenseCatAct.this,arrayList,ExpenseCatAct.this);
        binding.rvBudCat.setAdapter(adapter);

        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnAdd.setOnClickListener(view -> startActivity(new Intent(this,AddBudgetCatAct.class).putExtra("type","EXPENSE")));

        binding.tabIncome.setOnClickListener(view -> setTab(1));

        binding.tabExpense.setOnClickListener(view -> setTab(2));

        binding.tvEditDone.setOnClickListener(view -> {
            showGoneEditDeleteButton();
        });


    }

    private void showGoneEditDeleteButton() {
        if(arrayList.size()>0) {
            if (editDoneChk == false) {
                binding.tvEditDone.setText("Done");
                for (int i =0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(true);
                }
                adapter.notifyDataSetChanged();
                editDoneChk = true;
            }
            else {
                binding.tvEditDone.setText("Edit");
                for (int i =0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(false);
                }
                adapter.notifyDataSetChanged();
                editDoneChk = false;
            }

        }

    }

    private void setTab(int i) {
        if(i==1){
            tabPos =i;
            binding.tabIncome.setTextColor(getResources().getColor(R.color.blue));
            binding.tabExpense.setTextColor(getResources().getColor(R.color.gray));
           if(sessionManager.isNetworkAvailable()) getAllBudgetCategories("INCOME");
           else Toast.makeText(this,getString(R.string.checkInternet),Toast.LENGTH_LONG).show();
        }

      else  if(i==2){
          tabPos =i;
            binding.tabIncome.setTextColor(getResources().getColor(R.color.gray));
            binding.tabExpense.setTextColor(getResources().getColor(R.color.blue));
            if(sessionManager.isNetworkAvailable()) getAllBudgetCategories("EXPENSE");
            else Toast.makeText(this,getString(R.string.checkInternet),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setTab(tabPos);
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
                        IncomeExpenseCatModel incomeExpenseCatModel = new Gson().fromJson(stringResponse, IncomeExpenseCatModel.class);
                        binding.tvNotFound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(incomeExpenseCatModel.getCategories());
                        adapter.notifyDataSetChanged();
                        showGoneEditDeleteButton();

                    } else {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotFound.setVisibility(View.VISIBLE);
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


    private void deleteBudgetCategories(String bCatId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cat_id",bCatId);
        Log.e(TAG, "delete category BudgetRequest==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_delete_budget_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "delete category BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                       getAllBudgetCategories("EXPENSE");
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



    @Override
    public void onCategoryClick(IncomeExpenseCatModel.Category category,String tag) {
        if(tag.equalsIgnoreCase("delete")) AlertDialogDeleteCategory("Are you sure you want to delete " + category.getCatName() + " Category?",category);
        else {
             startActivity(new Intent(ExpenseCatAct.this,EditExpenseCategoryAct.class)
                     .putExtra("data",category)
                     .putExtra("type","EXPENSE"));
        }
    }

    public void AlertDialogDeleteCategory(String msg,IncomeExpenseCatModel.Category category){

        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ExpenseCatAct.this);
        builder1.setMessage(msg);
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        deleteBudgetCategories(category.getCatId());
                    }
                });

        builder1.setNegativeButton(
                getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

}
