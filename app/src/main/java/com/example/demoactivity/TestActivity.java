package com.example.demoactivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        final EditText et1 = findViewById(R.id.et_compare1);
        final EditText et2 = findViewById(R.id.et_compare2);
        Button btn = findViewById(R.id.btn_get);
        final TextView tv = findViewById(R.id.tv_result);

        int result = 0;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et1.getText()) || TextUtils.isEmpty(et2.getText())) {
                    Toast.makeText(TestActivity.this, "请输入需要比较的值", Toast.LENGTH_SHORT).show();
                    return;
                }

                int result = et1.getText().toString().compareToIgnoreCase(et2.getText().toString());
                tv.setText(String.valueOf(result));
            }
        });
    }


}
