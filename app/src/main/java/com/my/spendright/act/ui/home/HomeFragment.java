package com.my.spendright.act.ui.home;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

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
import com.my.spendright.ElectircalBill.PaymentBill;
import com.my.spendright.Model.GetAllAccountModel;
import com.my.spendright.Model.GetExpenSeReport;
import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.Model.HomeCatModel;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.PayMentCabilBillAct;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.FundAct;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.act.Notification;
import com.my.spendright.act.ui.budget.model.BudgetGrpModel;
import com.my.spendright.act.ui.education.EducationAct;
import com.my.spendright.act.ui.home.virtualcards.CreateVirtualAct;
import com.my.spendright.act.ui.home.wallet.TransferFundAct;
import com.my.spendright.adapter.HomeAdapter;
import com.my.spendright.airetime.PaymentBillAireTime;
import com.my.spendright.biomatriclogin.Utilitiesss;
import com.my.spendright.databinding.DialogBudgetInfoBinding;
import com.my.spendright.databinding.FragmentHomeBinding;
import com.my.spendright.listener.HomeListener;
import com.my.spendright.utils.AvatarGenerator;
import com.my.spendright.utils.Constant;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.RetrofitClientsOne;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeListener{
    public String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    private SessionManager sessionManager;
    ArrayList<GetProfileModel.AccountDetail> modelList = new ArrayList<> ();
    ArrayList<GetExpenSeReport.Result> modelList1=new ArrayList<>();
    ArrayList<GetAllAccountModel.Result> modelList11=new ArrayList<>();

    GetProfileModel finallyPr;
    ArrayList<HomeCatModel>homeCatModelArrayList= new ArrayList<>();
    ArrayList<BudgetGrpModel.Group> arrayList =new ArrayList<>();

    String ttt = "0.00";
    String totalBud = "0.00";

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    Executor executor;
    String keyValue="";
    AvatarGenerator avatarGenerator;


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate (inflater, R.layout.fragment_home, container, false);

        sessionManager = new SessionManager (getActivity ());
        //HomeActivity.container.setBackgroundColor(getResources().getColor(R.color.blue));

        homeCatModelArrayList.add(new HomeCatModel("1","Credit Wallet",R.drawable.ic_credit_wallet));
        homeCatModelArrayList.add(new HomeCatModel("2","Set Budget",R.drawable.ic_budget));
        homeCatModelArrayList.add(new HomeCatModel("3","Fund Transfer",R.drawable.ic_fund_transfer));
        homeCatModelArrayList.add(new HomeCatModel("4","Electricity",R.drawable.ic_electricity));
        homeCatModelArrayList.add(new HomeCatModel("5","Data",R.drawable.ic_data));
        homeCatModelArrayList.add(new HomeCatModel("6","Local and Foreign Airtime",R.drawable.ic_airtime));
        homeCatModelArrayList.add(new HomeCatModel("7","Cable TV",R.drawable.ic_cable));
        homeCatModelArrayList.add(new HomeCatModel("8","Transaction Report",R.drawable.ic_transaction));
        homeCatModelArrayList.add(new HomeCatModel("9","",R.drawable.ic_more));

        binding.rvHome.setAdapter(new HomeAdapter(getActivity(),homeCatModelArrayList,HomeFragment.this));

       binding.rlAdd.setOnClickListener (v -> {
         //   if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("0"))
                startActivity (new Intent (getActivity (), AddActivity.class));
        });


        binding.llFundTransfer.setOnClickListener (v -> {
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1")) startActivity (new Intent (getActivity(), TransferFundAct.class)
                    .putExtra("mainBal",totalBud+""));
        });

        binding.llVirtualCard.setOnClickListener (v -> {
           // if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1"))   startActivity (new Intent (getActivity(), CreateVirtualAct.class));
          //  Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), EducationAct.class));
        });



        binding.RRNotification.setOnClickListener (v -> {
            startActivity (new Intent (getActivity (), Notification.class));
        });


        binding.llWallet.setOnClickListener (v -> {
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1"))    startActivity (new Intent (getActivity (), FundAct.class));
        });

        binding.tvRefresh.setOnClickListener(view ->{
            binding.progressBar.setVisibility (View.VISIBLE);
                    GetProfileMethod();
        });




        binding.llBudget.setOnClickListener(view -> {
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1"))   openSetBudgetInfoDialog(getActivity());
        });


