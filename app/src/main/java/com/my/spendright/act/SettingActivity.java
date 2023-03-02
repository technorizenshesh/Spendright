package com.my.spendright.act;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.my.spendright.ElectircalBill.Model.GetVtsWalletBalnce;
import com.my.spendright.Model.GetVtpassLoginModel;
import com.my.spendright.Model.GetVtpassMode;
import com.my.spendright.R;
import com.my.spendright.act.ui.ContactInfoAct;
import com.my.spendright.databinding.ActivitySettingBinding;
import com.my.spendright.databinding.DialogFullscreenBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    private SessionManager sessionManager;
    private View promptsView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding= DataBindingUtil.setContentView(this,R.layout.activity_setting);

      sessionManager = new SessionManager(SettingActivity.this);

       binding.imgBack.setOnClickListener(v -> {
           onBackPressed();
       });

       binding.RRMyProfile.setOnClickListener(v -> {
           startActivity(new Intent(SettingActivity.this,MyProfileAct.class));
       });

       binding.RRterms.setOnClickListener(v -> {
           fullscreenDialog(SettingActivity.this);
       });

       binding.RRMyAccount.setOnClickListener(v -> {
           startActivity(new Intent(SettingActivity.this,MyAccountAct.class));
       });


        binding.RRContact.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, ContactInfoAct.class));
        });

       binding.RRLogout.setOnClickListener(v -> {
           LogoutVtpassLogin("false");
           Preference.clearPreference(SettingActivity.this);
           startActivity(new Intent(SettingActivity.this, SelectAccount.class));
           finish();
       });

       binding.RRChangePassword.setOnClickListener(v -> {
           startActivity(new Intent(SettingActivity.this, ChangePassword.class));
           finish();
       });



       binding.RRVtpasLogout.setOnClickListener(v -> {

           binding.progressBar.setVisibility(View.GONE);
           LogoutVtpassLogin("false");
       });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.progressBar.setVisibility(View.VISIBLE);
        getVtpassLogin();
    }

    private void getVtpassLogin(){
        Call<GetVtpassMode> call = RetrofitClients.getInstance().getApi()
                .getVTpassLogin(sessionManager.getUserID());
        call.enqueue(new Callback<GetVtpassMode>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetVtpassMode> call, Response<GetVtpassMode> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    GetVtpassMode finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        if(finallyPr.getResult().getCheckStatus().equalsIgnoreCase("true"))
                        {
//                            binding.RRVtpasLogin.setVisibility(View.GONE);
//                            binding.RRVtpasLogout.setVisibility(View.VISIBLE);

                            Preference.save(SettingActivity.this,Preference.KEY_VTPASS_UserName,finallyPr.getResult().getEmail());
                            Preference.save(SettingActivity.this,Preference.KEY_VTPASS_pass,finallyPr.getResult().getPassword());

                        }else
                        {
//                            binding.RRVtpasLogin.setVisibility(View.VISIBLE);
//                            binding.RRVtpasLogout.setVisibility(View.GONE);
                        }

                    }else
                    {
//                       binding.RRVtpasLogin.setVisibility(View.VISIBLE);
//                       binding.RRVtpasLogout.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetVtpassMode> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void AddVtpassLogin(String UserNAme,String Password,String blance){

        Call<GetVtpassLoginModel> call = RetrofitClients.getInstance().getApi()
                .addVTpassLogin(sessionManager.getUserID(),UserNAme,Password,blance);
        call.enqueue(new Callback<GetVtpassLoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetVtpassLoginModel> call, Response<GetVtpassLoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    GetVtpassLoginModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

                        Toast.makeText(SettingActivity.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
//                        binding.RRVtpasLogin.setVisibility(View.GONE);
//                        binding.RRVtpasLogout.setVisibility(View.VISIBLE);

                    } else
                    {
                        Toast.makeText(SettingActivity.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

//                        binding.RRVtpasLogin.setVisibility(View.VISIBLE);
//                        binding.RRVtpasLogout.setVisibility(View.GONE);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetVtpassLoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void AlertDaliogLoginVtpass() {

        LayoutInflater li;
        RelativeLayout RRlogin;
        ImageView imgBack;
        EditText edtEmail;
        EditText edtPassword;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(SettingActivity.this);
        promptsView = li.inflate(R.layout.alert_vtpass_login, null);
        RRlogin = (RelativeLayout) promptsView.findViewById(R.id.RRlogin);
        imgBack = (ImageView) promptsView.findViewById(R.id.imgBack);
        edtEmail = (EditText) promptsView.findViewById(R.id.edtEmail);
        edtPassword = (EditText) promptsView.findViewById(R.id.edtPassword);
        alertDialogBuilder = new AlertDialog.Builder(SettingActivity.this);
        alertDialogBuilder.setView(promptsView);

        RRlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserName=edtEmail.getText().toString();
                String Password=edtPassword.getText().toString();
                if(UserName.equals(""))
                {
                    Toast.makeText(SettingActivity.this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
                }else  if(Password.equals(""))
                {
                    Toast.makeText(SettingActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

                }else
                {
                    binding.progressBar.setVisibility(View.VISIBLE);

                    Preference.save(SettingActivity.this,Preference.KEY_VTPASS_UserName,UserName);
                    Preference.save(SettingActivity.this,Preference.KEY_VTPASS_pass,Password);
                    loginWallet(UserName,Password);
                }

               // binding.progressBar.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }
        });

        imgBack.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void loginWallet(final String username, final String password) {
      //  ApiNew loginService = RetrofitSetup.createService(ApiNew.class, username, password);
        //ApiNew loginService = RetrofitSetup.getClient().create(ApiNew.class);
    //    Map<String,String> headerMap = new HashMap<>();
     //   headerMap.put("api-key",Preference.VTPASS_API_KEY);
    //    headerMap.put("public-key",Preference.VTPASS_PUBLIC_KEY);
        Call<GetVtsWalletBalnce> call = RetrofitClients.getInstance().getApi().Api_balance();
        call.enqueue(new Callback<GetVtsWalletBalnce>() {
            @Override
            public void onResponse(@NonNull Call<GetVtsWalletBalnce> call, @NonNull Response<GetVtsWalletBalnce> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    GetVtsWalletBalnce finallyPr = response.body();

                    binding.progressBar.setVisibility(View.VISIBLE);
                    LogoutVtpassLogin("true");
                    AddVtpassLogin(username,password,""+finallyPr.getContents().getBalance());

                } else {
                    Toast.makeText(SettingActivity.this,response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetVtsWalletBalnce> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(SettingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LogoutVtpassLogin(String CheckType){

        Call<GetVtpassLoginModel> call = RetrofitClients.getInstance().getApi()
                .logoutvtpass(sessionManager.getUserID(),CheckType);
        call.enqueue(new Callback<GetVtpassLoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetVtpassLoginModel> call, Response<GetVtpassLoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    GetVtpassLoginModel finallyPr = response.body();

                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {

//                        binding.RRVtpasLogin.setVisibility(View.VISIBLE);
//                        binding.RRVtpasLogout.setVisibility(View.GONE);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetVtpassLoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    public void fullscreenDialog(Context context){
        Dialog dialogFullscreen = new Dialog(context, WindowManager.LayoutParams.MATCH_PARENT);

        DialogFullscreenBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_fullscreen, null, false);
        dialogFullscreen.setContentView(dialogBinding.getRoot());

        WebSettings webSettings = dialogBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        dialogBinding.webView.loadUrl("https://spendright.ng/spendright/admin/termcondition");

        dialogBinding.tvCancel.setOnClickListener(v -> dialogFullscreen.dismiss());

        dialogBinding.tvOk.setOnClickListener(v -> dialogFullscreen.dismiss());


        dialogFullscreen.show();

    }


}
