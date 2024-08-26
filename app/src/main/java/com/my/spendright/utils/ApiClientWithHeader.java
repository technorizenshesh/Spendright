package com.my.spendright.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.my.spendright.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;





public class ApiClientWithHeader {

    // private static final String BASE_URL ="https://www.lamavietech.ml/spendright/webservice/";
    private static final String BASE_URL ="https://spendright.ng/webservice/";  //     https://spendright.ng/spendright/webservice/
    private static final String BASE_URL1 ="https://spendright.ng/spendright/api/";

    private static ApiClientWithHeader mInstance;
   // private Retrofit retrofit;
    HttpLoggingInterceptor interceptor;
    SessionManager sessionManager;
    static Context context;

/*
    private ApiClientWithHeader(){
        sessionManager = new SessionManager((Activity) context);
      //  interceptor = new HttpLoggingInterceptor();
       // interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
      //  interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Set the desired log level
        httpClientBuilder.addInterceptor(loggingInterceptor);


        OkHttpClient httpClient = httpClientBuilder.build();


*/
/*
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(400, TimeUnit.SECONDS)
                .readTimeout(400, TimeUnit.SECONDS)
                .addNetworkInterceptor(new AddInterceptor())
                .addInterceptor(interceptor).build();
*//*


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiClientWithHeader getInstance(Context context1){
        if (mInstance == null){
            context = context1;
            mInstance = new ApiClientWithHeader();
        }
        return mInstance;
    }

    public Api getApi(){

        return retrofit.create(Api.class);

    }

    */




    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl,Context context1) {
         context = context1;
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Add your interceptor
            httpClient.addInterceptor(new AddInterceptor());

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }





}
