package com.my.spendright.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class SessionManager {

  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;
  private Activity activity;
  private Context context;


  private String PREF_NAME = "myPreference";
  private int PRIVATE_MODE =0;

    public static final String KEY_USER_TOKEN = "keyUserToken";
    public static final String KEY_USER_AUTHKEY = "keyUserAuth";
    public static final String KEY_USER_NAME = "keyUserName";
    public static final String KEY_USER_MOBILE = "keyUserMobile";
    public static final String KEY_USER_ID = "keyUserID";
    public static final String KEY_account_id = "keyAccountId";
    public static final String KEY_USER_EMAIL = "keyUserEmail";

    public static final String KEY_USER_NEWTOKEN = "keyUserToken";

    public static final String KEY_USER_PASS = "keyUserPass";




    public static final String KEY_USER_ADDRESS = "keyUserAddress";
    public static final String KEY_USER_PROFILE_PICTURE = "keyUserProfile";
    public static final String KEY_DEVICE_TOKEN = "keyDeviceToken";
    public static final String KEY_USER_STATUS = "keyUserStatus";
    public static final String KEY_ACCOUNT_REFERENCE = "accountReference";
    public static final String KEY_cate_id = "category_id";

    public static final String KEY_trans_id = "transaction_id";

    public static final String KEY_ele_address = "ele_address";

    public static final String KEY_ele_type = "ele_type";


/*
    public void saveUserToken(String userAuthKey){
    editor = preferences.edit();
    editor.putString(KEY_USER_AUTHKEY, userAuthKey);
    editor.apply();
    }
*/

    public SessionManager(Activity activity){
          this.activity = activity;
          this.context = activity.getApplicationContext();
          preferences = activity.getApplicationContext().getSharedPreferences(PREF_NAME,PRIVATE_MODE);
    }

    private SessionManager(Context context){
    this.context = context;
    preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void createLoginSession(String userID, String fullName, String email, String address, String address1,
                                   String profilePic, String mobile, String deviceToken){

        editor = preferences.edit();
        editor.putString(KEY_USER_ID,userID);
        editor.putString(KEY_USER_NAME, fullName);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ADDRESS, address);
        editor.putString(KEY_USER_STATUS, address1);
        editor.putString(KEY_USER_PROFILE_PICTURE, profilePic);
        editor.putString(KEY_DEVICE_TOKEN, deviceToken);
        editor.putString(KEY_USER_MOBILE, mobile);
        editor.apply();

    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveUserImage(String profilePic){
        editor = preferences.edit();
        editor.putString(KEY_USER_PROFILE_PICTURE, profilePic);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveUserName(String UserName){
        editor = preferences.edit();
        editor.putString(KEY_USER_NAME, UserName);
        editor.apply();
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void getEditData(String fullName, String email){

        editor = preferences.edit();
        editor.putString(KEY_USER_NAME, fullName);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();

    }



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveUserPas(String pass){
        editor = preferences.edit();
        editor.putString(KEY_USER_PASS, pass);
        editor.apply();

    }


    public String getUserPass(){
        String pass = preferences.getString(KEY_USER_PASS,"");
        return pass;
    }



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveUserToken(String token){
        editor = preferences.edit();
        editor.putString(KEY_USER_NEWTOKEN, token);
        editor.apply();

    }


    public String getUserToken(){
        String token = preferences.getString(KEY_USER_NEWTOKEN,"");
        return token;
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveUserId(String id){

        editor = preferences.edit();
        editor.putString(KEY_USER_ID, id);
        editor.apply();

    }

    public String getUserID(){
        String userID = preferences.getString(KEY_USER_ID,"");
        return userID;
    }



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveAccountReference(String accountReference){
        editor = preferences.edit();
        editor.putString(KEY_ACCOUNT_REFERENCE, accountReference);
        editor.apply();

    }

    public String getAccountReference(){
        String accountReference = preferences.getString(KEY_ACCOUNT_REFERENCE,"");
        return accountReference;
    }



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveCateId(String catId){

        editor = preferences.edit();
        editor.putString(KEY_cate_id, catId);
        editor.apply();

    }

    public String getCatId(){
        String catId = preferences.getString(KEY_cate_id,"");
        return catId;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void saveAccountId(String accontId){

        editor = preferences.edit();
        editor.putString(KEY_account_id, accontId);
        editor.apply();

    }

    public String getAccontId(){
        String accontId = preferences.getString(KEY_account_id,"");
        return accontId;
    }



    public void saveTransId(String transId){

        editor = preferences.edit();
        editor.putString(KEY_trans_id, transId);
        editor.apply();

    }

    public String getTransId(){
        String transId = preferences.getString(KEY_trans_id,"");
        return transId;
    }





    public void saveEleAddress(String eleAddress){

        editor = preferences.edit();
        editor.putString(KEY_ele_address, eleAddress);
        editor.apply();

    }

    public String getEleAddress(){
        String eleAddress = preferences.getString(KEY_ele_address,"");
        return eleAddress;
    }


    public void saveEleType(String eleType){

        editor = preferences.edit();
        editor.putString(KEY_ele_type, eleType);
        editor.apply();

    }

    public String getEleType(){
        String eleType = preferences.getString(KEY_ele_type,"");
        return eleType;
    }



    public String getUserName(){
        String userName = preferences.getString(KEY_USER_NAME,"");
        return userName;
    }

    public String getAuthKEy(){
        String authKey = preferences.getString(KEY_USER_AUTHKEY,"");
        return authKey;
    }

    public String getUserEmail(){
        String userEmail = preferences.getString(KEY_USER_EMAIL,"");
        return userEmail;
    }

    public String getUserAddress(){
        String userAddress = preferences.getString(KEY_USER_ADDRESS,"");
        return userAddress;
    }

    public String getUserProfilePicture(){
        String userProfile = preferences.getString(KEY_USER_PROFILE_PICTURE,"");
        return userProfile;
    }

    public String getUserMobile(){
        String mobile = preferences.getString(KEY_USER_MOBILE,"");
        return mobile;
    }

    public String getUserStatus(){

        String userStatus = preferences.getString(KEY_USER_STATUS,"");
        return userStatus;
    }



   public void logoutUser(){
        editor = preferences.edit();
        editor.clear();
        editor.apply();
   }


    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }


    public void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }



}
