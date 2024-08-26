package com.my.spendright.act.ui.education;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.R;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.act.ui.budget.AddBeneficiaryBottomSheet;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.ActivityEducationBinding;
import com.my.spendright.utils.ApiClient;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationAct extends AppCompatActivity implements CreateVirtualListener {
    String TAG ="EducationAct";
    ActivityEducationBinding binding;
    ArrayList<EducationModel.Content.Variation>packageArrayList;
    String serviceID="",variation_code="",amount="",serviceName="";

    ArrayList<String>productArrayList;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_education);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_education);
        initViews();
    }

    private void initViews() {
        sessionManager = new SessionManager(this);
        packageArrayList = new ArrayList<>();
        productArrayList = new ArrayList<>();
        productArrayList.add(getString(R.string.waec_registration));
        productArrayList.add(getString(R.string.waec_result));
        productArrayList.add(getString(R.string.jamb));


        binding.tvLink.setOnClickListener(view ->  new EducationProfileIDBottomSheet("").callBack(this::onVirtual).show(getSupportFragmentManager(),""));

        binding.tvProduct.setOnClickListener(view -> showDropDownProduct(EducationAct.this,view,binding.tvProduct,productArrayList));

        binding.tvPackage.setOnClickListener(view ->
        {
            if(packageArrayList.size()>0)showDropDownPackage(EducationAct.this,view,binding.tvPackage,packageArrayList);
        });

       binding.RRProceed.setOnClickListener(view -> {

           if(serviceID.equalsIgnoreCase("")){
               Toast.makeText(this, "Please select product", Toast.LENGTH_SHORT).show();
           }

          else if(variation_code.equalsIgnoreCase("")){
               Toast.makeText(this, "Please select package", Toast.LENGTH_SHORT).show();
           }

           else if(serviceID.equalsIgnoreCase("jamb") && binding.edtProfileID.getText().toString().isEmpty()){
             Toast.makeText(this, "Please Profile ID", Toast.LENGTH_SHORT).show();
           }


           else {

               if (serviceID.equalsIgnoreCase("waec-registration")) {
                   startActivity(new Intent(EducationAct.this, WaecRegistrationAct.class)
                           .putExtra("title", "WAEC Registration")
                           .putExtra("serviceID", serviceID)
                           .putExtra("variation_code", variation_code)
                           .putExtra("amount", amount)
                           .putExtra("serviceName","WAEC Registration"));

               } else if (serviceID.equalsIgnoreCase("waec")) {
                   startActivity(new Intent(EducationAct.this, WaecRegistrationAct.class)
                           .putExtra("title", "WAEC Result")
                           .putExtra("serviceID", serviceID)
                           .putExtra("variation_code", variation_code)
                           .putExtra("amount", amount)
                           .putExtra("serviceName","WAEC Result"));
               }

               else if (serviceID.equalsIgnoreCase("jamb")) {
                   startActivity(new Intent(EducationAct.this, JAMBAct.class)
                           .putExtra("title", "JAMB e-PIN")
                           .putExtra("serviceID", serviceID)
                           .putExtra("variation_code", variation_code)
                           .putExtra("amount", amount)
                           .putExtra("serviceName","JAMB e-PIN")
                           .putExtra("profileId",binding.edtProfileID.getText().toString())
                           .putExtra("customerName",binding.tvAccountName.getText().toString()));

               }

           }

       });



        binding.edtProfileID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==10) {
                    Log.e("onTextChange===",charSequence.length()+"");
                    VerifyAccount();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged===","======");
            }
        });



        binding.imgBack.setOnClickListener(view -> finish());

    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownProduct(Context mContext, View v, TextView textView, ArrayList<String>mainList) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        for (int i = 0; i < mainList.size(); i++) {
            popupMenu.getMenu().add(mainList.get(i));
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
                textView.setText(menuItem.getTitle());
               for(int j=0;j<mainList.size();j++){
                   if(menuItem.getTitle().equals(mainList.get(j))) {
                       binding.tvPackage.setText("Please Select Package");
                       if(mainList.get(j).equalsIgnoreCase("WAEC Registration")){
                           serviceID = "waec-registration";
                           binding.tvProfileId.setVisibility(View.GONE);
                           binding.rlProfileID.setVisibility(View.GONE);
                           binding.viewProfileID.setVisibility(View.GONE);
                           binding.tvAccountName.setVisibility(View.GONE);
                           binding.tvLink.setVisibility(View.GONE);
                           binding.RRProceed.setBackground(getDrawable(R.drawable.border_blue));
                           binding.RRProceed.setAlpha(1.0F);
                           binding.RRProceed.setClickable(true);
                           binding.RRProceed.setFocusable(true);
                           binding.RRProceed.setEnabled(true);
                           callPackageMethod(serviceID);
                       }
                      else if(mainList.get(j).equalsIgnoreCase("WAEC Result")){
                           serviceID = "waec";
                           binding.tvProfileId.setVisibility(View.GONE);
                           binding.rlProfileID.setVisibility(View.GONE);
                           binding.viewProfileID.setVisibility(View.GONE);
                           binding.tvAccountName.setVisibility(View.GONE);
                           binding.tvLink.setVisibility(View.GONE);
                           binding.RRProceed.setBackground(getDrawable(R.drawable.border_blue));
                           binding.RRProceed.setAlpha(1.0F);
                           binding.RRProceed.setClickable(true);
                           binding.RRProceed.setFocusable(true);
                           binding.RRProceed.setEnabled(true);
                           callPackageMethod(serviceID);
                       }
                      else if(mainList.get(j).equalsIgnoreCase("JAMB e-PIN")){
                           serviceID = "jamb";
                           binding.tvProfileId.setVisibility(View.VISIBLE);
                           binding.rlProfileID.setVisibility(View.VISIBLE);
                           binding.viewProfileID.setVisibility(View.VISIBLE);
                           binding.tvAccountName.setVisibility(View.VISIBLE);
                           binding.tvLink.setVisibility(View.VISIBLE);
                           binding.RRProceed.setBackground(getDrawable(R.drawable.border_blue));
                           binding.RRProceed.setAlpha(0.5F);
                           binding.RRProceed.setClickable(false);
                           binding.RRProceed.setFocusable(false);
                           binding.RRProceed.setEnabled(false);

                           //callJAMBMethod(serviceID);
                           callPackageMethod(serviceID);

                       }
                   }

               }


            return true;
        });
        popupMenu.show();
    }


    private void showDropDownPackage(Context mContext, View v, TextView textView, ArrayList<EducationModel.Content.Variation>mainList) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        for (int i = 0; i < mainList.size(); i++) {
            popupMenu.getMenu().add(mainList.get(i).getName());
        }

        //popupMenu.getMenu().add(0,stringList.size()+1,0,R.string.add_new_category ).setIcon(R.drawable.ic_added);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for(int j=0;j<mainList.size();j++){
                if(menuItem.getTitle().equals(mainList.get(j).getName())) {
                    variation_code = mainList.get(j).getVariationCode();
                    amount = mainList.get(j).getVariationAmount();
                }

            }


            return true;
        });
        popupMenu.show();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    private void callPackageMethod(String serviceID) {
        binding.progressBar.setVisibility(View.GONE);

        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().ApiWAECRegistration(Preference.getHeader(EducationAct.this),serviceID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        JSONObject result = jsonObject.getJSONObject("result");
                        Log.e(TAG, "Package Response = " + stringResponse);
                        if(result.has("response_description")) {
                            if (result.getString("response_description").equalsIgnoreCase("000")) {
                                EducationModel educationModel = new Gson().fromJson(result.toString(), EducationModel.class);
                                packageArrayList.clear();
                                packageArrayList.addAll(educationModel.getContent().getVariations());

                            }

                            else
                                Toast.makeText(EducationAct.this, result.getJSONObject("content").getString("errors"), Toast.LENGTH_LONG).show();

                        }


                        else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                            sessionManager.logoutUser();
                            Toast.makeText(EducationAct.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EducationAct.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }




                        else {
                            Toast.makeText(EducationAct.this, result.getJSONObject("content").getString("errors"), Toast.LENGTH_LONG).show();

                        }


                    }



                    catch (Exception e){
                        e.printStackTrace();
                    }





                } else {
                    Toast.makeText(EducationAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void callJAMBMethod(String serviceID) {
        binding.progressBar.setVisibility(View.GONE);

        Call<ResponseBody> call = ApiClient.getInstance().getApi().ApiJAMB(serviceID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                       // JSONObject result = jsonObject.getJSONObject("result");
                        Log.e(TAG, "Package Response = " + stringResponse);
                        if(jsonObject.getString("response_description").equalsIgnoreCase("000")){
                            EducationModel educationModel = new Gson().fromJson(jsonObject.toString(),EducationModel.class);
                            packageArrayList.clear();
                            packageArrayList.addAll(educationModel.getContent().getVariations());

                        }
                        else Toast.makeText(EducationAct.this,jsonObject.getJSONObject("content").getString("errors"),Toast.LENGTH_LONG).show();


                    }catch (Exception e){
                        e.printStackTrace();
                    }





                } else {
                    Toast.makeText(EducationAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void VerifyAccount() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_verify_jamb(Preference.getHeader(EducationAct.this),binding.edtProfileID.getText().toString(),serviceID,variation_code,sessionManager.getUserID());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    // user object available
                    try {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        JSONObject result = jsonObject.getJSONObject("result");
                        Log.e("Check Account", "Verify Account Response :" + stringResponse);
                        if (result.getString("code").equals("000")) {
                            // PayFinalModel finallyPr =  new Gson().fromJson(stringResponse,PayFinalModel.class); // response.body();
                            if (!result.getJSONObject("content").has("error")) {
                                binding.tvAccountName.setText(result.getJSONObject("content").getString("Customer_Name"));
                                binding.tvAccountName.setTextColor(getResources().getColor(R.color.green));
                                binding.RRProceed.setClickable(true);
                                binding.RRProceed.setFocusable(true);
                                binding.RRProceed.setEnabled(true);
                                binding.RRProceed.setBackground(getDrawable(R.drawable.border_blue));
                                binding.RRProceed.setAlpha(1.0F);

                            } else {
                                binding.RRProceed.setClickable(false);
                                binding.RRProceed.setFocusable(false);
                                binding.RRProceed.setEnabled(false);
                                binding.RRProceed.setBackground(getDrawable(R.drawable.border_blue));
                                binding.RRProceed.setAlpha(0.5F);
                            //    serviceID ="";
                            //    binding.edtProfileID.setText("");
                                binding.tvAccountName.setText(result.getJSONObject("content").getString("error"));
                                binding.tvAccountName.setTextColor(getResources().getColor(R.color.red));

                                // Toast.makeText(EducationAct.this, jsonObject.getString("response_description"), Toast.LENGTH_SHORT).show();
                            }

                        }

                        else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                            sessionManager.logoutUser();
                            Toast.makeText(EducationAct.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EducationAct.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                else {
                    Toast.makeText(EducationAct.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onVirtual(String data, String tag) {

    }
}
