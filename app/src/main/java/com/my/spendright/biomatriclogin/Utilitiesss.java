package com.my.spendright.biomatriclogin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.biometric.BiometricManager;

import com.google.android.material.snackbar.Snackbar;
import com.my.spendright.R;

public class Utilitiesss {

    private static final Utilitiesss ourInstance = new Utilitiesss();

    public static Utilitiesss getInstance() {
        return ourInstance;
    }

    private Utilitiesss() {
    }







    public static boolean isBiometricHardWareAvailable(Context context){
        boolean result = false;
        BiometricManager biometricManager = BiometricManager.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                case BiometricManager.BIOMETRIC_SUCCESS :
                    result = true;
                    break;


                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE :
                    result = false;
                    break;

                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE :
                    result = false;
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED :
                    result = false;
                    break;

                case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED :
                    result = true;
                    break;

                case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED :
                    result = true;
                    break;

                case BiometricManager.BIOMETRIC_STATUS_UNKNOWN :
                    result = false;
                    break;
            }
        }

        else {
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS :
                    result = true;
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE :
                    result = false;
                    break;

                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE :
                    result = false;
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED :
                    result = false;
                    break;
            }
        }


        return result;

    }
}
