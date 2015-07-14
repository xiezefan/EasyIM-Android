package me.xiezefan.easyim.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import java.util.Map;

import me.xiezefan.easyim.R;


/**
 * Notification Helper
 * Created by XieZeFan on 2015/4/26 0026.
 */
public class PromptHelper {

    public void vibration(Context context) {
        if (SPHelper.getBoolean(SPHelper.NOTIFICATION_VIBRATION, false)) {
            Vibrator mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
            mVibrator.vibrate(new long[] {100,400,100,400}, -1);
        }
    }

    public void prompt(Context context) {
        if (SPHelper.getBoolean(SPHelper.NOTIFICATION_VOICE, true)) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            RingtoneManager.getRingtone(context, notification).play();
        }
    }

    public void showBigText(Context context, int notificationId, String title, String text, Intent resultIntent) {
        showBigText(context, notificationId, title + ":" + text, title, text, text, resultIntent);
    }

    public void showBigText(Context context, int notificationId,
                                   String ticker, String title,
                                   String miniText, String bigText,
                                   Intent resultIntent) {

        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                context, notificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(miniText)
                .setTicker(ticker)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setAutoCancel(true);

        builder.setContentIntent(contentPendingIntent);
        showNotification(context, notificationId, builder);
    }

    private void showNotification(Context context,int notificationId, NotificationCompat.Builder builder) {
        NotificationManager mNotifyMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        if (SPHelper.getBoolean(SPHelper.NOTIFICATION_VIBRATION, true)) {
            notification.vibrate = new long[] {100,400,100,400};
        }
        if (SPHelper.getBoolean(SPHelper.NOTIFICATION_VIBRATION, true)) {
            notification.sound=Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +R.raw.ping_8);
        }

        notification.ledARGB = Color.GREEN;//led灯颜色
        notification.ledOffMS = 1000;//关闭时间 毫秒
        notification.ledOnMS = 1000;//开启时间 毫秒
        notification.flags|=Notification.FLAG_SHOW_LIGHTS;
        mNotifyMgr.notify(notificationId, notification);
    }


}
