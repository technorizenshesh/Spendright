package com.my.spendright.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public static String doubleToStringNoDecimalSecond(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        if(d==0) d = 0.00;
        else formatter.applyPattern("#,###");
        return formatter.format(d);
    }



    public static String encodeEmoji (String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }


    public static String decodeEmoji (String message) {
        String myString= null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public static String getCurrentDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String todayString = formatter.format(todayDate);
        return todayString;

    }

    public static Date parseDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");  // Preference.getCurrentDate()
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return strDate;
    }

    public static long parseDateCopy(String date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");  // Preference.getCurrentDate()
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.setTime(strDate);
        return calendar.getTimeInMillis();
    }


    public static String convertDate(String dd){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date dt = null;
                String date2;
        try {
            dt = format.parse(dd);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        SimpleDateFormat your_format = new SimpleDateFormat("dd MMM yyyy");

        date2 = your_format.format(dt);
        return  date2;
    }



    public static ArrayList<String> getCurrentDaily(){
        ArrayList<String>dailList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
       // cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 1; i++) {
            dailList.add( sdf.format(cal.getTime()));
            Log.e("dateTag", sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return dailList;
    }


    public static ArrayList<String> getCurrentWeek(){
        ArrayList<String>weeklist = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 7; i++) {
            weeklist.add( sdf.format(cal.getTime()));
            Log.e("dateTag", sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return weeklist;
    }

    public static ArrayList<String> getCurrentMonth(){
        ArrayList<String>monthList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 30; i++) {
            monthList.add( sdf.format(cal.getTime()));
            Log.e("dateTag", sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return monthList;
    }

    public static ArrayList<String> getCurrentYear(){
        ArrayList<String>yearList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 365; i++) {
            yearList.add( sdf.format(cal.getTime()));
            Log.e("dateTag", sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return yearList;
    }



   public static String getAlphaNumericString(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        Log.e("refferece====",sb.toString());

        return sb.toString();
    }


}
