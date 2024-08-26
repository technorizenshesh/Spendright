package com.my.spendright.act.ui.expense;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.google.gson.Gson;
import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.PaymentComplete;
import com.my.spendright.act.ui.budget.model.BankListModel;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;
import com.my.spendright.databinding.DialogFullscreenPdf2Binding;
import com.my.spendright.databinding.DialogFullscreenPdfBinding;
import com.my.spendright.databinding.FragmentExpenseBinding;
import com.my.spendright.utils.PermissionCheck;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    JSONArray jsonArray;
    int PERMISSION_ID = 44;


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
             jsonArray = new JSONArray();
            if (sendToServer.size() > 0) {
                ArrayList<String>arrayList = new ArrayList<>();
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
                   arrayList.add(sendToServer.get(i).getId());

                }
                Log.e("sendToServer===", jsonArray.toString());

              //  if(checkPermissions()) Download_PDF_Internal_Storage("https://spendright.ng/webservice/MYPDF?type=pdf&data="+ jsonArray.toString());
            //    else requestPermissions();
                // DownloadReport("pdf",jsonArray.toString());
           // Download_PDF_Internal_Storage("https://spendright.ng/webservice/MYPDF?type=pdf&data="+jsonArray.toString().replace("","%22"));

                Download_PDF_Internal_Storage("https://spendright.ng/webservice/MYPDF?type=pdf&data="+commaSeperated(arrayList));



                //  Download_PDF_Internal_Storage("https://spendright.ng/webservice/MYPDF?type=pdf&data=[{%22id%22:%221086%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%221470.00%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-08%22,%22description%22:%22MTN%20Data%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221080%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221079%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221078%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221077%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221076%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221075%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221074%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221073%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221072%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221071%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221070%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22},{%22id%22:%221069%22,%22user_id%22:%22279%22,%22account_budget_id%22:%22390%22,%22pay_name%22:%22%22,%22transaction_amount%22:%224.90%22,%22type%22:%22expense%22,%22main_category_id%22:%224%22,%22main_category_name%22:%22Utilities%22,%22date_time%22:%222024-03-06%22,%22description%22:%22MTN%20Airtime%20VTU%20|%20+2348064141647%22,%22emoji%22:%22%F0%9F%92%A5%22,%22refrence_no%22:%22%22,%22service%22:%22%22}]");

           //     fullscreenDialog(getActivity(),"https://spendright.ng/webservice/MYPDF?type=pdf&data="+jsonArray.toString());


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String commaSeperated(ArrayList<String> arrayList) {
        StringBuilder result = new StringBuilder();
        for (String string : arrayList) {
            result.append(string);
            result.append(",");
        }
        return result.length() > 0 ? /*result.substring(0, result.length() - 1)*/result.deleteCharAt(result.length() - 1).toString() : "";
    }


    public void fullscreenDialog(Context context,String urlString){
        Dialog dialogFullscreen = new Dialog(context, WindowManager.LayoutParams.MATCH_PARENT);

        DialogFullscreenPdf2Binding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_fullscreen_pdf2, null, false);
        dialogFullscreen.setContentView(dialogBinding.getRoot());

        // WebSettings webSettings = dialogBinding.webView.getSettings();
        // webSettings.setJavaScriptEnabled(true);
        //   webSettings.setBuiltInZoomControls(true);
        //   webSettings.setDisplayZoomControls(false);
        String url =  "https://docs.google.com/viewer?url="+ urlString;
        //String url =  "https://docs.google.com/viewer?url="+ "https://spendright.ng/webservice/invoice_me?transaction_id="+sessionManager.getTransId();


        //   dialogBinding.webView.loadUrl(url);

        Log.e("pdf url===",url);

        dialogBinding.imgBack.setOnClickListener(view -> dialogFullscreen.dismiss());




        dialogBinding.webView.getSettings().setJavaScriptEnabled(true);

        dialogBinding.webView.getSettings().setLoadWithOverviewMode(true);
        dialogBinding.webView.getSettings().setDomStorageEnabled(true);
        dialogBinding.webView.getSettings().setBuiltInZoomControls(true);
        dialogBinding.webView.getSettings().setUseWideViewPort(true);
        dialogBinding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("url===",url);
            }
        });
        dialogBinding.webView.loadUrl(url);


        dialogFullscreen.show();

    }




    private void Download_PDF_Internal_Storage(String url) {
        Log.e("pdf url download===",url);
       // try {
       //     Uri Download_ = Uri.parse("https://spendright.ng/webservice/MYPDF?type=pdf&data="+ URLEncoder.encode(url,"utf-8"));
       // url = url.replace(" ","%20");
        Uri Download_ = Uri.parse(url);
        Log.e("pdf uri download===",Download_.toString());

           DownloadManager.Request request = new DownloadManager.Request(Download_);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("PDF File Download");
            request.setDescription("Downloading " + "pdf" + " file using Download Manager.");
            request.setVisibleInDownloadsUi(true);
            request.setMimeType("application/pdf");
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


    //    } catch (UnsupportedEncodingException e) {
     //       e.printStackTrace();
     //   }



    }


    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_ID
        );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Download_PDF_Internal_Storage("https://spendright.ng/webservice/MYPDF?type=pdf&data="+ jsonArray.toString());

            }
        }
    }


}