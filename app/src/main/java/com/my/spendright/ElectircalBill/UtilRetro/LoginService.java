package com.my.spendright.ElectircalBill.UtilRetro;

import com.my.spendright.ElectircalBill.Model.GetVtsWalletBalnce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.POST;

public interface LoginService {
    String Api_balance = "balance";

    @POST("/login")
    Call<GetVtsWalletBalnce> basicLogin();
}
