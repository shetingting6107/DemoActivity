package com.example.demoactivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.demoactivity.accessibility.ui.AccessibilityActivity;
import com.example.demoactivity.content.observer.DemoObserverActivity;
import com.example.demoactivity.mvvm.UserInfoActivity;
import com.example.demoactivity.netWork.NetWorkActivity;
import com.example.demoactivity.wanandroid.WanAndroidMainActivity;
import com.example.demoactivity.xLog.XlogActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //得到了授权
                    Toast.makeText(this, "网络权限授权成功！", Toast.LENGTH_SHORT).show();
                }else {
                    //未授权
                    Toast.makeText(this, "网络权限授权失败！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
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
        TextView tv_mvvm = findViewById(R.id.tv_mvvm);
        tv_mvvm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        TextView tv_webview = findViewById(R.id.tv_webview);
        tv_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(MainActivity.this, Manifest.permission.INTERNET)) {
                    Intent intent = new Intent(MainActivity.this, NetWorkActivity.class);
                    startActivity(intent);
                }else {
                    //请求权限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 102);
                }

            }
        });

        TextView tv_wanandroid = findViewById(R.id.tv_wanandroid);
        tv_wanandroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WanAndroidMainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
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

    private boolean checkPermission(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }
}
