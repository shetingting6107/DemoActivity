package com.example.demoactivity.accessibility.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoactivity.R;

public class AccessibilityActivity extends AppCompatActivity {

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
    }
}
