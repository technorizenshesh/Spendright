package com.my.spendright.act.ui.settings;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.Model.StateModel;
import com.my.spendright.R;
import com.my.spendright.act.ConfirmPaymentAct;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.MyProfileAct;
import com.my.spendright.act.RegistrationSecondAct;
import com.my.spendright.act.SelectAccount;
import com.my.spendright.act.ui.NumberVerifyAct;
import com.my.spendright.act.ui.budget.BudgetGrpAct;
import com.my.spendright.databinding.ActivityKycBinding;
import com.my.spendright.databinding.ActivityUpdateProfileBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class ProfileUpdateAct extends AppCompatActivity {
    public String TAG ="ProfileUpdateAct";

    ActivityUpdateProfileBinding binding;
    SessionManager sessionManager;
    GetProfileModel finallyPr;
    ArrayList<StateModel.Result.State> arrayList;
    String stateName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        sessionManager = new SessionManager(ProfileUpdateAct.this);
        initViews();


    }

    private void initViews() {
        arrayList = new ArrayList<>();

        binding.imgBack.setOnClickListener(v -> {
            finish();
        });


        binding.btnUpdate.setOnClickListener(v -> {
            validation();
        });

        binding.ivDelete.setOnClickListener(view -> AlertDialogAccountDelete());


        binding.edCountry.setOnClickListener(v -> {
           if (arrayList.size()>0)showDropDownCountry(v,binding.edCountry,arrayList);
        });

        binding.ivEdit.setOnClickListener(view -> {
            binding.tvTitle.setText(getString(R.string.update_profile));
            binding.btnUpdate.setVisibility(View.VISIBLE);
            if(finallyPr.getResult().getKycStatus().equalsIgnoreCase("0")){
                binding.edUsername.setEnabled(false);
                binding.edLastName.setEnabled(true);
                binding.edOtherName.setEnabled(true);
                binding.edPhoneNumber.setEnabled(true);
                binding.edtStreetAddress.setEnabled(true);
                binding.edCountry.setEnabled(true);
                binding.CpCountry.setEnabled(true);

            }
            else {
                binding.edUsername.setEnabled(false);
                binding.edLastName.setEnabled(false);
                binding.edOtherName.setEnabled(false);
                binding.edPhoneNumber.setEnabled(true);
                binding.edtStreetAddress.setEnabled(true);
                binding.edCountry.setEnabled(true);
                binding.CpCountry.setEnabled(true);

            }
        });

    }

    private void validation() {
        if(finallyPr.getResult().getKycStatus().equalsIgnoreCase("0")){
            if(binding.edLastName.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_legal_lastname), Toast.LENGTH_SHORT).show();
            else if(binding.edOtherName.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_legal_other), Toast.LENGTH_SHORT).show();
            else if(binding.edPhoneNumber.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_phone_number), Toast.LENGTH_SHORT).show();
            else if(binding.edtStreetAddress.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_legal_lastname), Toast.LENGTH_SHORT).show();
            else if(stateName.equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_legal_lastname), Toast.LENGTH_SHORT).show();
            else{
                if(!binding.edPhoneNumber.getText().toString().equalsIgnoreCase(finallyPr.getResult().getMobile())){
                    startActivity(new Intent(ProfileUpdateAct.this, NumberVerifyAct.class)
                            .putExtra("lastName",binding.edLastName.getText().toString())
                            .putExtra("otherName",binding.edOtherName.getText().toString())
                            .putExtra("phoneNumber",binding.edPhoneNumber.getText().toString())
                            .putExtra("countryCode",binding.CpCountry.getSelectedCountryCodeWithPlus())
                            .putExtra("address",binding.edtStreetAddress.getText().toString())
                            .putExtra("state",stateName));
                }
                else UpdateProfileMethod();
            }



        }
        else {

              if(binding.edPhoneNumber.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_phone_number), Toast.LENGTH_SHORT).show();
            else if(binding.edtStreetAddress.getText().toString().equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_legal_lastname), Toast.LENGTH_SHORT).show();
            else if(stateName.equalsIgnoreCase(""))
                Toast.makeText(this, getString(R.string.enter_legal_lastname), Toast.LENGTH_SHORT).show();
            else{
                if(!binding.edPhoneNumber.getText().toString().equalsIgnoreCase(finallyPr.getResult().getMobile())){
                    startActivity(new Intent(ProfileUpdateAct.this, NumberVerifyAct.class)
                            .putExtra("lastName",binding.edLastName.getText().toString())
                            .putExtra("otherName",binding.edOtherName.getText().toString())
                            .putExtra("phoneNumber",binding.edPhoneNumber.getText().toString())
                            .putExtra("countryCode",binding.CpCountry.getSelectedCountryCodeWithPlus())
                            .putExtra("address",binding.edtStreetAddress.getText().toString())
                            .putExtra("state",stateName));
                }
                else UpdateProfileMethod();
            }


        }

        }

    private void GetProfileMethod() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<GetProfileModel> call = RetrofitClients.getInstance().getApi()
                .Api_get_profile_data(sessionManager.getUserID());
        call.enqueue(new Callback<GetProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        stateName = finallyPr.getResult().getState();

                       binding.edUsername.setText(finallyPr.getResult().getUserName());
                        binding.edLastName.setText(finallyPr.getResult().getLastName());
                        binding.edOtherName.setText(finallyPr.getResult().getOtherLegalName());
                        binding.edPhoneNumber.setText(finallyPr.getResult().getMobile());
                        binding.edtStreetAddress.setText(finallyPr.getResult().getFullAddress());
                        binding.edCountry.setText(stateName);
                        binding.CpCountry.setCountryForPhoneCode(Integer.parseInt(finallyPr.getResult().getCountryCode().replace("+","")));
                        binding.edUsername.setEnabled(false);
                        binding.edLastName.setEnabled(false);
                        binding.edOtherName.setEnabled(false);
                        binding.edPhoneNumber.setEnabled(false);
                        binding.edtStreetAddress.setEnabled(false);
                        binding.edCountry.setEnabled(false);
                        binding.CpCountry.setEnabled(false);
                        getAllState();
                    } else {

                        Toast.makeText(ProfileUpdateAct.this, "" + finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    //binding.RRadd.setVisibility (View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void showDropDownCountry(View v, TextView textView, List<StateModel.Result.State> stringList) {
        PopupMenu popupMenu = new PopupMenu(ProfileUpdateAct.this, v);
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
                Toast.makeText(ProfileUpdateAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void UpdateProfileMethod(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_user_update_profile(sessionManager.getUserID(),
                        binding.edLastName.getText().toString(),
                        binding.edOtherName.getText().toString(),
                        binding.edPhoneNumber.getText().toString(),
                        binding.CpCountry.getSelectedCountryCode(),
                        binding.edtStreetAddress.getText().toString(),
                        stateName);

        Map<String,String> map = new HashMap<>();
        map.put("user_id",sessionManager.getUserID());
        map.put("last_name",binding.edLastName.getText().toString());
        map.put("other_legal_name",binding.edOtherName.getText().toString());
        map.put("mobile", binding.edPhoneNumber.getText().toString());
        map.put("country_code", binding.CpCountry.getSelectedCountryCodeWithPlus());
        map.put("full_address",binding.edtStreetAddress.getText().toString());
        map.put("state",stateName);

        Log.e(TAG,"Update profile Request==="+map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Update profile  Response :" + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(ProfileUpdateAct.this, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show();
                        binding.tvTitle.setText(getString(R.string.profile));
                        binding.btnUpdate.setVisibility(View.GONE);
                        GetProfileMethod();
                    } else {
                        Toast.makeText(ProfileUpdateAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileUpdateAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteProfile(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_delete_user_profile(sessionManager.getUserID());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "delete user profile  Response :" + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Preference.clearPreference(ProfileUpdateAct.this);
                        startActivity(new Intent(ProfileUpdateAct.this, SelectAccount.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        Toast.makeText(ProfileUpdateAct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileUpdateAct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void AlertDialogAccountDelete(){

        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ProfileUpdateAct.this);
        builder1.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_account));
        builder1.setCancelable(false);


        builder1.setPositiveButton(
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        DeleteProfile();
                    }
                });


        builder1.setNegativeButton(
                getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        GetProfileMethod();

    }
}

