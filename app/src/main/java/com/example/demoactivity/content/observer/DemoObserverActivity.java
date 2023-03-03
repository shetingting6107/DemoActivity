package com.example.demoactivity.content.observer;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoactivity.R;

public class DemoObserverActivity extends AppCompatActivity {

    private ContentObserver contentObserver;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_observer);
        if (handler == null) {
            handler = new Handler();
        }
        contentObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Toast.makeText(DemoObserverActivity.this, "observer changed!", Toast.LENGTH_SHORT).show();
            }
        };
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                //注册监听
                getContentResolver().registerContentObserver(uri, true, contentObserver);
            }
        });
    }

    @Override
    protected void onDestroy() {
        getContentResolver().unregisterContentObserver(contentObserver);
        contentObserver = null;
        super.onDestroy();
    }
}
