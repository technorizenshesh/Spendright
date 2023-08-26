package com.my.spendright.Notification;

import static androidx.core.app.NotificationCompat.BADGE_ICON_SMALL;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.my.spendright.R;
import com.my.spendright.act.HomeActivity;
import com.my.spendright.utils.Preference;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    String status ="";
    String Msg ="";
    String amount ="";
    String Title ="";
    String key ="";
    static int count = 0 ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("remote_message>>>", String.valueOf(remoteMessage));

        // log the getting message from firebase
        // Log.d(TAG, "From: " + remoteMessage.getFrom());

        //  if remote message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload" + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleNow(data);
        }

        // if message contains a notification payload.
       /* if (remoteMessage.getNotification() != null) {

            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
            // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

    }




    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // make a own server request here using your http client
    }

    private void handleNow(Map<String, String> data) {
        sendNotification(data.get("status"), data.get("key"), data);
         /* if (data.containsKey("title") && data.containsKey("message")) {
            sendNotification(data.get("title"), data.get("message"));
        } */
    }

    private void sendNotification(String title, String messageBody, Map<String, String> data) {
        JSONObject object = null;
            Log.e("title", "title = " + title);
            Log.e("title", "messageBody = " + messageBody);
            try {
                object = new JSONObject(data.get("message"));
                status = object.optString("result");
                //  = object.optString("type_of_payment");
                Title = object.optString("title"); //messageBody;
                Msg = object.optString("key");
                amount = object.optString("amount");
                //  Intent intent;


                int requestCode = (int) System.currentTimeMillis();


       /*     if (status.equals("Accept")) {
                try {


                    msg = object.getString("key");
                    driver_id = object.getString("driver_id");
                    request_id = object.getString("request_id");
                    driver_imgUrl = object.optString("image");
                    driver_number = object.optString("provider_number");
                    provider_firstname = object.getString("provider_firstname");

                    intent = new Intent(this, DriverInfoActivity.class);

                    intent.putExtra("driver_id", driver_id);
                    intent.putExtra("provider_firstname", provider_firstname);
                    intent.putExtra("request_id", request_id);
                    intent.putExtra("pickup_status", status);
                    intent.putExtra("driver_imgUrl", driver_imgUrl);
                    intent.putExtra("driver_number", driver_number);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            else if (status.equals("Complete")) {

                try {


                    msg = object.getString("key");//  * http://bai-hai.com/webservice/add_rating?
                    // user_id=1&provider_id=1&request_id=1&review=good%20service&rating=5
                    //driver_id = object.getString("driver_id");
                    request_id = object.getString("request_id");
                    intent = new Intent(this, RatingActivity.class);
                    //intent.putExtra("driver_id", driver_id);
                    intent.putExtra("request_id", request_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("excep", String.valueOf(e));

                }


            }
            else if (chatRequestStatus.equals("Chat request")) {

                try {
                    msg = object.getString("key");

                    intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            else if (chatRequestStatus.equals("Your Chat request is Accepted")) {

                try {
                    msg = object.getString("key");

                    intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


            if (Chatresult.equals("successful")) {

                try {
                    title = object.getString("name");
                    msg = object.getString("key");//  * http://bai-hai.com/webservice/add_rating?
                    // user_id=1&provider_id=1&request_id=1&review=good%20service&rating=5
                    //driver_id = object.getString("driver_id");
                    //request_id = object.getString("request_id");
                    intent = new Intent(this, HomeActivity.class);
                    //intent.putExtra("driver_id", driver_id);
                    //intent.putExtra("request_id", request_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("excep", String.valueOf(e));

                }
            }
*/
                // msg = object.getString("key");

                //            Drawable drawable = getApplicationInfo().loadIcon(getPackageManager());
//            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();


                boolean importantShift = Preference.getBool(this, Preference.key_switch_shift_change);

   /*    if(importantShift)
       {*/
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_IMMUTABLE);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.channelId))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(Msg))
                        .setSmallIcon(R.drawable.ic_noti)
                        .setContentTitle(Title)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentText(Msg)
                        // .setContentText(amount)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setVibrate(new long[]{1000, 1000})
                         .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                         .setNumber(count)
                        .setColor(getResources().getColor(R.color.noti_logo_color))
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // Since android Oreo notification channel is needed.


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Channel human readable title
                    NotificationChannel channel = new NotificationChannel(getString(R.string.channelId),
                            "Cloud Messaging Service",
                            NotificationManager.IMPORTANCE_DEFAULT);

                    notificationManager.createNotificationChannel(channel);
                }

                if (!object.getString("userimage").equalsIgnoreCase("")){
                /*    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(object.getString("userimage"))
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    //largeIcon
                                    notificationBuilder.setLargeIcon(resource);
                                    //Big Picture
                                //    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));

                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                }
                            });*/
                    notificationBuilder.setLargeIcon(getBitmapFromURL(object.getString("userimage")));
                    //Big Picture
                      // notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(object.getString("userimage"))));
            }
                else {
                                            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_one));

                }




           notificationManager.notify(getNotificationId(), notificationBuilder.build());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }

            private static int getNotificationId () {
                Random rnd = new Random();
                return 100 + rnd.nextInt(9000);
            }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

