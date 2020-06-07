package rm.android.words;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class NotificationHelper {
    public static String POSITIVE_CLICK = "POSITIVE_CLICK";
    public static String NEGATIVE_CLICK = "NEGATIVE_CLICK";
    public static String REPLY_CLICK = "REPLY_CLICK";
    public static String REPLY_TEXT_KEY = "REPLY_TEXT_KEY";
    public static String GROUP_KEY = "GROUP_KEY";


    public static void createNotification(Context ctx, String title, String text, String _id, String color) {
        Log.d("NotificationHelper", "onMessageReceived createNotification");
        int notificationId = new Random().nextInt();

        Intent intent = new Intent(ctx, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, 0);

        Intent positive = new Intent(ctx, NotificationReceiver.class);
        positive.putExtra("notiID", notificationId);
        positive.putExtra("_id", _id);
        positive.setAction(NotificationHelper.POSITIVE_CLICK);

        PendingIntent pIntent_positive = PendingIntent.getBroadcast( ctx, notificationId, positive, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent negative = new Intent(ctx, NotificationReceiver.class);
        negative.putExtra("notiID", notificationId);
        negative.setAction(NotificationHelper.NEGATIVE_CLICK);

        PendingIntent pIntent_negative = PendingIntent.getBroadcast(ctx, notificationId, negative, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, "notify_001")
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setStyle(new NotificationCompat.BigTextStyle())
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setColor(Color.parseColor(color))
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .addAction(new NotificationCompat.Action(R.drawable.save, "Got it!", pIntent_positive))
            .addAction(new NotificationCompat.Action(R.drawable.save, "Remind", pIntent_negative))
            .setAutoCancel(true)
            .setContentIntent(pIntent);

        notificationBuilder.setContentIntent(pIntent);
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_nr_2";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(notificationId, notificationBuilder.build());
    }
}


/*

    public static NotificationCompat.Builder createNotificationBuider(Context context, String title,
                                                                      String message, int smallIcon) {
        return createNotificationBuider(context, title, message, smallIcon, null, 0, true, null);
    }

    public static NotificationCompat.Builder createNotificationBuider(Context context, String title,
                                                                      String message, int smallIcon, PendingIntent contentIntent) {
        return createNotificationBuider(context, title, message, smallIcon, null, 0, true, contentIntent);
    }

    public static NotificationCompat.Builder createNotificationBuider(Context context, String title, String message,
                                                                      int smallIcon, Bitmap largeIcon, int colorID,
                                                                      boolean isAutoCancel, PendingIntent contentIntent) {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setAutoCancel(isAutoCancel);
            builder.setGroup(GROUP_KEY);

            if (!TextUtils.isEmpty(title))
                builder.setContentTitle(title);
            if (!TextUtils.isEmpty(message))
                builder.setContentText(message);
            if (smallIcon != 0)
                builder.setSmallIcon(smallIcon);
            if (largeIcon != null)
                builder.setLargeIcon(largeIcon);
            if (contentIntent != null)
                builder.setContentIntent(contentIntent);

            return builder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void showNotification(Context context, int notiID, Notification notification) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notiID, notification);
    }

 */