/*
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

        */




        binding.llAirtime.setOnClickListener (v -> {
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1"))  {
                sessionManager.saveCateId("1");
                startActivity(new Intent(getActivity(), PaymentBillAireTime.class));
            }
        });


        binding.llData.setOnClickListener (v -> {
          /*  if (modelList11.size()>0) {
                binding.progressBar.setVisibility(View.VISIBLE);

                startActivity(new Intent(getActivity(), PaymentBillBroadBandAct.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }*/

            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1")) {
                sessionManager.saveCateId("2");
                startActivity(new Intent(getActivity(), PaymentBillBroadBandAct.class).putExtra("Balance", totalBud));
            }

        });


        binding.llCableTv.setOnClickListener (v -> {
          /*  if (modelList11.size()>0) {

                binding.progressBar.setVisibility(View.VISIBLE);
                sessionManager.saveCateId("3");
                startActivity(new Intent(getActivity(), PayMentCabilBillAct.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }*/

            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1")) {
                sessionManager.saveCateId("3");
                startActivity(new Intent(getActivity(), PayMentCabilBillAct.class).putExtra("Balance", totalBud));
            }

        });



      /*  binding.llElectricity.setOnClickListener (v -> {

          *//*  if (modelList11.size()>0) {
                binding.progressBar.setVisibility(View.VISIBLE);
                sessionManager.saveCateId("4");
                startActivity(new Intent(getActivity(), PaymentBill.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }*//*
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1"))  {
                sessionManager.saveCateId("4");
                startActivity(new Intent(getActivity(), PaymentBill.class).putExtra("Balance", ttt));
            }

        });*/



        binding.llElectricity.setOnClickListener (v -> {

          /*  if (modelList11.size()>0) {
                binding.progressBar.setVisibility(View.VISIBLE);
                sessionManager.saveCateId("4");
                startActivity(new Intent(getActivity(), PaymentBill.class).putExtra("Balance", finallyPr.getAccountDetail().get(0).getCurrentBalance()));
            }*/
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1"))  {
                sessionManager.saveCateId("4");
                startActivity(new Intent(getActivity(), PaymentBill.class).putExtra("Balance", totalBud));
            }

        });


        binding.llMore.setOnClickListener (v -> {
            if (finallyPr.getResult().getCheckUser().equalsIgnoreCase("1"))  {
             //   Navigation.findNavController(v).navigate(R.id.action_home_fragment_to_report,null);

            }

        });


        executor = ContextCompat.getMainExecutor(getActivity());

        setPrompt();






        return binding.getRoot ();
    }



    private void initBiometricPrompt( String title, String subtitle, String description, Boolean setDeviceCred){
        if (setDeviceCred) {
            /*For API level > 30
              Newer API setAllowedAuthenticators is used*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // int authFlag =  ;
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(title)
                        .setSubtitle(subtitle)
                        .setDescription(description)
                        .setAllowedAuthenticators(DEVICE_CREDENTIAL | BIOMETRIC_STRONG)
                        .setNegativeButtonText(Constant.CANCEL)
                        .build();
            } else {
                /*SetDeviceCredentials method deprecation is ignored here
                  as this block is for API level<30*/
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(title)
                        .setSubtitle(subtitle)
                        .setDescription(description)
                        .setDeviceCredentialAllowed(true)
                        .build();
            }
        } else {
            promptInfo = new  BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setNegativeButtonText(Constant.CANCEL)
                    .build();
        }

            //  biometricPrompt.authenticate(promptInfo);

            BiometricPrompt.CryptoObject cryptoObject = null;
            try {
                cryptoObject = new BiometricPrompt.CryptoObject(getEncryptCipher(createKey()));
            } catch (NoSuchPaddingException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (NoSuchProviderException e) {
                throw new RuntimeException(e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new RuntimeException(e);
            }
            biometricPrompt.authenticate(promptInfo, cryptoObject);


    }

    private SecretKey createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String provider = "AndroidKeyStore";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
        KeyGenParameterSpec keyGenParameterSpec = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenParameterSpec = new KeyGenParameterSpec.Builder("MY_KEY", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(keyGenParameterSpec);
        }
        // Log.e("ciphrKey=====",keyGenerator.generateKey()+"");
        return keyGenerator.generateKey();
    }

    private Cipher getEncryptCipher(Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = KeyProperties.KEY_ALGORITHM_AES;
        String blockMode = KeyProperties.BLOCK_MODE_CBC;
        String padding = KeyProperties.ENCRYPTION_PADDING_PKCS7;
        Cipher cipher = Cipher.getInstance(algorithm+"/"+blockMode+"/"+padding);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        Log.e("ciphrKey=====",key+"");
        Log.e("ciphrKey2=====",cipher+"");
         this.keyValue = key+"";
        return cipher;
    }




    private void setPrompt() {
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getActivity(),Constant.AUTHENTICATION_ERROR + " " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Toast.makeText(LoginActivity.this, Constant.AUTHENTICATION_SUCCEEDED , Toast.LENGTH_SHORT).show();
                //binding.textViewAuthResult.visibility = View.VISIBLE
               // Toast.makeText(getActivity(), Constant.AUTHENTICATION_SUCCEEDED , Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    updateFingerPrintInServer(keyValue);
                }
                Log.e("authresult=====",result.getAuthenticationType()+"");

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getActivity(), Constant.AUTHENTICATION_FAILED , Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void openSetBudgetInfoDialog(FragmentActivity activity) {
       Dialog mDialog = new Dialog(activity, WindowManager.LayoutParams.MATCH_PARENT);
        mDialog.setCancelable(true);

       DialogBudgetInfoBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity)
                , R.layout.dialog_budget_info, null, false);
        mDialog.setContentView(dialogBinding.getRoot());


        dialogBinding.imgBack.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        dialogBinding.RRContinue.setOnClickListener(v -> {
            mDialog.dismiss();
            NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = navHostFragment.getNavController();
            Bundle bundle = new Bundle();
            navController.navigate(R.id.action_home_fragment_to_setBudget,bundle);
        });

        mDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume ();
        if (sessionManager.isNetworkAvailable ()) {
            binding.progressBar.setVisibility (View.VISIBLE);
            GetProfileMethod();
            GetPaymentReportMethod();
            GetProfileMethod11();
          //  getHomeCat();
            getCounter();

        } else {

            Toast.makeText (getActivity (), R.string.checkInternet, Toast.LENGTH_SHORT).show ();
        }
    }

    private void GetPaymentReportMethod() {
        modelList1.clear();
        //sessionManager.getUserID();
        Log.e("user_id ==",sessionManager.getUserID());
        Log.e("date ==",getCurrentDate());

        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
         .get_vtpass_history_search(sessionManager.getUserID(),"","",getCurrentDate(),getCurrentDate(),"","","");
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                serBarChar(0.0f,0.0f);
            }
        });
    }



    private GetProfileModel.AccountDetail getItem(int position) {
        return modelList.get (position);
    }


    private void GetProfileMethod() {
        Call<GetProfileModel> call = RetrofitClients.getInstance ().getApi ()
            .Api_get_profile_data (sessionManager.getUserID());
        Log.e("user id=====",sessionManager.getUserID());
        call.enqueue (new Callback<GetProfileModel> () {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility (View.GONE);
                try {
                     finallyPr = response.body ();
                     binding.llTab.setVisibility(View.VISIBLE);
                    if (finallyPr.getStatus ().equalsIgnoreCase ("1")) {
                        sessionManager.saveAccountReference(finallyPr.getResult().getBatchId());

                        Log.e("refferece===",finallyPr.getResult().getLastLogin());

                        Log.e("refferece===",finallyPr.getResult().getCheckUser());
                        if(finallyPr.getResult().getCheckUser().equalsIgnoreCase("1")){
                            binding.rlNewUser.setVisibility(View.GONE);
                            binding.llDetails.setVisibility(View.VISIBLE);
                        }
                        else {
                            binding.rlNewUser.setVisibility(View.VISIBLE);
                            binding.llDetails.setVisibility(View.GONE);
                        }
                        binding.txtUser.setText (finallyPr.getResult ().getUserName());
                        binding.txtLastLogin.setText ("Last Login : " + Preference.convertDate22(finallyPr.getResult ().getLastLogin()));
                        // Generate avatar bitmap
                        avatarGenerator = new AvatarGenerator();
                        Bitmap avatarBitmap = avatarGenerator.generateAvatar(finallyPr.getResult ().getUserName(), 200); // Adjust image size as needed
                        binding.ivAvatar.setImageBitmap(avatarBitmap);




                        modelList = (ArrayList<GetProfileModel.AccountDetail>) finallyPr.getAccountDetail ();
                        final int position = 0;
                      //  final GetProfileModel.AccountDetail model = getItem (position);
                       // binding.txtAmt.setText ("₦"+model.getTotal());
                        binding.txtAmt.setText ("₦"+finallyPr.getResult().getReferralBonus());

                        if(Double.parseDouble(finallyPr.getResult().getPaymentWalletOriginal())< 1)  totalBud = String.format("%.2f", Double.parseDouble(finallyPr.getResult().getPaymentWalletOriginal()));
                         else totalBud = Preference.doubleToStringNoDecimal(Double.parseDouble(finallyPr.getResult().getPaymentWalletOriginal()));
                        binding.txtAmt1.setText("₦"+ totalBud);

                            if(Double.parseDouble(finallyPr.getResult().getPaymentWallet())< 1)  ttt = String.format("%.2f", Double.parseDouble(finallyPr.getResult().getPaymentWallet()));
                        else ttt = Preference.doubleToStringNoDecimal(Double.parseDouble(finallyPr.getResult().getPaymentWallet()));
                        binding.tvAailableBal.setText("₦"+ttt);

                     /// For new user Biometric

/*
                        if(Utilitiesss.getInstance().isBiometricHardWareAvailable(getActivity())){
                            if(finallyPr!=null) {
                                if(finallyPr.getResult().getFingerPrintsKey().equalsIgnoreCase(""))
                                    initBiometricPrompt(
                                            Constant.ENABLE_BIOMETRIC_AUTHENTICATION,
                                            Constant.BIOMETRIC_AUTHENTICATION_SUBTITLE,
                                            Constant.BIOMETRIC_AUTHENTICATION_DESCRIPTION,
                                            false
                                    );
                            }
                        }
*/


                       // getAllBudgetGrps(Double.parseDouble(ttt));

                    } else {

                        Toast.makeText (getActivity (), "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
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

    private void updateFingerPrintInServer(String key){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_finger_print(sessionManager.getUserID(),key);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "figure updated Response = " + stringResponse);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), "added successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else {

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    @Override
    public void home(String id, String value) {

    }


    private void getCounter(){
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi()
                .Api_notification_counter(sessionManager.getUserID());
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {

                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Counter Response = " + stringResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        if(!jsonObject.getString("result").equalsIgnoreCase("0")) {
                            binding.tvCounter.setVisibility(View.VISIBLE);
                            binding.tvCounter.setText(jsonObject.getString("result"));
                        }
                        else {
                            binding.tvCounter.setVisibility(View.GONE);

                        }

                    }
                    else {
                        binding.tvCounter.setVisibility(View.GONE);

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}
