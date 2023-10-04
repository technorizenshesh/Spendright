package com.my.spendright;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.my.spendright.act.WelcomeActivity;
import com.my.spendright.databinding.ActivityMainBinding;
import com.my.spendright.utils.InAppUpdate;




public class SplashAct extends AppCompatActivity {

    ActivityMainBinding binding;
    private static int UPDATE_FROM_PLAY_STORE_REQUEST = 2;

    private InAppUpdate inAppUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);




      //  inAppUpdate = new InAppUpdate(SplashAct.this);
     //   inAppUpdate.checkForAppUpdate();

    }








    public void checkVersion(){
        int versionName= 0;
        try {
            versionName =  getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        int finalVersionName = versionName;
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                Log.e("check====",appUpdateInfo.availableVersionCode()+"      "  + finalVersionName );
                if(finalVersionName < appUpdateInfo.availableVersionCode()){
                    showDialogToSendToPlayStore();
                }
                else  handlerMethod();

            } else handlerMethod();
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inAppUpdate.onActivityResult(requestCode, resultCode);
        Log.e("code=====",resultCode+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   inAppUpdate.onResume();

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(SplashAct.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashAct.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}
                        , 101);
            } else {
                checkVersion();
            }
        } else  {
            checkVersion();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  inAppUpdate.onDestroy();
    }





    private void showDialogToSendToPlayStore() {

                new AlertDialog.Builder(SplashAct.this)
                        .setTitle("Alert")
                        .setMessage("Update Is Available.Do You Want To Update?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID))); // UPDATE_FROM_PLAY_STORE_REQUEST

                                        } catch (Exception e) {
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID))); // UPDATE_FROM_PLAY_STORE_REQUEST
                                            } catch (Exception e1) {

                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                handlerMethod();

                            }
                        }).show();




    }


    private void handlerMethod() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashAct.this, WelcomeActivity.class);
                startActivity(intent);
                finish();

            }
        }, 3000);
    }

















    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean postNotification = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    ;
                    if (postNotification ) {
                        checkVersion();
                    } else {
                        Toast.makeText(SplashAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SplashAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }



    }
