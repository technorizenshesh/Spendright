package com.my.spendright.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Preference {

public static final String APP_PREF = "KapsiePreferences";

public static SharedPreferences sp;
public static String KEY_VTPASS_UserName = "VtpassUname";
public static String KEY_VTPASS_pass = "VtpassPass";
public static String key_switch_shift_change = "shift_change";

private Activity activity;
private Context context;

public Preference(Activity activity){
    this.activity = activity;
    this.context = activity.getApplicationContext();
}

//-----------------------------------


public boolean isNetworkAvailable()
{
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    return ni != null;
}

public static String get(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    String userId = sp.getString(key, "0");
    return userId;
}
public static void save(Context context, String key, String value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putString(key, value);
    edit.commit();
}

public static void saveInt(Context context, String key, int value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putInt(key, value);
    edit.commit();
}
public static void saveFloat(Context context, String key, Float value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putFloat(key, value);
    edit.commit();
}

public static int getInt(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    int userId = sp.getInt(key,0);
    return userId;
}

public static Float getFloat(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    Float userId = sp.getFloat(key,0);
    return userId;
}


public static boolean saveBool(Context context, String key, Boolean value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putBoolean(key, value);
    edit.commit();
    return false;
}

public static Boolean getBool(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    return sp.getBoolean(key,false);
}

public static void clearPreference(Context context) {
    sp = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = sp.edit();
    edit.clear();
    edit.apply();
}


    public static String doubleToStringNoDecimal(double d) {
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        if(d==0) d = 0.00;
        else formatter.applyPattern("#,###.00");
        return formatter.format(d);
    }

}
