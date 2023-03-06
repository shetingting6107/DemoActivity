package com.example.demoactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.demoactivity.accessibility.ui.AccessibilityActivity;
import com.example.demoactivity.content.observer.DemoObserverActivity;
import com.example.demoactivity.content.resolver.ContentResolverActivity;
import com.example.demoactivity.xLog.XlogActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPermission();
    }

    private void initView() {
        TextView tc_acc = findViewById(R.id.tv_accessibility);
        tc_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccessibilityActivity.class);
                startActivity(intent);
            }
        });

        TextView tv_xlog = findViewById(R.id.tv_xlog);
        tv_xlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, XlogActivity.class);
                startActivity(intent);
            }
        });
        TextView tv_content = findViewById(R.id.tv_content);
        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DemoObserverActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
        Log.d("Main","test commit");
    }
}
