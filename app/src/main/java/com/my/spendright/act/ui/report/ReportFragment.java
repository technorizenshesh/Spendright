package com.my.spendright.act.ui.report;

import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.spendright.Model.GetBudgetActTransaction;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.R;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.PaiChartAct;
import com.my.spendright.act.SettingActivity;
import com.my.spendright.act.UpdatedAccountInfoTrasaction;
import com.my.spendright.act.ui.home.virtualcards.TransactionAdapter;
import com.my.spendright.adapter.CategoryAdapterNewFilter;
import com.my.spendright.adapter.ExpencePaymentAdapter;
import com.my.spendright.databinding.FragmentReportBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;


public class ReportFragment extends Fragment {
    private String TAG = "ReportFragment";

    FragmentReportBinding binding;
    private SessionManager sessionManager;
    ArrayList<GetExpenSeReport.Result> modelList = new ArrayList<>();
    ArrayList<GetExpenSeReport.Result> sendToServerList = new ArrayList<>();


    ExpencePaymentAdapter mAdapter;

    private int mYear, mMonth, mDay;
    String paymentdate = "";


    String ToDate = "";
    String frmDate = "";

    String FrmAmt = "";
    String ToAmt = "";

    String totalInCOmes = "";
    double totalExpences = 0.0;

    String Transaction_Type = "expense";

    TransactionAdapter transactionAdapter;
    String filterText;

