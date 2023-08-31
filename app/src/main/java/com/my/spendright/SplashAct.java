package com.my.spendright;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.my.spendright.act.WelcomeActivity;
import com.my.spendright.databinding.ActivityMainBinding;

public class SplashAct extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(SplashAct.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashAct.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}
                        ,101);
            }
            else {

                handlerMethod();
            }
        }
        else handlerMethod();

    }

    private void handlerMethod() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {

                Intent intent=new Intent(SplashAct.this, WelcomeActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);
    }

}