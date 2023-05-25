package com.example.demoactivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private final long DEFAULT_COUNT = 6 * 1000L;
    private final long DEFAULT_DELAY = 1000L;

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
        btn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(et1.getText()) || TextUtils.isEmpty(et2.getText())) {
                Toast.makeText(TestActivity.this, "请输入需要比较的值", Toast.LENGTH_SHORT).show();
                return;
            }

            int result1 = et1.getText().toString().compareToIgnoreCase(et2.getText().toString());
            tv.setText(String.valueOf(result1));
        });

        Button btn_top = findViewById(R.id.btn_top_activity);
        TextView tv_top_name = findViewById(R.id.tv_top_name);
        btn_top.setOnClickListener(v -> {
            String name = getTopActivity(TestActivity.this);
            tv_top_name.setText(name);
        });

        final EditText et_time1 = findViewById(R.id.et_time1);
        final EditText et_time2 = findViewById(R.id.et_time2);
        Button btn_start = findViewById(R.id.btn_start);
        Button btn_count = findViewById(R.id.btn_time_count);

        btn_start.setOnClickListener(v -> {
            long timeCount = !TextUtils.isEmpty(et_time1.getText().toString()) ? Long.parseLong(et_time1.getText().toString()) * 1000: DEFAULT_COUNT;
            long timeDelay = !TextUtils.isEmpty(et_time2.getText().toString()) ? Long.parseLong(et_time2.getText().toString()) * 1000: DEFAULT_DELAY;

            CountDownTimer countDownTimer = new CountDownTimer(timeCount, timeDelay) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btn_count.setText("还剩余" + millisUntilFinished / 1000 + "秒钟结束");
                    btn_count.setEnabled(false);
                    btn_count.setBackgroundColor(getResources().getColor(R.color.color_dark_gray));
                }

                @Override
                public void onFinish() {
                    btn_count.setText("TICK");
                    btn_count.setEnabled(true);
                    btn_count.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            };

            countDownTimer.start();
        });

        btn_count.setOnClickListener(v -> Toast.makeText(TestActivity.this, "当前此按钮可以点击！", Toast.LENGTH_SHORT).show());
    }

    public static String getTopActivity(Context c) {
        ActivityManager am = (ActivityManager) c
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningTaskInfo> infos
                    = am.getRunningTasks(1);
            if (infos != null && infos.size() != 0) {
                ComponentName name = infos.get(0).topActivity;
                String actName = null;
                if (name != null) {
                    actName = name.getClassName();
                }
                return actName;
            }
        }
        return null;
    }

}
