package com.example.demoactivity.accessibility.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.demoactivity.accessibility.ui.AccessibilityActivity;

import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String APP_PACKAGE_NAME = "com.ss.android.ugc.aweme";//抖音
    private static final String APP_PACKAGE_NAME1 = "com.ss.android.ugc.aweme.lite";//抖音极速版
    private static final String APP_PACKAGE_NAME2 = "com.ss.android.ugc.live";//抖音火山版
    private static final String APP_PACKAGE_NAME3 = "com.tencent.mm";//微信
    private static final List<String> apps = new ArrayList<>();
    private static final String TAG = "MyAccessibilityService";
    private static CharSequence text = "";
    private static String packName;

    /**
     * 当启动服务的时候就会被调用,系统成功绑定该服务时被触发，也就是当你在设置中开启相应的服务，
     * 系统成功的绑定了该服务时会触发，通常我们可以在这里做一些初始化操作
     */
    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        info.packageNames = new String[]{APP_PACKAGE_NAME, APP_PACKAGE_NAME1, APP_PACKAGE_NAME2, APP_PACKAGE_NAME3};//监听的app是抖音
        apps.add(APP_PACKAGE_NAME);
        apps.add(APP_PACKAGE_NAME1);
        apps.add(APP_PACKAGE_NAME2);
        apps.add(APP_PACKAGE_NAME3);
        this.setServiceInfo(info);
    }

    /**
     * 通过系统监听窗口变化的回调,sendAccessibiliyEvent()不断的发送AccessibilityEvent到此处
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        if (!apps.contains(packageName)) {
            return;//监听到的包不是对应的包则不处理
        }
        packName = packageName;
        int eventType = event.getEventType();
        Log.d(TAG, "eventType is = " + eventType);
        AccessibilityNodeInfo root = this.getRootInActiveWindow();
        AccessibilityNodeInfo chatView = null;
        if (root != null){
            chatView = inChatWindow(root);
        }
        switch (eventType) {
            //记录文字变化
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                Log.d(TAG, "text is changed");
                if (chatView == null) {
                    //非聊天界面不处理
                    break;
                }
                CharSequence str = event.getText() != null && event.getText().size() > 0 ? event.getText().get(0) : null;
                if (str != null && !str.toString().equals("发送消息") && !str.toString().equals("发消息...")) {
                    text = str;
                }
                break;
                //当页面滑动时，处理是否是对应处理事件
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.d(TAG, "click view");
                if (chatView == null) {
                    text = "";
                    break;
                }
                if (TextUtils.isEmpty(text)){
                    break;
                }
                AccessibilityNodeInfo chatFri = getChatName(root);
                if (chatFri != null) {
                    String friendName = getStr(chatFri.getText());
                    AccessibilityActivity.setLabel(friendName, text.toString(), getAppPackageName());
                    text = "";
                }
                break;
        }
    }

    public String getStr(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        return charSequence.toString();
    }

    private AccessibilityNodeInfo inChatWindow(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }
        if (packName == null) {
            return null;
        }
        List<AccessibilityNodeInfo> chatList = null;
        //聊天界面id
        switch (packName) {
            case APP_PACKAGE_NAME:
                chatList = root.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.aweme:id/pkg");
                break;
            case APP_PACKAGE_NAME1:
                chatList = root.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.aweme.lite:id/au-");
                break;
            case APP_PACKAGE_NAME2:
                chatList = root.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.live:id/al8");
                break;
            case APP_PACKAGE_NAME3:
                chatList = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b79");
                break;
            default:
                break;
        }
        if (chatList == null || chatList.size() <= 0) {
            return null;
        }
        return chatList.get(0);
    }

    private AccessibilityNodeInfo getChatName(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }
        if (packName == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nameList = null;
        //聊天好友名称
        switch (packName) {
            case APP_PACKAGE_NAME:
                nameList = root.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.aweme:id/bi");
                break;
            case APP_PACKAGE_NAME1:
                nameList = root.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.aweme.lite:id/qd");
                break;
            case APP_PACKAGE_NAME2:
                nameList = root.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.live:id/title");
                break;
            case APP_PACKAGE_NAME3:
                nameList = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ko4");
                break;
            default:
                break;
        }
        if (nameList == null || nameList.size() <= 0) {
            return null;
        }
        return nameList.get(0);
    }

    private String getAppPackageName() {
        String name = "";
        if (packName == null) {
            return name;
        }

        switch (packName) {
            case APP_PACKAGE_NAME:
                name = "抖音";
                break;
            case APP_PACKAGE_NAME1:
                name = "抖音极速版";
                break;
            case APP_PACKAGE_NAME2:
                name = "抖音火山版";
                break;
            case APP_PACKAGE_NAME3:
                name = "微信";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }

    /**
     * 中断服务时的回调.
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "DemoActivity 辅助功能中断！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查找拥有特定焦点类型的控件
     *
     * @param focus
     * @return
     */
    @Override
    public AccessibilityNodeInfo findFocus(int focus) {
        return super.findFocus(focus);
    }

    /**
     * 如果配置能够获取窗口内容,则会返回当前活动窗口的根结点
     *
     * @return
     */
    @Override
    public AccessibilityNodeInfo getRootInActiveWindow() {
        return super.getRootInActiveWindow();
    }

    /**
     * 获取系统服务
     *
     * @param name
     * @return
     */
    @Override
    public Object getSystemService(@NonNull String name) {
        return super.getSystemService(name);
    }

    /**
     * 如果允许服务监听按键操作，该方法是按键事件的回调，需要注意，这个过程发生了系统处理按键事件之前
     *
     * @param event
     * @return
     */
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }
}
