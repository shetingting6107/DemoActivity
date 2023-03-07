package com.example.demoactivity.accessibility.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoactivity.R;

public class AccessibilityActivity extends AppCompatActivity {

    private static TextView tv_label;
    private static String labelText = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility);
        initView();
    }

    private void initView() {
        TextView tv_start_service = findViewById(R.id.tv_start_access_service);
        tv_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });
        tv_label = findViewById(R.id.tv_label);
    }

    public static void setLabel(String name, String text) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(text)) {
            return;
        }

        labelText += "好友名：" + name + "; 发送内容：" + text + "\n";
        tv_label.setText(labelText);
    }
}
