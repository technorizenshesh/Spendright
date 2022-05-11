package com.my.spendright.ElectircalBill;

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
import com.my.spendright.ElectircalBill.Model.GetService;
import com.my.spendright.ElectircalBill.Model.GetServiceElectricialModel;
import com.my.spendright.ElectircalBill.Model.GetVtsWalletBalnce;
import com.my.spendright.ElectircalBill.UtilRetro.RetrofitSetup;
import com.my.spendright.Model.GetAccountCategory;
import com.my.spendright.R;
import com.my.spendright.act.AddActivity;
import com.my.spendright.act.PaymentInformation;
import com.my.spendright.act.VtpassActivityLogin;
import com.my.spendright.adapter.CategoryAdapter;
import com.my.spendright.adapter.ServicesAdapter;
import com.my.spendright.databinding.ActivityPaymentBillBinding;
import com.my.spendright.utils.ApiNew;
import com.my.spendright.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentBill extends AppCompatActivity {

    ActivityPaymentBillBinding binding;
      String type="prepaid";
    String BillNumber="1111111111111";

    private ArrayList<GetServiceElectricialModel.Content> modelListCategory = new ArrayList<>();
    String ServicesId="";
    String ServicesName="";
    String miniAmt="";
    String maxAmt="";
    String myWalletBalace="";
    private SessionManager sessionManager;
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payment_bill);

        sessionManager = new SessionManager(PaymentBill.this);

        Intent intent=getIntent();

        if(intent!=null)
        {
             myWalletBalace = intent.getStringExtra("Balance");
            binding.txtCurrentBalnce.setText(myWalletBalace);
        }

        binding.txttype.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(PaymentBill.this, binding.txttype);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.poupup_prepsot, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.Prepaid) {
                        type="prepaid ";
                        binding.txttype.setText("prepaid");
                    }if (id == R.id.Postpaid) {
                        type="postpaid";
                        binding.txttype.setText("postpaid");
                    }
                    return true;
                }
            });
            popup.show();
        });

        binding.RRPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    BillNumber=binding.edtBillNumber.getText().toString();
                    if(BillNumber.equalsIgnoreCase(""))
                    {
                        Toast.makeText(PaymentBill.this, "Please Enter Bill number", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        binding.progressBar.setVisibility(View.VISIBLE);

                        MerchantAccounCheck("harshit.ixora89@gmail.com","harshit89@");

                    }

                    //startActivity(new Intent(PaymentBill.this, PaymentInformation.class));
                }
            }
        });


        binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){
              ServicesId = modelListCategory.get(pos).getServiceID();
              ServicesName = modelListCategory.get(pos).getName();
              miniAmt= modelListCategory.get(pos).getMinimiumAmount();
              maxAmt= modelListCategory.get(pos).getMaximumAmount();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ServiceApi("harshit.ixora89@gmail.com","harshit89@");
        }else {
            Toast.makeText(PaymentBill.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


    }


    private void MerchantAccounCheck(final String username, final String password) {
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetMerchatAcocunt> call = loginService.Api_merchant_verify(BillNumber,ServicesId,type);
        call.enqueue(new Callback<GetMerchatAcocunt>() {
            @Override
            public void onResponse(@NonNull Call<GetMerchatAcocunt> call, @NonNull Response<GetMerchatAcocunt> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available
                    GetMerchatAcocunt finallyPr = response.body();
                    Toast.makeText(PaymentBill.this, ""+finallyPr.getCode(), Toast.LENGTH_SHORT).show();
                    if(!finallyPr.getContent().getCustomerName().equals(""))
                    {
                        startActivity(new Intent(PaymentBill.this, PaymentInformation.class)
                                .putExtra("Customer_Name",finallyPr.getContent().getCustomerName()+"")
                                .putExtra("Meter_Number",finallyPr.getContent().getMeterNumber()+"")
                                .putExtra("ServicesId",ServicesId)
                                .putExtra("ServicesName",ServicesName)
                                .putExtra("type",type)
                                .putExtra("myWalletBalace",myWalletBalace)
                                .putExtra("miniAmt",miniAmt)
                                .putExtra("maxAmt",maxAmt)
                                .putExtra("Address",finallyPr.getContent().getAddress()+""));

                    }else
                    {
                        Toast.makeText(PaymentBill.this,""+finallyPr.getContent().getCustomerName() , Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(PaymentBill.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<GetMerchatAcocunt> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void ServiceApi(final String username, final String password) {
        ApiNew loginService =
                RetrofitSetup.createService(ApiNew.class, username, password);
        Call<GetServiceElectricialModel> call = loginService.Api_service_electricity_bill();
        call.enqueue(new Callback<GetServiceElectricialModel>() {
            @Override
            public void onResponse(@NonNull Call<GetServiceElectricialModel> call, @NonNull Response<GetServiceElectricialModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // user object available

                    GetServiceElectricialModel finallyPr = response.body();
                    modelListCategory= (ArrayList<GetServiceElectricialModel.Content>) finallyPr.getContent();

                    ServicesAdapter customAdapter=new ServicesAdapter(PaymentBill.this,modelListCategory);
                    binding.spinnerService.setAdapter(customAdapter);


                } else {
                    Toast.makeText(PaymentBill.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<GetServiceElectricialModel> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}