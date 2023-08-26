package com.my.spendright.act.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.LoginModel;
import com.my.spendright.R;
import com.my.spendright.act.ChangePassword;
import com.my.spendright.act.FundAct;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.act.KYCAct;
import com.my.spendright.act.ManageBeneficiaryAct;
import com.my.spendright.act.MyProfileAct;
import com.my.spendright.act.SelectAccount;
import com.my.spendright.act.ui.budget.BudgetGrpAct;
import com.my.spendright.act.ui.budget.ReferEarnBottomSheet;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualListener;
import com.my.spendright.databinding.DialogFullscreenBinding;
import com.my.spendright.databinding.FragmentSettingsBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class SettingsFragment extends Fragment implements CreateVirtualListener {
    FragmentSettingsBinding binding;
    private SessionManager sessionManager;
    LoginModel finallyPr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HomeActivity.container.setBackgroundColor(getResources().getColor(R.color.white));
        binding = DataBindingUtil.inflate (inflater, R.layout.fragment_settings, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getActivity());


        binding.imgBack.setOnClickListener(v -> {
            //onBackPressed();
        });

        binding.RRMyProfile.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MyProfileAct.class));
        });

        binding.RRTerms.setOnClickListener(v -> {
         //   fullscreenDialog(getActivity());
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://spendright.ng/spendright/terms_conditions")));
        });

        binding.RRPrivacy.setOnClickListener(v -> {
            //   fullscreenDialog(getActivity());
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://spendright.ng/spendright/privacy_policy")));
        });

        binding.RRMyAccount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MyAccountAct.class));
        });


        binding.RRContact.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ContactInfoAct.class));
        });

        binding.RRLogout.setOnClickListener(v -> {
            Preference.clearPreference(getActivity());
            startActivity(new Intent(getActivity(), SelectAccount.class));
        });

        binding.RRChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChangePassword.class));
        });

        binding.RRManageBeneficiary.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ManageBeneficiaryAct.class));
        });


     binding.RRKYC.setOnClickListener(view1 -> {
         if (finallyPr != null) {
             if (finallyPr.getResult().getKycStatus().equalsIgnoreCase("0")) {
                 startActivity(new Intent(getActivity(), KYCAct.class)
                         .putExtra("user_id", finallyPr.getResult().getId())
                         .putExtra("mobile", finallyPr.getResult().getMobile())
                         .putExtra("name", finallyPr.getResult().getLastName() + finallyPr.getResult().getOtherLegalName())
                         .putExtra("from", "SettingsScreen"));
             }
             else {
                 startActivity(new Intent(getActivity(), KycSuccessfulAct.class));

             }

         }
     });



        binding.RRVtpasLogout.setOnClickListener(v -> {

            binding.progressBar.setVisibility(View.GONE);
        });


        binding.RRRateUs.setOnClickListener(view1 ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName()))));

/*
        new RateBottomSheet(getActivity()).show(getActivity().getSupportFragmentManager(),""));
*/

        binding.RRBudgetCat.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ExpenseCatAct.class));
        });

        binding.RRBudgetGroups.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), IncomeCatAct.class));
        });

     /*   binding.RRBiometrics.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), KYCUpdateAct.class));
        });*/

        binding.RRMyProfile.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileUpdateAct.class));
        });

        binding.RRMyAccount.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MyAccountAct.class));
        });

        binding.RRShareApp.setOnClickListener(view1 -> {
         /*   Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
          //  sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);*/

        if(finallyPr!=null)  new ReferEarnBottomSheet(finallyPr.getResult().getMyReferralNo()).callBack(this::onVirtual).show(getChildFragmentManager(), "");


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


    private void GetProfileMethod() {
       // binding.progressBar.setVisibility(View.VISIBLE);
        Call<LoginModel> call = RetrofitClients.getInstance().getApi()
                .Api_get_profile(sessionManager.getUserID());
        call.enqueue(new Callback<LoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    finallyPr = response.body();
                    if (finallyPr.getStatus().equalsIgnoreCase("1")) {
                        //sessionManager.saveAccountReference(finallyPr.getResult().getBatchId());

                    } else {

                        Toast.makeText(getActivity(), "" + finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
//                    binding.recyclermyAccount.setVisibility(View.GONE);
                    //binding.RRadd.setVisibility (View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        GetProfileMethod();
    }



    @Override
    public void onVirtual(String data, String tag) {
        if (tag.equalsIgnoreCase("referral")) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Spendright");
            String shareMessage = "Let me recommend you this application\n\n";
            shareMessage = /*shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n" +*/ finallyPr.getResult().getMessage() + "\n\n" + data;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share"));

        } else {
            GetProfileMethod();
        }

    }

    


}
