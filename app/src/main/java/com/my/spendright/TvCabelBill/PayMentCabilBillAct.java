package com.my.spendright.TvCabelBill;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.spendright.Broadband.PaymentBillBroadBandAct;
import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.Model.GetMerchatAcocuntTv;
import com.my.spendright.act.LoginActivity;
import com.my.spendright.adapter.ServicesAdapter;
import com.my.spendright.databinding.ActivityPayMentCabilBillBinding;
import com.my.spendright.utils.Preference;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayMentCabilBillAct extends AppCompatActivity {

    ActivityPayMentCabilBillBinding binding;
    private ArrayList<GetServiceElectricialModel.Content> modelListCategory = new ArrayList<>();

    String ServicesSubscriptionId="";
    String ServicesSubscriptionName="";
    private SessionManager sessionManager;

    String myWalletBalace="";

    String subscription_type="Bouquet Renewal";

    String BillNumber="";
    public static String serviceId ="";

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_pay_ment_cabil_bill);

        sessionManager = new SessionManager(PayMentCabilBillAct.this);

        Intent intent=getIntent();

        if(intent!=null)
        {
            myWalletBalace = intent.getStringExtra("Balance");
            binding.txtCurrentBalnce.setText(myWalletBalace);
        }

       binding.imgBack.setOnClickListener(v -> {
           onBackPressed();
       });

        binding.txttype.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(PayMentCabilBillAct.this, binding.txttype);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.purchase_product, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.BouquestChange) {
                        subscription_type="Bouquet Change";
                        binding.txttype.setText(subscription_type);
                    }if (id == R.id.BouquestRenewal) {
                        subscription_type="Bouquet Renewal";
                        binding.txttype.setText(subscription_type);
                    }
                    return true;
                }
            });
            popup.show();
        });

        binding.spinnerSubscription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
                ServicesSubscriptionId = modelListCategory.get(pos).getServiceID();
                serviceId = modelListCategory.get(pos).getServiceID();
                ServicesSubscriptionName = modelListCategory.get(pos).getName();
                if(ServicesSubscriptionName.equalsIgnoreCase("Showmax")){

                    startActivity(new Intent(PayMentCabilBillAct.this, ShowmaxAct.class)
                            .putExtra("Meter_Number",BillNumber+"")
                          //  .putExtra("CustomerName",finallyPr.getContent().getCustomerName()+"")
                         //   .putExtra("CustomerType",finallyPr.getContent().getCustomerType()+"")
                         //   .putExtra("RenewalAmt",finallyPr.getContent().getRenewalAmount()+"")
                            .putExtra("ServicesSubscriptionId",ServicesSubscriptionId+"")
                            .putExtra("ServicesSubscriptionName",ServicesSubscriptionName+"")
                            .putExtra("myWalletBalace",myWalletBalace+"")
                    );



                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.RRPay.setOnClickListener(v -> {

            BillNumber=binding.edtBillNumber.getText().toString();
            if(BillNumber.equalsIgnoreCase(""))
            {
                Toast.makeText(PayMentCabilBillAct.this, "Please Enter Bill number", Toast.LENGTH_SHORT).show();

            }else
            {
                binding.progressBar.setVisibility(View.VISIBLE);
                MerchantAccounCheck();

            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi();
        }else {
            Toast.makeText(PayMentCabilBillAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void ServiceApi() {
        Call<ResponseBody> call = RetrofitClients.getInstance().getApi().Api_service_tv_subscription(Preference.getHeader(PayMentCabilBillAct.this));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    try {
                        // user object available
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);

                        if (jsonObject.getString("response_description").equalsIgnoreCase("000")) {
                            GetServiceElectricialModel finallyPr = new Gson().fromJson(stringResponse, GetServiceElectricialModel.class); // response.body();
                            modelListCategory= (ArrayList<GetServiceElectricialModel.Content>) finallyPr.getContent();
                            ServicesAdapter customAdapter=new ServicesAdapter(PayMentCabilBillAct.this,modelListCategory);
                            binding.spinnerSubscription.setAdapter(customAdapter);
                        }

                        else if(jsonObject.getString("status").equalsIgnoreCase("9")){
                            sessionManager.logoutUser();
                            Toast.makeText(PayMentCabilBillAct.this, getString(R.string.invalid_token), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PayMentCabilBillAct.this, LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }


                        else {
                            Toast.makeText(PayMentCabilBillAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }






                } else {
                    Toast.makeText(PayMentCabilBillAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void MerchantAccounCheck() {

        Log.e("biller_number",BillNumber);
        Log.e("service id",ServicesSubscriptionId);
        Call<GetMerchatAcocuntTv> call = RetrofitClients.getInstance().getApi().Api_merchant_verify_Tv(Preference.getHeader(PayMentCabilBillAct.this),BillNumber,ServicesSubscriptionId);


        call.enqueue(new Callback<GetMerchatAcocuntTv>() {
            @Override
            public void onResponse(@NonNull Call<GetMerchatAcocuntTv> call, @NonNull Response<GetMerchatAcocuntTv> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    GetMerchatAcocuntTv finallyPr = response.body();

                    if(finallyPr.getCode().equalsIgnoreCase("000"))
                    {
                        if(subscription_type.equalsIgnoreCase("Bouquet Renewal"))
                        {

                            try {
                                if(!finallyPr.getContent().getCustomerName().equalsIgnoreCase(""))
                                {
                                    startActivity(new Intent(PayMentCabilBillAct.this, PaymentInformationTvAct.class)
                                            .putExtra("Meter_Number",BillNumber+"")
                                            .putExtra("CustomerName",finallyPr.getContent().getCustomerName()+"")
                                            .putExtra("CustomerType",finallyPr.getContent().getCustomerType()+"")
                                            .putExtra("RenewalAmt",finallyPr.getContent().getRenewalAmount()+"")
                                            .putExtra("ServicesSubscriptionId",ServicesSubscriptionId+"")
                                            .putExtra("ServicesSubscriptionName",ServicesSubscriptionName+"")
                                            .putExtra("myWalletBalace",myWalletBalace+"")
                                    );

                                }
                            }catch (Exception e)
                            {
                                Toast.makeText(PayMentCabilBillAct.this, "Customer Unique Number is Wrong..", Toast.LENGTH_SHORT).show();
                            }

                        }else if(subscription_type.equalsIgnoreCase("Bouquet Change"))
                        {
                            try {
                                if(!finallyPr.getContent().getCustomerName().equalsIgnoreCase(""))
                                {
                                    startActivity(new Intent(PayMentCabilBillAct.this, PaymentInformationTvChangeAct.class)
                                            .putExtra("Meter_Number",BillNumber+"")
                                            .putExtra("CustomerName",finallyPr.getContent().getCustomerName()+"")
                                            .putExtra("CustomerType",finallyPr.getContent().getCustomerType()+"")
                                            .putExtra("RenewalAmt",finallyPr.getContent().getRenewalAmount()+"")
                                            .putExtra("ServicesSubscriptionId",ServicesSubscriptionId+"")
                                            .putExtra("ServicesSubscriptionName",ServicesSubscriptionName+"")
                                            .putExtra("myWalletBalace",myWalletBalace+"")
                                    );

                                }
                            }catch (Exception e)
                            {
                                Toast.makeText(PayMentCabilBillAct.this, "Customer Unique Number is Wrong..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else
                    {
                        Toast.makeText(PayMentCabilBillAct.this,""+finallyPr.getContent().getCustomerName() , Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(PayMentCabilBillAct.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<GetMerchatAcocuntTv> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PayMentCabilBillAct.this, "Please check Network..", Toast.LENGTH_SHORT).show();
            }
        });
    }


}