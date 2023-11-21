package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;
import com.my.spendright.DemoBase;
import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.Model.GetMyPercentageModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.home.virtualcards.TransactionAdapter;
import com.my.spendright.act.ui.report.PieChartModel;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaiChartAct extends DemoBase implements SeekBar.OnSeekBarChangeListener, OnChartValueSelectedListener
{

    private PieChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY,txtIncome,txtExpence,tvFilter;
    private RelativeLayout RRimgback;

    private ArrayList<GetMyPercentageModel.AccountDetail> modelListCategory = new ArrayList<>();

    ArrayList<PieChartModel.Result> sendToServerList = new ArrayList<>();

    private SessionManager sessionManager;
    // Color.rgb(27,172,250),
    //int [] colors={  Color.rgb(245,121,59)};
    ArrayList<Integer>colors = new ArrayList<>();
    double totalIncomes=0.0,totalExpemce=0.0;
    ProgressBar progressBar;
    public String TAG ="PaiChartAct";
    String filterText;
    ArrayList<String> filterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pai_chart);
        setTitle("PieChartActivity");

        sessionManager = new SessionManager(PaiChartAct.this);

        txtIncome = findViewById(R.id.txtIncome);
        progressBar = findViewById(R.id.progressBar11);

        txtExpence = findViewById(R.id.txtExpence);
        tvFilter = findViewById(R.id.tvFilter11);

        if(getIntent()!=null)
        {
          //  totalIncomes= Double.parseDouble(getIntent().getStringExtra("income"));
            totalExpemce= Double.parseDouble(getIntent().getStringExtra("expence"));
           // sendToServerList = (ArrayList<GetExpenSeReport.Result>) getIntent().getSerializableExtra("list");

           // txtIncome.setText(totalIncomes+"");
            txtExpence.setText("₦"+Preference.doubleToStringNoDecimal(totalExpemce));


        }

        filterList.add("Daily");
        filterList.add("Weekly");
        filterList.add("Monthly");
        filterList.add("Yearly");
        filterList.add("All");

        tvFilter.setOnClickListener(view -> showDropDownFilterList(view, tvFilter, filterList, sendToServerList));




        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);


        seekBarX = findViewById(R.id.seekBar1);
        seekBarY = findViewById(R.id.seekBar2);
        RRimgback = findViewById(R.id.RRimgback);

        RRimgback.setOnClickListener(v -> {
            onBackPressed();
        });

        seekBarX.setOnSeekBarChangeListener(this);
        seekBarY.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        //  chart.setCenterTextTypeface(tfLight);
        // chart.setCenterText(generateCenterSpannableText());
        chart.setCenterText("Total Expenses");

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        seekBarX.setProgress(4);
        seekBarY.setProgress(10);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            chart.animateY(1400, Easing.EaseInOutQuad);
        }
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);


        ////// APi Call
        getReportData();

    }

    @Override
    protected void saveToGallery() {

    }

    private void setData(ArrayList<PieChartModel.Result> sendToServerList) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < sendToServerList.size() ; i++) {
            entries.add(new PieEntry(Float.parseFloat(sendToServerList.get(i).getTotal()),sendToServerList.get(i).getExpenseTrakingCategoryName()));

        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        chart.getLegend().setEnabled(false);



        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }









    private void getReportData() {
        progressBar.setVisibility(View.GONE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_get_pie_chart_report(sessionManager.getUserID());
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("user_id", sessionManager.getUserID());
        Log.e(TAG, " Report Transaction Request==" + requestBody.toString());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Payment Report Transaction Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        PieChartModel finallyPr = new Gson().fromJson(stringResponse, PieChartModel.class);
                        sendToServerList.clear();
                        sendToServerList.addAll(finallyPr.getResult());

                        for (int i=0;i<sendToServerList.size();i++){
                            colors.add(getRandomColor());
                        }

                        setData(sendToServerList);


                    } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                        sendToServerList.clear();
                        txtIncome.setText("0.00");
                        txtExpence.setText("0.00");

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
    private void showDropDownFilterList(View v, TextView textView, List<String> stringList, ArrayList<PieChartModel.Result> mainList) {
        PopupMenu popupMenu = new PopupMenu(PaiChartAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i));
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {

            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).equalsIgnoreCase(menuItem.getTitle().toString())) {
                    filterText = stringList.get(i);
                    textView.setText(menuItem.getTitle() + " " + "Payment Summary");
                    //  listener.onExpense(filterText);

                    if (filterText.equalsIgnoreCase("Daily")) {
                        totalExpemce =0.0;
                        //  sendToServerList.clear();
                      //  sendToServerList.addAll(filterData(Preference.getCurrentDaily(), mainList));
                         setData(filterData(Preference.getCurrentDaily(), mainList));
                        for(int j=0;j<filterData(Preference.getCurrentDaily(), mainList).size();j++){
                            totalExpemce = totalExpemce + (Double.parseDouble(filterData(Preference.getCurrentDaily(), mainList).get(j).getTotal()));
                        }
                        txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpemce));

                    } else if (filterText.equalsIgnoreCase("Weekly")) {
                        totalExpemce =0.0;

                      //  sendToServerList.clear();
                       // sendToServerList.addAll(filterData(Preference.getCurrentWeek(), mainList));
                        setData(filterData(Preference.getCurrentWeek(), mainList));
                        for(int j=0;j<filterData(Preference.getCurrentWeek(), mainList).size();j++){
                            totalExpemce = totalExpemce + (Double.parseDouble(filterData(Preference.getCurrentWeek(), mainList).get(j).getTotal()));
                        }
                        txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpemce));
                    } else if (filterText.equalsIgnoreCase("Monthly")) {
                        totalExpemce =0.0;

                        //sendToServerList.clear();
                      // sendToServerList.addAll(filterData(Preference.getCurrentMonth(), mainList));
                         setData(filterData(Preference.getCurrentMonth(), mainList));
                        for(int j=0;j<filterData(Preference.getCurrentMonth(), mainList).size();j++){
                            totalExpemce = totalExpemce + (Double.parseDouble(filterData(Preference.getCurrentMonth(), mainList).get(j).getTotal()));
                        }
                        txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpemce));
                    } else if (filterText.equalsIgnoreCase("Yearly")) {
                        totalExpemce =0.0;

                       // sendToServerList.clear();
                      // sendToServerList.addAll(filterData(Preference.getCurrentYear(), mainList));
                          setData(filterData(Preference.getCurrentYear(), mainList));
                        for(int j=0;j<filterData(Preference.getCurrentYear(), mainList).size();j++){
                            totalExpemce = totalExpemce + (Double.parseDouble(filterData(Preference.getCurrentYear(), mainList).get(j).getTotal()));
                        }
                        txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpemce));
                    } else {
                        totalExpemce =0.0;

                      // sendToServerList.clear();
                        //sendToServerList = mainList;
                      setData(sendToServerList);
                        for(int j=0;j<sendToServerList.size();j++){
                            totalExpemce = totalExpemce + (Double.parseDouble(sendToServerList.get(j).getTotal()));
                        }
                        txtExpence.setText("₦" + Preference.doubleToStringNoDecimal(totalExpemce));
                    }
                }
            }
            return true;
        });
        popupMenu.show();
    }


    public ArrayList<PieChartModel.Result> filterData(ArrayList<String> filterList, ArrayList<PieChartModel.Result> mainList) {
        ArrayList<PieChartModel.Result> list = new ArrayList<>();
        for (int i = 0; i < mainList.size(); i++) {
            for (int j = 0; j < filterList.size(); j++) {
                if (mainList.get(i).getDateTime().equalsIgnoreCase(filterList.get(j)))
                    list.add((mainList.get(i)));

            }


        }

        return list;

    }







    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}