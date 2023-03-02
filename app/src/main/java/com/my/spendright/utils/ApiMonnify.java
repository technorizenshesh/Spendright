package com.my.spendright.utils;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiMonnify {
    @POST("login")
    Call<ResponseBody> Api_generate_monnify_token(@Header("Authorization") String auth);


    @POST("reserved-accounts")
    Call<ResponseBody> Api_generate_account1(@HeaderMap Map<String,String> header, @Body Map<String, String> body);


  @GET("reserved-accounts/{accountReference}")
  Call<ResponseBody> Api_get_account1(@HeaderMap Map<String,String> header, @Path("accountReference") String accountReference);


/*  @POST("bulk-virtual-account-numbers")
    Call<ResponseBody> Api_generate_account(@HeaderMap Map<String,String> header, @Body Map<String, String> body);*/

    @POST("virtual-account-numbers")
    Call<ResponseBody> Api_generate_account(@HeaderMap Map<String,String> header, @Body Map<String, String> body);


   /* @GET("bulk-virtual-account-numbers/{batch_id}")
    Call<ResponseBody> Api_get_account(@HeaderMap Map<String,String> header, @Path("batch_id") String accountReference);*/


    @GET("virtual-account-numbers/{order_ref}")
    Call<ResponseBody> Api_get_account(@HeaderMap Map<String,String> header, @Path("order_ref") String accountReference);


}