    private ArrayList<GetCategoryModelNew.Result> modelListCategory = new ArrayList<>();
    ArrayList<String> filterList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeActivity.container.setBackgroundColor(getResources().getColor(R.color.blue));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false);

        sessionManager = new SessionManager(getActivity());

        setUpNavigationDrawer();


        filterList.add("Daily");
        filterList.add("Weekly");
        filterList.add("Monthly");
        filterList.add("Yearly");
        filterList.add("All");

        binding.RRimgSetting.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });

        binding.ivExport.setOnClickListener(v -> {
            addDataToIt();
        });

        binding.imgGraph.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PaiChartAct.class)
                   // .putExtra("income", totalInCOmes+"")
                    .putExtra("expence", totalExpences+""));
            //  binding.llgraph.setVisibility(View.GONE);
            //binding.RRgraph.setVisibility(View.VISIBLE);
        });

        binding.imgGraph1.setOnClickListener(v -> {
            binding.llgraph.setVisibility(View.VISIBLE);
            binding.RRgraph.setVisibility(View.GONE);
        });

        binding.tvFilter.setOnClickListener(view -> showDropDownFilterList(view, binding.tvFilter, filterList, modelList));


        return binding.getRoot();
    }

    private void addDataToIt() {
        try {
            JSONArray jsonArray = new JSONArray();
            if (sendToServerList.size() > 0) {
                for (int i = 0; i < sendToServerList.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", sendToServerList.get(i).getId());
                    jo.put("user_id", sendToServerList.get(i).getUserId());
                    jo.put("account_budget_id", sendToServerList.get(i).getAccountBudgetId());
                    jo.put("pay_name", sendToServerList.get(i).getPayName());
                    jo.put("transaction_amount", sendToServerList.get(i).getTransactionAmount());
                    jo.put("type", sendToServerList.get(i).getType());
                    jo.put("main_category_id", sendToServerList.get(i).getMainCategoryId());
                    jo.put("main_category_name", sendToServerList.get(i).getMainCategoryName());
                    jo.put("date_time", sendToServerList.get(i).getDateTime());
                    jo.put("description", sendToServerList.get(i).getDescription());
                    jo.put("emoji", sendToServerList.get(i).getEmoji());
                    jo.put("refrence_no", sendToServerList.get(i).getRefrence_no());
                    jo.put("service", sendToServerList.get(i).getService());
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

    @Override
    public void onResume() {
        super.onResume();

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetPaymentReportMethod();
            GetAccountBudgetMethod();
        } else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(ArrayList<GetExpenSeReport.Result> modelList) {
        mAdapter = new ExpencePaymentAdapter(getActivity(), modelList);
        binding.recycleViewReport.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recycleViewReport.setLayoutManager(linearLayoutManager);
        //binding.recyclermyAccount.setLayoutManager(linearLayoutManager);
        binding.recycleViewReport.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new ExpencePaymentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetExpenSeReport.Result model) {

                startActivity(new Intent(getActivity(), UpdatedAccountInfoTrasaction.class).putExtra("trasaction_id", model.getId()));

            }
        });
    }

    private void GetAccountBudgetMethod() {
        Call<GetCategoryModelNew> call = RetrofitClients.getInstance().getApi()
                .Api_get_account_detail(sessionManager.getUserID());
        call.enqueue(new Callback<GetCategoryModelNew>() {
            @Override
            public void onResponse(Call<GetCategoryModelNew> call, Response<GetCategoryModelNew> response) {

                binding.progressBar.setVisibility(View.GONE);

                GetCategoryModelNew finallyPr = response.body();

                if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                    modelListCategory = (ArrayList<GetCategoryModelNew.Result>) finallyPr.getResult();

                    if (getActivity() != null) {

                        CategoryAdapterNewFilter customAdapter = new CategoryAdapterNewFilter(getActivity(), modelListCategory);
                        binding.drawerFilterLayout.spinnerBudgetAct.setAdapter(customAdapter);
                    }
                } else {

                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetCategoryModelNew> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void GetPaymentReportMethod() {
        modelList.clear();
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .get_vtpass_history_search(sessionManager.getUserID(), FrmAmt, ToAmt, frmDate, ToDate, Transaction_Type, "", "SYSTEM");
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());
        requestBody.put("account_budget_id", "");
        requestBody.put("from_date", frmDate);
        requestBody.put("to_date", ToDate);
        requestBody.put("from_amount", FrmAmt);
        requestBody.put("to_amount", ToAmt);
        requestBody.put("transaction_type", Transaction_Type);
        requestBody.put("trx_type", "SYSTEM");

        Log.e(TAG, "Payment Report Transaction Request==" + requestBody.toString());


        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Payment Report Transaction Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        GetExpenSeReport finallyPr = new Gson().fromJson(stringResponse, GetExpenSeReport.class);

                        // binding.txtExpence.setText("Total Expenses-"+finallyPr.getTotalExpense());

                       // totalInCOmes = finallyPr.getTotalIncome();
                       // totalExpences = finallyPr.getTotalExpense();
                        totalExpences = 0.0;
                        binding.txtIncome.setText(totalInCOmes);
                        modelList.clear();
                        sendToServerList.clear();
                        modelList = (ArrayList<GetExpenSeReport.Result>) finallyPr.getResult();
                        sendToServerList.addAll(modelList);
                        for(int i=0;i<modelList.size();i++){
                           totalExpences = totalExpences + (Double.parseDouble(modelList.get(i).getTransactionAmount()) );//+ Double.parseDouble(modelList.get(i).getAdminFee()));
                        }
                        binding.txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpences));


                        Log.e("serverListSize===", sendToServerList.size() + "");
                        // setAdapter(modelList);
                        transactionAdapter = new TransactionAdapter(getActivity(), modelList);
                        binding.recycleViewReport.setAdapter(transactionAdapter);

                    } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                        modelList.clear();
                        transactionAdapter.notifyDataSetChanged();
                        binding.txtIncome.setText("0.00");
                        binding.txtExpence.setText("0.00");

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

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setUpNavigationDrawer() {

        binding.imgFilter.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);
        });

        binding.drawerFilterLayout.RRBack.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);
        });

       /* binding.drawerFilterLayout.llFrmDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);

                           // paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

                            paymentdate = (year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

                            frmDate  = paymentdate;

                            binding.drawerFilterLayout.txtDate.setText(paymentdate);
                        }

                    }, mYear, mMonth, mDay);

         //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePickerDialog.show();

        });

        binding.drawerFilterLayout.llToDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            //paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            paymentdate = (year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                            ToDate = paymentdate;
                            binding.drawerFilterLayout.txtToDAte.setText(paymentdate);
                        }
                    }, mYear, mMonth, mDay);
          //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();

        });*/


        binding.drawerFilterLayout.llFrmDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);

                            // paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

                            paymentdate = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            frmDate = paymentdate;

                            binding.drawerFilterLayout.txtFormDate.setText(paymentdate);
                        }

                    }, mYear, mMonth, mDay);

            //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();

        });

        binding.drawerFilterLayout.llToDate.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            view.setVisibility(View.VISIBLE);
                            //paymentdate = (dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            paymentdate = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            ToDate = paymentdate;
                            binding.drawerFilterLayout.txtToDate.setText(paymentdate);
                        }
                    }, mYear, mMonth, mDay);
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();

        });


        binding.drawerFilterLayout.RRFilter.setOnClickListener(v -> {


            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);

            FrmAmt = binding.drawerFilterLayout.edtFrmAmt.getText().toString();
            ToAmt = binding.drawerFilterLayout.edtToAmt.getText().toString();

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                GetPaymentReportMethod();
            } else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });

        binding.drawerFilterLayout.txtReset.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);

            binding.drawerFilterLayout.txtFormDate.setText("00-00-00");
            binding.drawerFilterLayout.txtToDate.setText("00-00-00");
            binding.drawerFilterLayout.edtFrmAmt.setText("00");
            binding.drawerFilterLayout.edtToAmt.setText("00");
            binding.tvFilter.setText("All transactions");
            binding.drawerFilterLayout.RadbtnAll.setChecked(true);

            paymentdate = "";
            ToDate = "";
            frmDate = "";
            FrmAmt = "";
            ToAmt = "";
            Transaction_Type = "";

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                GetPaymentReportMethod();
            } else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });

       /* binding.drawerFilterLayout.txtAll.setOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.END))
                binding.drawer.closeDrawer(GravityCompat.END);
            else binding.drawer.openDrawer(GravityCompat.END);

            if (sessionManager.isNetworkAvailable()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                GetPaymentReportMethod();
            }else {
                Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownFilterList(View v, TextView textView, List<String> stringList, ArrayList<GetExpenSeReport.Result> mainList) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i));
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).equalsIgnoreCase(menuItem.getTitle().toString())) {
                    filterText = stringList.get(i);
                    textView.setText(menuItem.getTitle() + " " + "Transactions");
                    //  listener.onExpense(filterText);

                    if (filterText.equalsIgnoreCase("Daily")) {
                        totalExpences =0.0;
                        sendToServerList.clear();
                        sendToServerList.addAll(filterData(Preference.getCurrentDaily(), mainList));
                        transactionAdapter = new TransactionAdapter(getActivity(), sendToServerList);
                        binding.recycleViewReport.setAdapter(transactionAdapter);
                        for(int j=0;j<sendToServerList.size();j++){
                            totalExpences = totalExpences + (Double.parseDouble(sendToServerList.get(j).getTransactionAmount())); /*+ Double.parseDouble(sendToServerList.get(j).getAdminFee()));*/
                        }
                        binding.txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpences));

                    } else if (filterText.equalsIgnoreCase("Weekly")) {
                        totalExpences =0.0;

                        sendToServerList.clear();
                        sendToServerList.addAll(filterData(Preference.getCurrentWeek(), mainList));
                        transactionAdapter = new TransactionAdapter(getActivity(), sendToServerList);
                        binding.recycleViewReport.setAdapter(transactionAdapter);
                        for(int j=0;j<sendToServerList.size();j++){
                            totalExpences = totalExpences + (Double.parseDouble(sendToServerList.get(j).getTransactionAmount())); /*+ Double.parseDouble(sendToServerList.get(j).getAdminFee()));*/
                        }
                        binding.txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpences));
                    } else if (filterText.equalsIgnoreCase("Monthly")) {
                        totalExpences =0.0;

                        sendToServerList.clear();
                        sendToServerList.addAll(filterData(Preference.getCurrentMonth(), mainList));
                        transactionAdapter = new TransactionAdapter(getActivity(), sendToServerList);
                        binding.recycleViewReport.setAdapter(transactionAdapter);
                        for(int j=0;j<sendToServerList.size();j++){
                            totalExpences = totalExpences + (Double.parseDouble(sendToServerList.get(j).getTransactionAmount())); /*+ Double.parseDouble(sendToServerList.get(j).getAdminFee()));*/
                        }
                        binding.txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpences));
                    } else if (filterText.equalsIgnoreCase("Yearly")) {
                        totalExpences =0.0;

                        sendToServerList.clear();
                        sendToServerList.addAll(filterData(Preference.getCurrentYear(), mainList));
                        transactionAdapter = new TransactionAdapter(getActivity(), sendToServerList);
                        binding.recycleViewReport.setAdapter(transactionAdapter);
                        for(int j=0;j<sendToServerList.size();j++){
                            totalExpences = totalExpences + (Double.parseDouble(sendToServerList.get(j).getTransactionAmount())); /*+ Double.parseDouble(sendToServerList.get(j).getAdminFee()));*/
                        }
                        binding.txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpences));
                    } else {
                        totalExpences =0.0;

                        sendToServerList.clear();
                        sendToServerList = mainList;
                        transactionAdapter = new TransactionAdapter(getActivity(), mainList);
                        binding.recycleViewReport.setAdapter(transactionAdapter);
                        for(int j=0;j<sendToServerList.size();j++){
                            totalExpences = totalExpences + (Double.parseDouble(sendToServerList.get(j).getTransactionAmount())); /*+ Double.parseDouble(sendToServerList.get(j).getAdminFee()));*/
                        }
                        binding.txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpences));
                    }
                }
            }
            return true;
        });
        popupMenu.show();
    }


    public ArrayList<GetExpenSeReport.Result> filterData(ArrayList<String> filterList, ArrayList<GetExpenSeReport.Result> mainList) {
        ArrayList<GetExpenSeReport.Result> list = new ArrayList<>();
        for (int i = 0; i < mainList.size(); i++) {
            for (int j = 0; j < filterList.size(); j++) {
                if (mainList.get(i).getDateTime().equalsIgnoreCase(filterList.get(j)))
                    list.add((mainList.get(i)));

            }


        }

        return list;

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