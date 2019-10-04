package com.togo.c_sms.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.togo.c_sms.Activity.MainActivity;
import com.togo.c_sms.Activity.NotificationActivity;
import com.togo.c_sms.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseMsgSrvice extends FirebaseMessagingService {
    private static final  String TAG = "PushApp";
    Bitmap bitmap;
    @Override
    public void onNewToken(String s) {
        Log.d(TAG,s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null){
            Log.d(TAG,remoteMessage.getNotification().getBody());

            String imageUri = remoteMessage.getData().get("image");
            String TrueOrFlase = remoteMessage.getData().get("NotificationActivity");

            bitmap = getBitmapfromUrl(imageUri);

            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),bitmap,TrueOrFlase);
        }
    }

    private void sendNotification(String body, String title,Bitmap image,String TrueOrFalse) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("NotificationActivity", TrueOrFalse);
        PendingIntent  pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      //  Uri sounduri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(image)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
