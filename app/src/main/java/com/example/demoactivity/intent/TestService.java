package com.example.demoactivity.intent;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.demoactivity.R;

import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class TestService extends Service {

    private static Context context;

    private final static int NOTIFICATION_ID = 10001;
    private static final String CHANNEL_ONE_ID = "EMM_CHANNEL_ONE_ID";
    private static final String CHANNEL_ONE_NAME = "EMM_CHANNEL_ONE_ID";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context) {
        TestService.context = context;
        Intent intent = new Intent(context, TestService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }else {
            context.startService(intent);
        }
    }

    private Timer timer;

    private boolean toSecond = true;

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent intent ;
            if (toSecond) {
                Log.i("TESTSERVICE", "intent to second");
                intent = new Intent(context, SecondActivity.class);
                toSecond = false;
            }else {
                Log.i("TESTSERVICE", "intent to third");
                intent = new Intent(context, ThirdActivity.class);
                toSecond = true;
            }
            context.startActivity(intent);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TESTSERVICE", "service is start");
//        timer = new Timer("TestServer");
//        timer.schedule(task, 200, 5 * 1000);
//        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channel = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                channel = new NotificationChannel("21", "wormhole", NotificationManager.IMPORTANCE_LOW);
//                notificationManager.createNotificationChannel(channel);
//                Notification notification = new Notification.Builder(getApplicationContext(), "21").build();
//                startForeground(21, notification);
//            }
//        }
        // 绑定前台服务
        NotificationChannel notificationChannel = null;
        //进行8.0的判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        Notification.Builder builder = new Notification.Builder(this);
        Intent handleIntent = new Intent(this, LauncherActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, handleIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(String.format("%s---正在运行", this.getString(R.string.app_name)))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ONE_ID);
        }
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TESTSERVICE", "service is onDestroy");
//        timer.cancel();
//        timer = null;
    }

    /**
     * 启动前台服务
     */
    private void startForeground() {
        String channelId = null;
        // 8.0 以上需要特殊处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel("kim.hsl", "ForegroundService");
        } else {
            channelId = "";
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        Notification notification = builder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1, notification);
    }

    /**
     * 创建通知通道
     * @param channelId
     * @param channelName
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }
}
