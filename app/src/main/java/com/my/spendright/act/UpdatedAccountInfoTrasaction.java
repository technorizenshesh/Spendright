package com.my.spendright.act;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my.spendright.Model.AccountTransactionDetails;
import com.my.spendright.Model.DeleteAccountTrasaction;
import com.my.spendright.Model.GetCategoryModelNew;
import com.my.spendright.Model.GetMainGrpCategory;
import com.my.spendright.Model.SchdulepAymentModel;
import com.my.spendright.NumberTextWatcher;
import com.my.spendright.R;
import com.my.spendright.act.SetBudget.SetBudgetActivity;
import com.my.spendright.adapter.CategoryAdapterNew;
import com.my.spendright.adapter.GetCategoryGrpAdapter;
import com.my.spendright.databinding.ActivityUpdatedAccountInfoTrasactionBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatedAccountInfoTrasaction extends AppCompatActivity {

    ActivityUpdatedAccountInfoTrasactionBinding binding;
    private String trasaction_Id="";
    private SessionManager sessionManager;
    private ArrayList<GetMainGrpCategory.Result> modelListCategory = new ArrayList<>();

    String paymentdate="";
    String account_budget_id="";
    String TypePAyment="";

    String PayName="";
    String Amt="";
    String description ="";
    String CategoryId="";
    String CategoryName="";

    private int mYear, mMonth,mDay;

    private View promptsView;
    private AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_updated_account_info_trasaction);

        binding.edtAmt.addTextChangedListener(new NumberTextWatcher(binding.edtAmt,"#,###"));

        sessionManager = new SessionManager(UpdatedAccountInfoTrasaction.this);

       if(getIntent()!=null)
       {
           trasaction_Id =getIntent().getStringExtra("trasaction_id").toString();
       }


       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });

       binding.txtDate.setOnClickListener(v -> {
           final Calendar myCalendar = Calendar.getInstance();

           DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
               @Override
               public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                   // TODO Auto-generated method stub
                   myCalendar.set(Calendar.YEAR, year);
                   myCalendar.set(Calendar.MONTH, monthOfYear);
                   myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                   String myFormat = "yyyy-MM-dd"; // your format yyyy-MM-dd"
                   SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                   paymentdate = sdf.format(myCalendar.getTime());
                   binding.txtDate.setText(paymentdate);

               }




           };
           DatePickerDialog datePickerDialog = new DatePickerDialog(UpdatedAccountInfoTrasaction.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
           datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
           datePickerDialog.show();
       });

       binding.RRSave.setOnClickListener(v -> {

           Amt=binding.edtAmt.getText().toString();
           PayName=binding.edtName.getText().toString();
           description =binding.edtFlags.getText().toString();

           if(Amt.equalsIgnoreCase(""))
           {
               Toast.makeText(this, "Please Enter Amt.", Toast.LENGTH_SHORT).show();

           }else if(paymentdate.equalsIgnoreCase(""))
           {
               Toast.makeText(this, "Please Select Date.", Toast.LENGTH_SHORT).show();

           }else if(description .equalsIgnoreCase(""))
           {
               Toast.makeText(this, "Please Select description.", Toast.LENGTH_SHORT).show();

           }else
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
                   UpdateScdulePaymentMethos();
               }else {
                   Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
               }
           }

       });

       binding.RRDelete.setOnClickListener(v -> {

           AlertDaliogDelete();

       });


        binding.spinnerBudgetAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                CategoryId = modelListCategory.get(pos).getId();
                CategoryName = modelListCategory.get(pos).getSubCatName();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            GetGrpCategoryMethod();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

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

                        GetCategoryGrpAdapter customAdapter=new GetCategoryGrpAdapter(UpdatedAccountInfoTrasaction.this,modelListCategory);
                        binding.spinnerBudgetAct.setAdapter(customAdapter);

                        GetAccountInfoDetailsMethod();
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



    private void GetAccountInfoDetailsMethod()
    {
        Call<AccountTransactionDetails> call = RetrofitClients.getInstance().getApi()
                .Api_account_detail_info(trasaction_Id);
        call.enqueue(new Callback<AccountTransactionDetails>() {
            @Override
            public void onResponse(Call<AccountTransactionDetails> call, Response<AccountTransactionDetails> response) {

                binding.progressBar.setVisibility(View.GONE);

                AccountTransactionDetails finallyPr = response.body();

                if (finallyPr.getStatus().equalsIgnoreCase("1"))
                {
                     paymentdate=finallyPr.getResult().getDateTime();
                     Amt=finallyPr.getResult().getTransactionAmount();

                     PayName=finallyPr.getResult().getPayName();
                     description =finallyPr.getResult().getDescription();
                     CategoryId=finallyPr.getResult().getCategoryId();

                     TypePAyment=finallyPr.getResult().getType();

                       binding.txtDate.setText(paymentdate);
                       binding.edtAmt.setText(Amt);
                       binding.edtName.setText(PayName);
                       binding.edtFlags.setText(description);

                       if(TypePAyment.equalsIgnoreCase("income"))
                       {
                           binding.SitchBtn.setChecked(false);

                       }else
                       {
                           binding.SitchBtn.setChecked(true);
                       }


                }else {

                    binding.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<AccountTransactionDetails> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void UpdateScdulePaymentMethos(){
        String UserId=sessionManager.getUserID();
        Call<SchdulepAymentModel> call = RetrofitClients.getInstance().getApi()
                .edit_account_info(trasaction_Id,PayName,Amt,TypePAyment,CategoryId,CategoryName,paymentdate
                ,description);
        call.enqueue(new Callback<SchdulepAymentModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<SchdulepAymentModel> call, Response<SchdulepAymentModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    SchdulepAymentModel finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        finish();
                        Toast.makeText(UpdatedAccountInfoTrasaction.this, "Success Save", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    finish();
                    Toast.makeText(UpdatedAccountInfoTrasaction.this, "Success Save", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SchdulepAymentModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void DeleteAccountInfoItemMethod()
    {
        Call<DeleteAccountTrasaction> call = RetrofitClients.getInstance().getApi()
                .Api_delete_account_info(trasaction_Id);
        call.enqueue(new Callback<DeleteAccountTrasaction>() {
            @Override
            public void onResponse(Call<DeleteAccountTrasaction> call, Response<DeleteAccountTrasaction> response) {

                binding.progressBar.setVisibility(View.GONE);

                DeleteAccountTrasaction finallyPr = response.body();

                if (finallyPr.getStatus().equalsIgnoreCase("1"))
                {
                  finish();

                }else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<DeleteAccountTrasaction> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void AlertDaliogDelete() {

        LayoutInflater li;
        TextView txtSave;
        TextView txtCancel;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(this);
        promptsView = li.inflate(R.layout.alert_delete, null);
        txtSave = (TextView) promptsView.findViewById(R.id.txtSave);
        txtCancel = (TextView) promptsView.findViewById(R.id.txtCancel);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionManager.isNetworkAvailable()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    DeleteAccountInfoItemMethod();
                }else {
                    Toast.makeText(UpdatedAccountInfoTrasaction.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


}