package com.my.spendright.act.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.ElectircalBill.Model.GetVtsWalletBalnce;
import com.my.spendright.ElectircalBill.PaymentBill;
import com.my.spendright.Model.GetAllAccountModel;
import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.GetVtpassMode;
import com.my.spendright.Model.HomeCatModel;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.PayMentCabilBillAct;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.FundAct;
import com.my.spendright.act.Notification;
import com.my.spendright.act.PaymentReport;
import com.my.spendright.act.SchdulePayment;
import com.my.spendright.act.SetBudget.SetBudgetActivity;
import com.my.spendright.adapter.HomeAdapter;
import com.my.spendright.adapter.MyAccountHomeAdapter;
import com.my.spendright.airetime.PaymentBillAireTime;
import com.my.spendright.databinding.FragmentHomeBinding;
import com.my.spendright.listener.HomeListener;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    public String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    private SessionManager sessionManager;
    ArrayList<GetProfileModel.AccountDetail> modelList = new ArrayList<> ();
    ArrayList<GetExpenSeReport.Result> modelList1=new ArrayList<>();
    ArrayList<GetAllAccountModel.Result> modelList11=new ArrayList<>();

    GetProfileModel finallyPr;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate (inflater, R.layout.fragment_home, container, false);

        sessionManager = new SessionManager (getActivity ());

        binding.RRAddAccount.setOnClickListener (v -> {
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("0"))
                startActivity (new Intent (getActivity (), AddActivity.class));
        });


        binding.tvFund.setOnClickListener (v -> {
            if (modelList11.size()>0) startActivity (new Intent (getActivity(), FundAct.class));
        });

        binding.txtBudget.setOnClickListener (new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                if (modelList11.size()>0) {
                    sessionManager.saveAccountId(finallyPr.getAccountDetail().get(0).getId());
                    startActivity(new Intent(getActivity(), SetBudgetActivity.class));
                }
            }
        });

        binding.LLelectCIty.setOnClickListener (v -> {

            if (modelList11.size()>0) {
                binding.progressBar.setVisibility(View.VISIBLE);
                sessionManager.saveCateId("4");
                startActivity(new Intent(getActivity(), PaymentBill.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }
        });
        binding.bill2.setOnClickListener (v -> {
            if (modelList11.size()>0) {

                binding.progressBar.setVisibility(View.VISIBLE);
                sessionManager.saveCateId("3");
                startActivity(new Intent(getActivity(), PayMentCabilBillAct.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }
        });

        binding.bill3.setOnClickListener (v -> {
            if (modelList11.size()>0) {
                binding.progressBar.setVisibility(View.VISIBLE);
                sessionManager.saveCateId("1");
                startActivity(new Intent(getActivity(), PaymentBillAireTime.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }
        });
        binding.bill4.setOnClickListener (v -> {
            if (modelList11.size()>0) {
                binding.progressBar.setVisibility(View.VISIBLE);
                sessionManager.saveCateId("2");
                startActivity(new Intent(getActivity(), PaymentBillBroadBandAct.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }
        });

        binding.RRnotification.setOnClickListener (v -> {
            startActivity (new Intent (getActivity (), Notification.class));
        });

        binding.RRSchdule.setOnClickListener (v -> {
            startActivity (new Intent (getActivity (), SchdulePayment.class));
        });

        binding.llReport.setOnClickListener (v -> {

            startActivity (new Intent (getActivity (), PaymentReport.class));
        });


        binding.rlAdd.setOnClickListener (v -> {
                startActivity (new Intent (getActivity (), AddActivity.class));
        });


        return binding.getRoot ();
    }

    @Override
    public void onResume() {
        super.onResume ();
        if (sessionManager.isNetworkAvailable ()) {
            binding.progressBar.setVisibility (View.VISIBLE);
            GetProfileMethod ();
            GetPaymentReportMethod();
            GetProfileMethod11();
          //  getHomeCat();

        } else {

            Toast.makeText (getActivity (), R.string.checkInternet, Toast.LENGTH_SHORT).show ();
        }
    }

    private void GetPaymentReportMethod() {
        modelList1.clear();
        //sessionManager.getUserID();
        Log.e("user_id ==",sessionManager.getUserID());
        Log.e("date ==",getCurrentDate());

        Call<GetExpenSeReport> call = RetrofitClients.getInstance().getApi()
         .get_vtpass_history_search(sessionManager.getUserID(),"","",getCurrentDate(),getCurrentDate(),"","");
        call.enqueue(new Callback<GetExpenSeReport>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetExpenSeReport> call, Response<GetExpenSeReport> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetExpenSeReport finallyPr = response.body();
                    binding.progressBar.setVisibility(View.GONE);

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        String totalInCOmes = finallyPr.getTotalIncome().replace(",","");
                        String totalExpences = finallyPr.getTotalExpense().replace(",","");
                        modelList1= (ArrayList<GetExpenSeReport.Result>) finallyPr.getResult();
                        serBarChar(Float.parseFloat(totalInCOmes),Float.parseFloat(totalExpences));


                    } else
                    {
                        serBarChar(0.0f,0.0f);

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    serBarChar(0.0f,0.0f);

                }
            }
            @Override
            public void onFailure(Call<GetExpenSeReport> call, Throwable t) {
                serBarChar(0.0f,0.0f);
            }
        });
    }



    private GetProfileModel.AccountDetail getItem(int position) {
        return modelList.get (position);
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
                        sessionManager.saveAccountReference(finallyPr.getResult().getBatchId());
                        Log.e("refferece===",finallyPr.getResult().getMonnifyAccountReference());
                        if(finallyPr.getResult().getCheckUser().equalsIgnoreCase("1")){
                            binding.rlNewUser.setVisibility(View.VISIBLE);
                            binding.rlOldUser.setVisibility(View.GONE);
                        }
                        else {
                            binding.rlNewUser.setVisibility(View.GONE);
                            binding.rlOldUser.setVisibility(View.VISIBLE);
                        }
                        binding.txtUser.setText ("Welcome, " + finallyPr.getResult ().getFirstName ());
                        modelList = (ArrayList<GetProfileModel.AccountDetail>) finallyPr.getAccountDetail ();
                        final int position = 0;
                        final GetProfileModel.AccountDetail model = getItem (position);
                        binding.txtAmt.setText (model.getTotal());
                        String ttt = Preference.doubleToStringNoDecimal(Double.parseDouble(finallyPr.getResult().getPaymentWallet()));
                        binding.txtAmt1.setText (ttt);
                        if (modelList.size () <= 0) {
//                           binding.recyclermyAccount.setVisibility(View.GONE);
                            binding.RRadd.setVisibility (View.VISIBLE);

                        } else {
//                            binding.recyclermyAccount.setVisibility(View.VISIBLE);
                            binding.RRadd.setVisibility (View.GONE);

//                            setAdapter(modelList);
                        }


                    } else {

                        Toast.makeText (getActivity (), "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
                        binding.progressBar.setVisibility (View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    binding.RRadd.setVisibility (View.VISIBLE);
                    e.printStackTrace ();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility (View.GONE);
//                binding.recyclermyAccount.setVisibility(View.GONE);
                binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }

    private void GetProfileMethod11(){
        Call<GetAllAccountModel> call = RetrofitClients.getInstance().getApi()
                .get_getAllAccount(sessionManager.getUserID());
        call.enqueue(new Callback<GetAllAccountModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetAllAccountModel> call, Response<GetAllAccountModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetAllAccountModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        modelList11= (ArrayList<GetAllAccountModel.Result>) finallyPr.getResult();
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetAllAccountModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public String getCurrentDate(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        return  todayString;
    }



       public void serBarChar(float income, float expenses){
           BarChart chart = binding.barchart;
           ArrayList NoOfEmp = new ArrayList();

           NoOfEmp.add(new BarEntry(1f, income));
           NoOfEmp.add(new BarEntry(2f, expenses));

           ArrayList<String> year = new ArrayList();

           year.add("2008");
           year.add("2009");
           year.add("2010");
           year.add("2011");
           year.add("2012");
           year.add("2013");
           year.add("2014");
           year.add("2015");
           year.add("2016");
           year.add("2017");



           BarDataSet bardataset = new BarDataSet(NoOfEmp, "Today's Income and Expense");
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
               chart.animateY(5000);
           }
           BarData data = new BarData( bardataset);
           bardataset.setColors(new int[]{Color.GREEN,Color.RED});
           Description des = new Description();
           des.setText("Today's income and expense summary");
           des.setTextSize(8f);
           chart.setDescription(des);
           chart.setVisibleYRangeMaximum(bardataset.getYMax() + 120, YAxis.AxisDependency.LEFT);

           //setLegends();
           chart.setData(data);
       }

    public void setLegends(){

        Legend l = binding.barchart.getLegend();

        l.getEntries();

        l.setYEntrySpace(10f);

        l.setWordWrapEnabled(true);

        LegendEntry l1=new LegendEntry("",Legend.LegendForm.SQUARE,10f,2f,null, Color.GREEN);
        LegendEntry l2=new LegendEntry("", Legend.LegendForm.SQUARE,10f,2f,null,Color.RED);

        l.setCustom(new LegendEntry[]{l1,l2});

        l.setEnabled(true);

    }

}
