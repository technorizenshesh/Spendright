package com.my.spendright.utils;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {



    public static Retrofit retrofit = null;
    private static ApiClient mInstance;
    public  ApiClient() {

        OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://sandbox.vtpass.com/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();



    }


    public static synchronized ApiClient getInstance(){
        if (mInstance == null){
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    public Api getApi(){

        return retrofit.create(Api.class);

    }


/*
    public static Retrofit getClient1() {
         Retrofit retrofit1 = null;

        if (retrofit1 == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS).build();

            retrofit1 = new Retrofit.Builder()
                    .baseUrl(Constant.FL_SANDBOX_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit1;
    }
*/




}
