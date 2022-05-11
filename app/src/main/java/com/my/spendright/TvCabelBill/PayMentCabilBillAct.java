package com.my.spendright.TvCabelBill;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.my.spendright.ElectircalBill.Model.GetMerchatAcocunt;
import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.ElectircalBill.PaymentBill;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.R;
import com.my.spendright.TvCabelBill.Model.GetMerchatAcocuntTv;
import com.my.spendright.act.PaymentInformation;
import com.my.spendright.adapter.ServicesAdapter;
import com.my.spendright.databinding.ActivityPayMentCabilBillBinding;
import com.my.spendright.utils.ApiNew;
import com.my.spendright.utils.SessionManager;

import java.util.ArrayList;

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

    String subscription_type="Bouquest Renewal";

    String BillNumber="1212121212";

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
                        subscription_type="Bouquest Change";
                        binding.txttype.setText(subscription_type);
                    }if (id == R.id.BouquestRenewal) {
                        subscription_type="Bouquest Renewal";
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
                ServicesSubscriptionName = modelListCategory.get(pos).getName();
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
                MerchantAccounCheck("harshit.ixora89@gmail.com","harshit89@");

            }
        });

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi("harshit.ixora89@gmail.com","harshit89@");
        }else {
            Toast.makeText(PayMentCabilBillAct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    private void ServiceApi(final String username, final String password) {
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetServiceElectricialModel> call = loginService.Api_service_tv_subscription();
        call.enqueue(new Callback<GetServiceElectricialModel>() {
            @Override
            public void onResponse(@NonNull Call<GetServiceElectricialModel> call, @NonNull Response<GetServiceElectricialModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    GetServiceElectricialModel finallyPr = response.body();
                    modelListCategory= (ArrayList<GetServiceElectricialModel.Content>) finallyPr.getContent();

                    ServicesAdapter customAdapter=new ServicesAdapter(PayMentCabilBillAct.this,modelListCategory);
                    binding.spinnerSubscription.setAdapter(customAdapter);

                } else {
                    Toast.makeText(PayMentCabilBillAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetServiceElectricialModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void MerchantAccounCheck(final String username, final String password) {
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetMerchatAcocuntTv> call = loginService.Api_merchant_verify_Tv(BillNumber,ServicesSubscriptionId);
        call.enqueue(new Callback<GetMerchatAcocuntTv>() {
            @Override
            public void onResponse(@NonNull Call<GetMerchatAcocuntTv> call, @NonNull Response<GetMerchatAcocuntTv> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    GetMerchatAcocuntTv finallyPr = response.body();

                    if(finallyPr.getCode().equalsIgnoreCase("000"))
                    {
                        if(subscription_type.equalsIgnoreCase("Bouquest Renewal"))
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


                        }else
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
            }
        });
    }


}