package com.my.spendright.act.ui.budget;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.BuildConfig;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.act.FundAct;
import com.my.spendright.act.ui.budget.adapter.GrpDetailsAdapter;
import com.my.spendright.act.ui.budget.listener.onGrpCatListener;
import com.my.spendright.act.ui.budget.withdraw.WithdrawAct;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.act.ui.settings.model.BudgetCategoryModel;
import com.my.spendright.databinding.ActivityBudgetGrpDetailsBinding;
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

public class BudgetGrpAct extends AppCompatActivity implements CreateVirtualListener, onGrpCatListener {
    public String TAG = "BudgetGrpAct";

    ActivityBudgetGrpDetailsBinding binding;
    ArrayList<BudgetCategoryModel.Data> arrayList;
    SessionManager sessionManager;
    LoginModel finallyPr;
    GrpDetailsAdapter adapter;
    private String bGrpId = "", grpName = "", budCat = "";
    private double totalBud = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_budget_grp_details);
        sessionManager = new SessionManager(BudgetGrpAct.this);

        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            bGrpId = getIntent().getStringExtra("id");
            grpName = getIntent().getStringExtra("grpName");
            binding.tvTitle.setText("Budget for " + grpName);
        }
        arrayList = new ArrayList<>();

     /*   arrayList.add(new BudgetGrpDetailModel("","",false));
        arrayList.add(new BudgetGrpDetailModel("","",false));
        arrayList.add(new BudgetGrpDetailModel("","",false));
        arrayList.add(new BudgetGrpDetailModel("","",false));
*/


        binding.RRAddCat.setOnClickListener(view -> {
            new AddNewGrpCatBottomSheet(bGrpId).callBack(this::onVirtual).show(getSupportFragmentManager(), "");

        });

        binding.btnAddMoney.setOnClickListener(view -> {
            startActivity(new Intent(BudgetGrpAct.this, FundAct.class));
        });


    }

    @Override
    public void onVirtual(String data, String tag) {
        if (tag.equalsIgnoreCase("referral")) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Spendright");
            String shareMessage = "Let me recommend you this application\n\n";
            shareMessage = /*shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n" +*/ finallyPr.getResult().getMessage() + "\n\n" + data;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share"));

            ShareBudgetApp(budCat);
        } else {
            GetProfileMethod();
        }

    }

    private void GetProfileMethod() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<LoginModel> call = RetrofitClients.getInstance().getApi()
                .Api_get_profile(sessionManager.getUserID());
        call.enqueue(new Callback<LoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        sessionManager.saveAccountReference(finallyPr.getResult().getBatchId());
                        Log.e("refferece===", finallyPr.getResult() + "");
                         //binding.tvTotalBudget.setText("₦" + finallyPr.getResult().getPaymentWallet());
                        String ttt= "0.00";
                        if(Double.parseDouble(finallyPr.getResult().getPaymentWallet())< 1)  ttt = String.format("%.2f", Double.parseDouble(finallyPr.getResult().getPaymentWallet()));
                        else ttt = Preference.doubleToStringNoDecimal(Double.parseDouble(finallyPr.getResult().getPaymentWallet()));

                        binding.tvTotalBudget.setText("₦"+ ttt);
                        getAllBudgetCategories();

                     /*   adapter = new GrpDetailsAdapter(BudgetGrpAct.this,arrayList,finallyPr);
                        binding.rvGrpDetails.setAdapter(adapter);*/

                    } else {

                        Toast.makeText(BudgetGrpAct.this, "" + finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    //binding.RRadd.setVisibility (View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetProfileMethod();
    }

    private void getAllBudgetCategories() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("bcat_user_id", sessionManager.getUserID());
        requestBody.put("bcat_group_id", bGrpId);
        Log.e(TAG, "getAll category BudgetRequest==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_all_budget_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "getAll category BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        BudgetCategoryModel categoryModel = new Gson().fromJson(stringResponse, BudgetCategoryModel.class);
                        arrayList.clear();
                        arrayList.addAll(categoryModel.getData());
                        binding.tvNotFound.setVisibility(View.GONE);

                        adapter = new GrpDetailsAdapter(BudgetGrpAct.this, arrayList, finallyPr, BudgetGrpAct.this);
                        binding.rvGrpDetails.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        totalBud = 0.0;
                        for (int j = 0; j < arrayList.size(); j++) {
                            totalBud = totalBud + Double.parseDouble(arrayList.get(j).getBcatBudgetAmount());
                        }

                        // binding.tvTotalBudget.setText("₦"+ Preference.doubleToStringNoDecimal(totalBud));

                        if (arrayList.size() == 0) {
                            binding.tvNotFound.setVisibility(View.VISIBLE);
                                binding.rlAmount.setVisibility(View.GONE);

                        } else {
                            binding.tvNotFound.setVisibility(View.GONE);
                            binding.rlAmount.setVisibility(View.VISIBLE);
                        }


                    } else {
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        arrayList.clear();
                        adapter.notifyDataSetChanged();

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
    public void onGrpCate(String tag, BudgetCategoryModel.Data data) {
        if (tag.equalsIgnoreCase("unlock")) {
            budCat = data.getBcatId();
            if (finallyPr != null)
                new ReferEarnBottomSheet(finallyPr.getResult().getMyReferralNo()).callBack(this::onVirtual).show(getSupportFragmentManager(), "");
        } else if (tag.equalsIgnoreCase("withdraw")) {
            alertWithDraw(data.getBcatBudgetAmount(),data.getBcatId());
        } else if (tag.equalsIgnoreCase("edit")) {
            new EditNewGrpCatBottomSheet(data).callBack(this::onVirtual).show(getSupportFragmentManager(), "");

        } else if (tag.equalsIgnoreCase("delete")) {
            alertDeleteBudgetCategory(data.getBcatId());

        }
    }



    private void alertDeleteBudgetCategory(String bCatId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(BudgetGrpAct.this);
        builder1.setMessage(getString(R.string.delete_budget_category));
        builder1.setCancelable(false);


        builder1.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                 deleteBudgetCategory(bCatId);
            }

        });
        builder1.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void ShareBudgetApp(String id) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("bcat_id", id);
        Log.e(TAG, "share app status Request==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_share_unblock(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "share app status Response = " + stringResponse);
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


    private void alertWithDraw(String amount,String catId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(BudgetGrpAct.this);
        builder1.setMessage(getString(R.string.withdraw_funds) + "?");
        builder1.setCancelable(false);


        builder1.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                startActivity(new Intent(BudgetGrpAct.this, WithdrawAct.class).putExtra("amount", amount)
                        .putExtra("catId",catId));
            }

        });
        builder1.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void deleteBudgetCategory(String bCatId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("bcat_user_id", sessionManager.getUserID());
        requestBody.put("bcat_id", bCatId);
        Log.e(TAG, "delete Budget category Request==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_delete_budget_grp_category(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "delete Budget category Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        getAllBudgetCategories();
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




}
