package app.zingo.com.billgenerate.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import app.zingo.com.billgenerate.Activiies.RoomBookingNotifyActivity;
import app.zingo.com.billgenerate.Utils.PreferenceHandler;
import app.zingo.com.billgenerate.Activiies.NotificationListActivity;
import app.zingo.com.billgenerate.R;


/**
 * Created by ZingoHotels Tech on 19-03-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> map = remoteMessage.getData();

        sendPopNotification(notification.getTitle(), notification.getBody(), map);


    }
    private void sendPopNotification(String title, String body, Map<String, String> map) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = null;
        String message="";

        Uri sound;


            message = body;
            if(title.contains("New Booking from Zingo Hotels"))
            {
                sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/good_morning");
                intent = new Intent(this, RoomBookingNotifyActivity.class);
                System.out.println("inside got RoomBookingNotifyActivity");

            }
            else
            {
                sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/good_morning");
                //intent = new Intent(this, RoomBookingNotifyActivity.class);
                intent = new Intent(this, NotificationListActivity.class);
                System.out.println("inside got NotificationListActivity");
            }


        //intent = new Intent(this, RoomBookingNotifyActivity.class);
        intent.putExtra("Title",title);
        intent.putExtra("Message",body);
        //  Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/good_morning");
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int notifyID = 1;
        String CHANNEL_ID = ""+ PreferenceHandler.getInstance(MyFirebaseMessagingService.this).getUserId();// The id of the channel.
        CharSequence name = ""+ PreferenceHandler.getInstance(MyFirebaseMessagingService.this).getUserName();// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this)
                    .setTicker(title).setWhen(0)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setFullScreenIntent(pendingIntent,true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setContentInfo(title)
                    .setLargeIcon(icon)
                    .setChannelId("1")
                    .setPriority(Notification.PRIORITY_MAX)

                    // .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher);
        }else{
            notificationBuilder = new Notification.Builder(this)
                    .setTicker(title).setWhen(0)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setFullScreenIntent(pendingIntent,true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setContentInfo(title)
                    .setLargeIcon(icon)
                    .setPriority(Notification.PRIORITY_MAX)

                    // .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher);
        }

        try {
            String picture_url = map.get("picture_url");
            //String picture_url = "https://travel.jumia.com/blog/wp-content/uploads/2016/09/Hotel-booking-iStock_000089313057_Medium-940x529-660x400.jpg";
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                //notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(body));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(m, notificationBuilder.build());

        System.out.println("Bill title = "+title);
    }



    @Override
    public void handleIntent(Intent intent) {
        try
        {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet())
                {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            }
            else
            {
                super.handleIntent(intent);
            }
        }
        catch (Exception e)
        {
            super.handleIntent(intent);
        }
    }
}