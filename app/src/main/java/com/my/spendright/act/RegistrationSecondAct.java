package com.my.spendright.act;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Model.StateModel;
import com.my.spendright.R;
import com.my.spendright.databinding.ActvityRegistrationSecondBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class RegistrationSecondAct extends AppCompatActivity {
    public String TAG ="RegistrationSecondAct";
    ActvityRegistrationSecondBinding binding;
    String userId="",phone="",countryCode="",stateName="";
    private SessionManager sessionManager;
    ArrayList<StateModel.Result.State>arrayList;
    HashMap<String,String> map;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.actvity_registration_second);
        sessionManager = new SessionManager(RegistrationSecondAct.this);

        initViews();
        getAllState();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initViews() {
        arrayList = new ArrayList<>();
        if(getIntent()!=null){
            //userId = getIntent().getStringExtra("user_id");
            countryCode = getIntent().getStringExtra("countryCode");
            phone = getIntent().getStringExtra("phone_number");
            map = (HashMap<String, String>) getIntent().getExtras().get("hashMaps");

        }

        binding.imgBack.setOnClickListener(view ->finish());

        binding.edState.setOnClickListener(view -> {
            if(arrayList.size()>0){
                showDropDownCountry(view,binding.edState,arrayList);
            }
        });


        binding.btnNext.setOnClickListener(view -> validation());
    }


    public void validation() {
        if (binding.edtStreetAddress.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(this, "Please Enter Full Address.", Toast.LENGTH_SHORT).show();
        else if (stateName.equalsIgnoreCase(""))
            Toast.makeText(this, "Please Enter State.", Toast.LENGTH_SHORT).show();
        else if (binding.edPassword.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();
        else if (!binding.edPassword.getText().toString().equalsIgnoreCase(binding.edConfirmPassword.getText().toString()))
            Toast.makeText(this, "Password doesn't matched.", Toast.LENGTH_SHORT).show();
        else if (!binding.checEdTerms.isChecked())
            Toast.makeText(this, "please accept Terms &amp; Conditions.", Toast.LENGTH_SHORT).show();

        else {
            if (sessionManager.isNetworkAvailable()) {
              //  binding.progressBar.setVisibility(View.VISIBLE);
                // generateToken();
                //  generateFlAccount();



              //  signUpMethod_two("");
                map.put("referral_code",binding.edReferralCode.getText().toString());
                map.put("full_address",binding.edtStreetAddress.getText().toString());
                map.put("state",binding.edState.getText().toString());
                map.put("password",binding.edPassword.getText().toString());

                startActivity(new Intent(RegistrationSecondAct.this,LoginOne.class).putExtra("phone_number",phone)
                        .putExtra("countryCode",countryCode).putExtra("hashMaps",map));


            } else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownCountry(View v, TextView textView, List<StateModel.Result.State> stringList) {
        PopupMenu popupMenu = new PopupMenu(RegistrationSecondAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    stateName = stringList.get(i).getName();

                }
            }
            return true;
        });
        popupMenu.show();
    }



    private void signUpMethod_two(String batchId){
        Call<ResponseBody> call = RetrofitClientsOne.getInstance().getApi()
                .Api_signup_two(userId,binding.edReferralCode.getText().toString(),binding.edtStreetAddress.getText().toString(),binding.edState.getText().toString(),
                        binding.edPassword.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Signup two  Response :" + stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                        startActivity(new Intent(RegistrationSecondAct.this,LoginOne.class).putExtra("phone_number",jsonObject.getJSONObject("result").getString("mobile"))
                                .putExtra("countryCode",jsonObject.getJSONObject("result").getString("country_code"))
                                .putExtra("user_id",jsonObject.getJSONObject("result").getString("id")));

                        finish();

                    } else {
                        Toast.makeText(RegistrationSecondAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegistrationSecondAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllState(){
        Call<ResponseBody> call = RetrofitClientsOne.getInstance().getApi()
                .Api_get_all_state();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Ge All State  Response :" + stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                         StateModel stateModel = new Gson().fromJson(stringResponse,StateModel.class);
                         arrayList.clear();
                         arrayList.addAll(stateModel.getResult().getStates());


                    } else {
                        arrayList.clear();
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
                Toast.makeText(RegistrationSecondAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
