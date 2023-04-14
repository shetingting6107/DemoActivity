package com.example.demoactivity.intent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

public class TestService extends Service {

    private static Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context) {
        TestService.context = context;
        Intent intent = new Intent(context, TestService.class);
        context.startService(intent);
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
        timer = new Timer("TestServer");
        timer.schedule(task, 200, 5 * 1000);
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel("21", "wormhole", NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(), "21").build();
                startForeground(21, notification);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TESTSERVICE", "service is onDestroy");
        timer.cancel();
        timer = null;
    }
}
