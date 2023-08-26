package com.my.spendright.act.ui.budget;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.my.spendright.GraphAct;
import com.my.spendright.Model.GetMyAccountModel;
import com.my.spendright.R;
import com.my.spendright.act.ConfirmPaymentAct;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.ui.budget.adapter.BudgetGrpAdapter;
import com.my.spendright.act.ui.budget.adapter.MyCustomListAdapter;
import com.my.spendright.act.ui.budget.listener.onGrpListener;
import com.my.spendright.act.ui.budget.model.BudgetGrpModel;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.adapter.MyAccountAdapter;
import com.my.spendright.databinding.FragmentBudgetBinding;
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


public class BudgetFragment extends Fragment implements CreateVirtualListener, onGrpListener {
    private String TAG = "BudgetFragment";
    FragmentBudgetBinding binding;
    private SessionManager sessionManager;
   // MyAccountAdapter mAdapter;
    ArrayList<GetMyAccountModel.AccountDetail> modelList = new ArrayList<>();
    ArrayList<BudgetGrpModel.Group> arrayList;
    BudgetGrpAdapter adapter;
    MyCustomListAdapter mAdapter;
    private double totalBud =0.0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeActivity.container.setBackgroundColor(getResources().getColor(R.color.blue));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_budget, container, false);

        sessionManager = new SessionManager(getActivity());
        initViews();


        return binding.getRoot();

    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new BudgetGrpAdapter(getActivity(), arrayList, BudgetFragment.this);
        binding.recyclermyAccount.setAdapter(adapter);

        binding.RRAddGrp.setOnClickListener(view -> {
            new AddNewGrpBottomSheet().callBack(this::onVirtual).show(getChildFragmentManager(), "");

        });

      /*  mAdapter = new MyCustomListAdapter();

        // Set click to false (user did not clicked yet)
        mAdapter.setIfUserAlreadyClickedOption(true);

        // Set text and progress
        mAdapter.setOptions(new String []{"Option1", "Option2", "Option3"});


        mAdapter.setProgressBarValues(new float [] {50,75,25});
        binding.listView.setAdapter(mAdapter);*/

    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            // getMonthBugdet();
            getAllBudgetGrps();
        } else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


    }

/*
    private void setAdapter(ArrayList<GetMyAccountModel.AccountDetail> modelList) {


        mAdapter = new MyAccountAdapter(getActivity(), modelList);
        binding.recyclermyAccount.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recyclermyAccount.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MyAccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetMyAccountModel.AccountDetail model) {

                startActivity(new Intent(getActivity(), GraphAct.class).putExtra("month", model.getMonthName()));

            }
        });
    }
*/

    private void getMonthBugdet() {
        Call<GetMyAccountModel> call = RetrofitClients.getInstance().getApi()
                .getMonthBugdet(sessionManager.getUserID());
        call.enqueue(new Callback<GetMyAccountModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetMyAccountModel> call, Response<GetMyAccountModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetMyAccountModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        modelList = (ArrayList<GetMyAccountModel.AccountDetail>) finallyPr.getAccountDetail();

                        //setAdapter(modelList);
                    } else {

                        binding.progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetMyAccountModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onVirtual(String data,String tag) {
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getAllBudgetGrps();
        } else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }


    private void getAllBudgetGrps() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("group_owner_id", sessionManager.getUserID());
        Log.e(TAG, "getAll grps BudgetRequest==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_get_all_grps(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "getAll grps BudgetResponse = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        BudgetGrpModel budgetGrpModel = new Gson().fromJson(stringResponse, BudgetGrpModel.class);

                        arrayList.clear();
                        for (int i=0;i<budgetGrpModel.getGroups().size();i++){
                            if(!budgetGrpModel.getGroups().get(i).getGroupStatus().equalsIgnoreCase("DELETED"))
                                arrayList.add(budgetGrpModel.getGroups().get(i));
                        }
                        totalBud =0.0;
                        for (int j=0;j<arrayList.size();j++){
                            totalBud = totalBud + Double.parseDouble(arrayList.get(j).getAmount());
                        }


                        String bud = totalBud+"";

                        binding.tvTotalBudget.setText("₦"+ Preference.doubleToStringNoDecimalSecond(Double.parseDouble(bud.replace(".0",""))));


                      //  binding.tvTotalBudget.setText("₦"+ bud.replace(".0",""));


                        if(arrayList.size()==0){
                            binding.tvNotFound.setVisibility(View.VISIBLE);
                            binding.llGrp.setVisibility(View.GONE);
                            binding.llTotal.setVisibility(View.GONE);
                            binding.cardColor.setVisibility(View.GONE);

                        }
                        else {
                            binding.tvNotFound.setVisibility(View.GONE);
                            binding.llGrp.setVisibility(View.VISIBLE);
                            binding.llTotal.setVisibility(View.VISIBLE);
                            binding.cardColor.setVisibility(View.VISIBLE);
                        }


                        adapter.notifyDataSetChanged();

                        for (int k=0;k<arrayList.size();k++){
                            View view =new View(getActivity());
                            double weight =   (100 * Double.parseDouble(arrayList.get(k).getAmount()))/totalBud;
                            Log.e("View weight=====",weight+"");
                             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, (float) weight);
                             view.setLayoutParams(params);
                             view.setBackgroundColor(Color.parseColor(arrayList.get(k).getGroupColor()));
                             binding.llColor.addView(view);

                        }



                    } else {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        binding.llGrp.setVisibility(View.GONE);
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

    private void deleteBudgetGrps(String grpId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("group_row_id", grpId);
        requestBody.put("group_owner_id", sessionManager.getUserID());
        Log.e(TAG, "delete grp BudgetRequest==" + requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_delete_budget_grp(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "delete grp BudgetResponse = " + stringResponse);
                    if (sessionManager.isNetworkAvailable()) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        getAllBudgetGrps();
                    } else {
                        Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                 /*  if (jsonObject.getString("status").equalsIgnoreCase("1")) {



                    } else {

                    }*/

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
    public void onGrp(int position, BudgetGrpModel.Group group, String tag) {
        if (tag.equalsIgnoreCase("delete")) alertGrpDialog(group);
    }

    private void alertGrpDialog(BudgetGrpModel.Group group) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_budget_group));
        builder1.setCancelable(false);


        builder1.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    deleteBudgetGrps(group.getGroupRowId());
                } else {
                    Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();

                }
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
}