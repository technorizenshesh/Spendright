package com.my.spendright.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.ui.settings.KycSuccessfulAct;
import com.my.spendright.biomatriclogin.Utilitiesss;
import com.my.spendright.databinding.ActivityKycBinding;
import com.my.spendright.databinding.ActivityRegistrationTwoBinding;
import com.my.spendright.utils.Api;
import com.my.spendright.utils.ApiClient;
import com.my.spendright.utils.ApiMonnify;
import com.my.spendright.utils.Constant;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KYCAct extends AppCompatActivity {
    public String TAG = "KYCAct";
    ActivityKycBinding binding;
    private String token="";
    private String dob="";
    private String gender="";
    private String mobileNo="";
    private String name="";
    private String from="";


    private String userId="";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_kyc);
        sessionManager = new SessionManager(KYCAct.this);
        initViews();


    }

    private void initViews() {
        if(getIntent()!=null){
            userId = getIntent().getStringExtra("user_id");
            mobileNo = getIntent().getStringExtra("mobile");
            name = getIntent().getStringExtra("name");
            from = getIntent().getStringExtra("from");
            GetProfileMethod(userId);
            if(from.equalsIgnoreCase("otpScreen")) binding.layPrefer.setVisibility(View.VISIBLE);
            else binding.layPrefer.setVisibility(View.GONE);

        }



        binding.imgBack.setOnClickListener(v -> {
            finish();
        });


        binding.btnVerify.setOnClickListener(v -> {
            validation();
        });


        binding.layPrefer.setOnClickListener(v -> {
            startActivity(new Intent(KYCAct.this,LoginActivity.class));
            finish();
        });




        binding.rdMale.setOnClickListener(view -> {
            gender = "MALE";
        });

        binding.rdFemale.setOnClickListener(view -> {
            gender = "FEMALE";
        });



        binding.llDob.setOnClickListener(view -> {



            final Calendar myCalendar = Calendar.getInstance();

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "dd-MMM-yyyy"; // your format yyyy-MM-dd"
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    dob = sdf.format(myCalendar.getTime());
                    binding.edtDateOFBirth.setText(dob);

                }

            };
            DatePickerDialog datePickerDialog= new DatePickerDialog(KYCAct.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
           // datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
             datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            Log.e("-------",myCalendar.getTimeInMillis()+"");

            datePickerDialog.show();




        });


    }



    private void GetProfileMethod(String userId) {
        Call<GetProfileModel> call = RetrofitClients.getInstance ().getApi ()
                .Api_get_profile_data (userId);
        Log.e("user id=====",userId);
        call.enqueue (new Callback<GetProfileModel> () {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility (View.GONE);
                try {
                    GetProfileModel   finallyPr = response.body ();
                    if (finallyPr.getStatus ().equalsIgnoreCase ("1")) {
                           binding.edtLegalLastName.setText(finallyPr.getResult().getLastName());
                           binding.edtLegalName.setText(finallyPr.getResult().getOtherLegalName());

                    } else {

                        Toast.makeText (KYCAct.this, "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
                        binding.progressBar.setVisibility (View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    //binding.RRadd.setVisibility (View.VISIBLE);
                    e.printStackTrace ();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility (View.GONE);
//                binding.recyclermyAccount.setVisibility(View.GONE);
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }

    private void validation() {
        if (binding.edtBvn.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(KYCAct.this, getString(R.string.please_enter_bvn_number), Toast.LENGTH_LONG).show();
        else if (binding.edtLegalLastName.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(KYCAct.this, getString(R.string.enter_legal_lastname), Toast.LENGTH_LONG).show();
        else if (binding.edtLegalName.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(KYCAct.this, getString(R.string.enter_legal_other), Toast.LENGTH_LONG).show();
        else if (dob.equalsIgnoreCase(""))
            Toast.makeText(KYCAct.this, getString(R.string.please_enter_dob), Toast.LENGTH_LONG).show();
        else if (gender.equalsIgnoreCase(""))
            Toast.makeText(KYCAct.this, getString(R.string.please_select_gender), Toast.LENGTH_LONG).show();
        else if (!binding.checEdTerms.isChecked())
            Toast.makeText(KYCAct.this, getString(R.string.accept_terms_condition), Toast.LENGTH_LONG).show();
        else {
            if (sessionManager.isNetworkAvailable()) kycVerification("");
            else Toast.makeText(KYCAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();

        }
    }

    private void kycVerification(String token) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("bvn",binding.edtBvn.getText().toString());
        requestBody.put("name",name.trim() );
        requestBody.put("dateOfBirth",dob);
        requestBody.put("mobileNo", "0"+mobileNo);
        requestBody.put("user_id", userId);
        requestBody.put("legal_last", binding.edtLegalLastName.getText().toString().trim());
        requestBody.put("legal_name", binding.edtLegalName.getText().toString().trim());
        requestBody.put("gender", gender);


        Log.e("MonnifyKycRequest",requestBody.toString());

        Call<ResponseBody> loginCall = RetrofitClientsOne.getInstance().getApi().Api_verify_kyc(requestBody);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "MonnifyKyc Response = " + stringResponse);
                    if (jsonObject.getBoolean("requestSuccessful") == true) {
                        JSONObject object = jsonObject.getJSONObject("responseBody");
                        JSONObject nameObj = object.getJSONObject("name");
                        String nameMatch = nameObj.getString("matchStatus");
                        String dobMatch = object.getString("dateOfBirth");
                        String mobileMatch = object.getString("mobileNo");
                        if(nameMatch.equalsIgnoreCase("NO_MATCH"))
                            Toast.makeText(KYCAct.this, getText(R.string.name_not_matched), Toast.LENGTH_SHORT).show();
                       else if(dobMatch.equalsIgnoreCase("NO_MATCH"))
                            Toast.makeText(KYCAct.this, getText(R.string.dob_not_matched), Toast.LENGTH_SHORT).show();
                       else if(mobileMatch.equalsIgnoreCase("NO_MATCH"))
                            Toast.makeText(KYCAct.this, getText(R.string.mobile_number_not_matched), Toast.LENGTH_SHORT).show();
                        else if(nameMatch.equalsIgnoreCase("FULL_MATCH")&& dobMatch.equalsIgnoreCase("FULL_MATCH") && mobileMatch.equalsIgnoreCase("FULL_MATCH")){
                            Toast.makeText(KYCAct.this, "KYC verified successfully", Toast.LENGTH_SHORT).show();
                           if(from.equalsIgnoreCase("otpScreen")) {
                               startActivity(new Intent(KYCAct.this, LoginActivity.class)
                                       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                               finish();
                           }
                           else {
                               startActivity(new Intent(KYCAct.this, KycSuccessfulAct.class)
                                       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                               finish();

                           }
                        }

                    } else {
                        Toast.makeText(KYCAct.this, jsonObject.getString("responseMessage"), Toast.LENGTH_SHORT).show();
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
