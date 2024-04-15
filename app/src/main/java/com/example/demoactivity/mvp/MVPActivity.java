package com.example.demoactivity.mvp;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoactivity.R;

public class MVPActivity extends AppCompatActivity {

    private Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_activity);
        btn_login = findViewById(R.id.btn_login);
    }
}
