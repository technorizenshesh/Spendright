package com.my.spendright.act.ui.home.virtualcards;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.my.spendright.Model.GetProfileModel;
import com.my.spendright.R;
import com.my.spendright.act.KYCAct;
import com.my.spendright.databinding.ActivityCreateVirtualBinding;
import com.my.spendright.utils.RetrofitClients;
import com.my.spendright.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateVirtualAct extends AppCompatActivity implements CreateVirtualListener{
    ActivityCreateVirtualBinding binding;
    SessionManager sessionManager;
    GetProfileModel finallyPr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_virtual);
        sessionManager = new SessionManager(CreateVirtualAct.this);

        initViews();
    }

    private void initViews() {

        binding.imgBack.setOnClickListener(view -> finish());


        binding.RRCreateVirtualCard.setOnClickListener(view ->{

            if(finallyPr!=null){
                if(finallyPr.getResult().getKycStatus().equalsIgnoreCase("0")){
                    startActivity(new Intent(this, KYCAct.class)
                            .putExtra("user_id",finallyPr.getResult().getId())
                            .putExtra("mobile", finallyPr.getResult().getMobile())
                            .putExtra("name",finallyPr.getResult().getLastName()+ finallyPr.getResult().getOtherLegalName())
                            .putExtra("from","VirtualCardScreen"));
                }
                else {
                    new CreateVirtualBottomSheet().callBack(this::onVirtual).show(getSupportFragmentManager(),"");
                }
            }
        });

        binding.RRAllVirtualCard.setOnClickListener(view ->{

            if(finallyPr!=null){
                if(finallyPr.getResult().getKycStatus().equalsIgnoreCase("0")){
                    startActivity(new Intent(this, KYCAct.class)
                            .putExtra("user_id",finallyPr.getResult().getId())
                            .putExtra("mobile", finallyPr.getResult().getMobile())
                            .putExtra("name",finallyPr.getResult().getLastName()+ finallyPr.getResult().getOtherLegalName())
                            .putExtra("from","VirtualCardScreen"));
                }
                else {
                    startActivity(new Intent(this,MyVirtualCardAct.class));
                }
            }
        });


    }

    @Override
    public void onVirtual(String data, String tag) {
        if(data.equals("1")) startActivity(new Intent(CreateVirtualAct.this,CardCreateSuccessAct.class));
        else {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetProfileMethod();
    }

    private void GetProfileMethod() {
        binding.progressBar.setVisibility (View.VISIBLE);
        Call<GetProfileModel> call = RetrofitClients.getInstance ().getApi ()
                .Api_get_profile_data (sessionManager.getUserID ());
        call.enqueue (new Callback<GetProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                binding.progressBar.setVisibility (View.GONE);
                try {
                    finallyPr = response.body ();

                    if (finallyPr.getStatus ().equalsIgnoreCase ("1")) {
                        Log.e("refferece===",finallyPr.getResult()+"");

                    } else {

                        Toast.makeText (CreateVirtualAct.this, "" + finallyPr.getMessage (), Toast.LENGTH_SHORT).show ();
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
                //   binding.RRadd.setVisibility (View.VISIBLE);
            }
        });

    }

}

