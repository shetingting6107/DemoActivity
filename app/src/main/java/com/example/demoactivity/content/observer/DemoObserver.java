package com.example.demoactivity.content.observer;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class DemoObserver extends ContentObserver {
    private static final String TAG = "DemoObserver";

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public DemoObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.d(TAG, "Observer changed.");
        super.onChange(selfChange);
    }
}
