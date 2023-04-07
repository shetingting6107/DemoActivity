package com.example.demoactivity.xLog;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.flattener.Flattener;
import com.elvishew.xlog.flattener.Flattener2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyFlattener implements Flattener, Flattener2 {

    @Override
    public CharSequence flatten(int logLevel, String tag, String message) {
        return flatten(System.currentTimeMillis(), logLevel, tag, message);
    }

    @Override
    public CharSequence flatten(long timeMillis, int logLevel, String tag, String message) {
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(timeStamp);
        String time = simpleDateFormat.format(date);
        return time
                + '|' + LogLevel.getLevelName(logLevel)
                + '|' + tag
                + '\n' + message;
    }
}
