package com.my.spendright.utils;

import static com.my.spendright.utils.ApiClientWithHeader.context;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.my.spendright.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class  AddInterceptor implements Interceptor {
    SessionManager sessionManager;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        sessionManager = new SessionManager((Activity) context);

        Request.Builder builder = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2RhdGEiOiIyNzkiLCJBUElfVElNRSI6MTcxMjY0MTk1Mn0.6X-CuD7vvrBARocO86QsqxrzWUnDdEWtCqiXzX40iLU")
                .addHeader("Api-key-spendright", "vz0vKvt8VqbzQXcQF294eDJEtdvVr9wttVL93WrBt1BRZi7vQ4Lv0PZdfgQK3qgNuhtjDntqNDnue4nb39C6J7enB2BUg1JgxJUCBqMqfjz82LLezxXcQG0piyKD9uixaSQyE3tMj70pp5rMxxNyP0cKbL7RRHJTUS9NAHY51Bzqp4Kbu2zQN3Y2TA0u9p3Ky9TgkDHBdzEaUDc9Tqevnb6F0V9WeqUHtQqbyS1rkS53Q10C2hDpPwf9AvJ6kd1PHUwPFtLepQehuZ8c8imin7CHj1ZnfwW3BwaSPRLGdC3t")
                .addHeader("Api-secret-spendright", "xzmkkrPHRgVf5YGeXWrW7wq0rdHkS6Khm8u7bNMGhy3mfDEDwrvDT6rPSbJtHP6LTRQw0AHSttPEjKkASDpmcwiZV3Xm7zivBAiVP2WdDL1rNy3bQGEChwDx6a13eiXTxnBVR447QeWJraQ9rP78mAbLCNp29BUy8A1fu2Zb5ESf9Ychwkt3bfVwgz9PXA1jgBQbA2FeXcApSNR9C7PyXk9wM3gDXWQpDZ8kPcd0FezB0HQVDz1pUHxhAHzvhgnqJASRwRzVFZwC9cKUGwDq4mrQ0yE8M8SZzyWTHXHwjcGG");
        Log.e("TTT access_token", sessionManager.getUserToken());
        Log.e("TTT api key", BuildConfig.ApiKey);
        Log.e("TTT secret",BuildConfig.ApiSecret);

        return chain.proceed(builder.build());
    }
}

