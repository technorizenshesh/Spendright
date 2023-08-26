package com.my.spendright.act.ui.expense;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.google.gson.Gson;
import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.ui.budget.model.BankListModel;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.FragmentExpenseBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;


public class ExpenseFragment extends Fragment implements ExpenseListener
{
    FragmentExpenseBinding binding;

    private String TAG = "ExpenseFragment";
    private SessionManager sessionManager;
    ArrayList<GetBudgetActTransaction.AccountDetail> modelList=new ArrayList<>();
    ArrayList<GetProfileModel.AccountDetail> modelListTransaction=new ArrayList<>();
    static ArrayList<GetBudgetActTransaction.AccountDetail.Transaction> sendToServer=new ArrayList<>();

    ArrayList<String> filterList=new ArrayList<>();
    public static int  pagerPosition =0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeActivity.container.setBackgroundColor(getResources().getColor(R.color.blue));
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_expense, container, false);

        sessionManager = new SessionManager(getActivity());

        filterList.add("Daily");
        filterList.add("Weekly");
        filterList.add("Monthly");
        filterList.add("Yearly");
        filterList.add("All");

        binding.RRaddImgAccount.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), AddActivity.class));
        });

        binding.ivExport.setOnClickListener(v -> {
            addDataToIt();
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetTransactionMethod("","","","","","");
        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


    }

    private void GetTransactionMethod(String id, String stDate, String enDate, String stAmt, String enAmt, String transType){
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .get_budget_by_transaction(sessionManager.getUserID(),id,stDate,enDate,stAmt,enAmt,transType);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());
        requestBody.put("id",id);
        requestBody.put("start_date", stDate);
        requestBody.put("end_date",enDate );
        requestBody.put("start_amount",stAmt );
        requestBody.put("end_amount",enAmt );
        requestBody.put("transaction_type",transType );

        Log.e(TAG, "getAccount and Transaction Request==" + requestBody.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "getAccount and Transaction Response = " + stringResponse);


                   // Log.e("Expense response==",)
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        GetBudgetActTransaction finallyPr = new Gson().fromJson(stringResponse, GetBudgetActTransaction.class);
                        modelList.clear();
                        sendToServer.clear();
                        modelList= (ArrayList<GetBudgetActTransaction.AccountDetail>) finallyPr.getAccountDetail();

                        binding.viewpager.setAdapter(new CustomPagerAdapter(getActivity(),modelList,filterList,ExpenseFragment.this));
                        binding.viewpager.setCurrentItem(pagerPosition);
                        sendToServer.addAll(modelList.get(pagerPosition).getTransaction());
                    } else {
                        Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }catch (Exception e)
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
    public void onExpense(String id, String stDate, String enDate, String stAmt, String enAmt, String transType) {

        GetTransactionMethod(id,stDate,enDate,stAmt,enAmt,transType);

    }


    private void addDataToIt() {
        try {
            JSONArray jsonArray = new JSONArray();
            if (sendToServer.size() > 0) {
                for (int i = 0; i < sendToServer.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", sendToServer.get(i).getId());
                    jo.put("user_id", sendToServer.get(i).getUserId());
                    jo.put("account_budget_id", sendToServer.get(i).getAccountBudgetId());
                    jo.put("pay_name", sendToServer.get(i).getPayName());
                    jo.put("transaction_amount", sendToServer.get(i).getTransactionAmount());
                    jo.put("type", sendToServer.get(i).getType());
                    jo.put("main_category_id", sendToServer.get(i).getMainCategoryId());
                    jo.put("main_category_name", sendToServer.get(i).getMainCategoryName());
                    jo.put("date_time", sendToServer.get(i).getDateTime());
                    jo.put("description", sendToServer.get(i).getDescription());
                    jo.put("emoji", sendToServer.get(i).getEmoji());
                    jo.put("refrence_no", "");
                    jo.put("service", "");
                    jsonArray.put(jo);
                }
                Log.e("sendToServer===", jsonArray.toString());
                Download_PDF_Internal_Storage("https://spendright.ng/spendright/webservice/MYPDF?type=pdf&data="+jsonArray.toString());
                // DownloadReport("pdf",jsonArray.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Download_PDF_Internal_Storage(String url) {
        Uri Download_ = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("PDF File Download");
        request.setDescription("Downloading " + "pdf" + " file using Download Manager.");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "pdf_name.pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(getActivity(), "PDF Download Started", Toast.LENGTH_SHORT).show();

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                // Do something on download complete
                Toast.makeText(ctxt, "file downloaded...", Toast.LENGTH_SHORT).show();
            }
        };

        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}