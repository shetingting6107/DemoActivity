package com.example.demoactivity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demoactivity.deviceAdmin.DeviceManageReceiver;
import com.example.demoactivity.otto.BusHelper;
import com.example.demoactivity.otto.EventData;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private final long DEFAULT_COUNT = 6 * 1000L;
    private final long DEFAULT_DELAY = 1000L;

    Button btn_goto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        BusHelper.getBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusHelper.getBus().unregister(this);
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

        //激活设备管理器
        Button btn_device_admin = findViewById(R.id.btn_device_admin);
        btn_device_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicePolicyManager manager = (DevicePolicyManager) TestActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);
                //启动第三方组件
                ComponentName componentName = new ComponentName(TestActivity.this, DeviceManageReceiver.class);
                // 判断是否为设备管理器
                String TAG = "TEST_ACTIVITY";
                if (manager.isAdminActive(componentName)) {
                    Log.d(TAG, "isDeviceAdminActive 已经激活");
                    Toast.makeText(TestActivity.this, "isDeviceAdminActive 已经激活", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "isDeviceAdminActive 未激活");
                    Toast.makeText(TestActivity.this, "isDeviceAdminActive 未激活", Toast.LENGTH_SHORT).show();
                    //如果不是，则构建一个intent，action参数的意思为添加一个设备管理者
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    TestActivity.this.startActivity(intent);
                }
            }
        });

        //解除设备管理器
        Button btn_unregister_device_admin = findViewById(R.id.btn_unregister_device_admin);
        btn_unregister_device_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (isDeviceOwnerApp(TestActivity.this)) {
                            Log.d("TEST", "========removeDeviceOwner=========");
                            //   DeviceAdminReceiver.handleDeviceAdminDisable(context);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DevicePolicyManager mDevicePolicyManager = getDPM(TestActivity.this);
                                    mDevicePolicyManager.clearDeviceOwnerApp(TestActivity.this.getPackageName());
                                }
                            }, 1000 * 6);
                        }
                    }
                } catch (Exception ex) {
                    Log.e("TEST", "exception while removeDeviceOwner:" + Log.getStackTraceString(ex));
                }
            }
        });

        TextView tv_html = findViewById(R.id.tv_html);
        String str = "去百度";
        String url = "https://www.baidu.com/";
        String htmlText = "<a href=\""
                + url
                + "\">"
                + str
                + "</a>";
        CharSequence cs = Html.fromHtml(htmlText);
        tv_html.setText(cs);
        tv_html.setMovementMethod(LinkMovementMethod.getInstance());

        btn_goto = findViewById(R.id.btn_OTTO);
        btn_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送事件的通用方式
                BusHelper.getBus().post(new EventData("li", "hello"));
            }
        });
    }

    /**
     * 订阅事件,该方法只会接收一个参数，即希望订阅的事件
     * 名称是任意的，满足注释、单参数、公共访问修饰符即可
     * @param eventData 订阅事件
     */
    @Subscribe
    public void getData(EventData eventData) {
        Toast.makeText(this, "name = " + eventData.getName() + ", message = " + eventData.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 两个订阅事件都会生效
     * @param eventData 订阅事件
     */
    @Subscribe
    public void showData(EventData eventData) {
        String text = eventData.getName() + " + " + eventData.getMessage();
        btn_goto.setText(text);
    }

    /**
     * produce注释也可以发布事件，它在任何订阅者注册时立即向其提供回调
     * 此方法不接受任何参数，返回类型的作用为生成初始值的事件类型
     * @return 事件类型
     */
    @Produce
    public EventData produceEventData() {
        EventData eventData = new EventData("yang", "hi!");
        return eventData;
    }

    @TargetApi(18)
    public boolean isDeviceOwnerApp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            DevicePolicyManager manager = getDPM(context);
            if (manager.isDeviceOwnerApp(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private DevicePolicyManager getDPM(Context context) {
        return (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
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
