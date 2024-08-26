package com.my.spendright.utils;

import android.content.Context;

public class ContextSingleton {
    private static ContextSingleton instance;
    private Context applicationContext;

    private ContextSingleton(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    public static synchronized ContextSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new ContextSingleton(context);
        }
        return instance;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }
}
