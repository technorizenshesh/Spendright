package com.my.spendright.utils;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientsNew {

    //SandBox
    private static final String BASE_URL ="https://sandbox.vtpass.com/api/";

    private static RetrofitClientsNew mInstance;
    private Retrofit retrofit;

    private RetrofitClientsNew(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(400, TimeUnit.SECONDS)
                .readTimeout(400, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

     public static synchronized RetrofitClientsNew getInstance(){
        if (mInstance == null){
       mInstance = new RetrofitClientsNew();
        }
        return mInstance;
    }

  public ApiNew getApi(){

   return retrofit.create(ApiNew.class);

  }

}
