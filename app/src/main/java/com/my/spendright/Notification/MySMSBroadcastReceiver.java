package com.my.spendright.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySMSBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "MySMSBroadcastReceiver";

    onSmsReadListener listener;
    public MySMSBroadcastReceiver( onSmsReadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        Log.e("sms=====", sender);
                        // Check if the sender's address matches your OTP sender
                      /*  if (sender.contains("your-otp-sender-address")) {
                            String messageBody = smsMessage.getMessageBody();
                            // Extract the OTP using regex or other methods
                            // Update UI or handle the OTP as required
                            handleReceivedOTP(messageBody);
                        }*/
                    }
                }
            }
        }
    }

    private void handleReceivedOTP(String messageBody) {
        // Extract OTP from messageBody and update UI
        // Example regex to extract OTP (adjust according to your OTP format)
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            String otp = matcher.group(0);
            // Assuming you have an EditText named otpEditText
          //  otpEditText.setText(otp);
            listener.onSms(otp);
        }
    }


    public interface onSmsReadListener {
        void onSms(String otp);
    }
}