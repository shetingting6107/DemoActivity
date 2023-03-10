package com.example.demoactivity.accessibility.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

    private static final String DY_CHAT_NAME_ITEM = "com.bytedance.ies.dmt.ui.widget.DmtTextView";
    private static final String DY_CHAT_EDIT_ITEM = "android.widget.EditText";
    private static final String DY_CHAT_LIST_ITEM = "androidx.recyclerview.widget.RecyclerView";

    private static boolean isInput = false;

    private static final List<String> sensitives = new ArrayList<>();
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
        initSensitive();
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
        boolean chatView = false;
        if (root != null){
            chatView = inChatWindow(root);
        }
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.d(TAG, "view text changed");
                if (!chatView) {
                    //非聊天界面不处理
                    break;
                }
                CharSequence cs = event.getText() != null && event.getText().size() > 0 ? event.getText().get(0) : null;
                if (cs == null || !cs.toString().equals("发送消息") && !cs.toString().equals("发消息...")) {
                    text = cs;
                }
                if (text != null && sensitives.contains(text.toString())) {
                    text = "****";
                    Log.d(TAG, "contain sensitive word");
                    AccessibilityNodeInfo inputwindow = event.getSource();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                        inputwindow.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                        inputwindow.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    } else {
                        selectInputwindow(inputwindow, text.length());
                        ClipboardManager clipboardManager = (ClipboardManager) this
                                .getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("label", text));
                        inputwindow.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    }
                }
                isInput = true;
                break;
            //记录文字变化
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                Log.d(TAG, "text is changed");
                if (!chatView) {
                    //非聊天界面不处理
                    break;
                }
                CharSequence str = event.getText() != null && event.getText().size() > 0 ? event.getText().get(0) : null;
                if (!isInput && str != null
                        && (str.toString().equals("发送消息") || str.toString().equals("发消息..."))) {
                    String chatFri = getChatName(root);
                    if (chatFri != null) {
                        AccessibilityActivity.setLabel(chatFri, text.toString(), getAppPackageName());
                        text = "";
                    }
                }
                isInput = false;
                break;
                //通过滑动事件获取退出重进聊天界面的编辑文字
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (!chatView) {
                    //非聊天界面不处理
                    break;
                }
                text = getEditTextStr(root);
                Log.d(TAG, "text is =" + text);
                break;
        }
    }

    public String getStr(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        return charSequence.toString();
    }

    private boolean inChatWindow(AccessibilityNodeInfo root) {
        if (root == null) {
            return false;
        }
        if (TextUtils.isEmpty(packName)) {
            return false;
        }
        List<AccessibilityNodeInfo> chatList = null;
        boolean isInChat = false;
        //聊天界面id
        switch (packName) {
            case APP_PACKAGE_NAME:
            case APP_PACKAGE_NAME1:
                isInChat = getDouYinChatView(root);
                break;
            case APP_PACKAGE_NAME2:
                isInChat = getDouYinLiveChatView(root);
                break;
            case APP_PACKAGE_NAME3:
                chatList = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b79");
                break;
            default:
                break;
        }
        return isInChat;
    }

    private String getChatName(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }
        if (TextUtils.isEmpty(packName)) {
            return null;
        }
        List<AccessibilityNodeInfo> nameList = null;
        String name = null;
        //聊天好友名称
        switch (packName) {
            case APP_PACKAGE_NAME:
            case APP_PACKAGE_NAME1:
                name = getDouYinChatFriName(root);
                break;
            case APP_PACKAGE_NAME2:
                name = getDouYinLiveChatName(root);
                break;
            case APP_PACKAGE_NAME3:
                nameList = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ko4");
                break;
            default:
                break;
        }

        return name;
    }

    private String getAppPackageName() {
        String name = "";
        if (TextUtils.isEmpty(packName)) {
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

    private void initSensitive() {
        sensitives.add("test");
        sensitives.add("ok");
        sensitives.add("qwer");
        sensitives.add("1111");
        sensitives.add("12345");
    }

    private void selectInputwindow(AccessibilityNodeInfo inputwindow, int contentLength) {
        if (inputwindow == null) {
            return;
        }
        Bundle arguments = new Bundle();
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, contentLength);
        inputwindow.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
    }

    private boolean getDouYinChatView1(AccessibilityNodeInfo root) {
        if (root == null) {
            return false;
        }
        AccessibilityNodeInfo child = root.getChild(0);
        if (child.getChildCount() < 5) {
            //表明不是在聊天界面
            return false;
        }
//        for (int i = 0; i < child.getChildCount(); i ++) {
//            printNodeInfo(child.getChild(i), i);
//        }

        //androidx.recyclerview.widget.RecyclerView
        AccessibilityNodeInfo chat = child.getChild(3);
        if (chat == null || !DY_CHAT_LIST_ITEM.equals(chat.getClassName().toString())) {
            return false;
        }
        //android.widget.TextView
        AccessibilityNodeInfo name = child.getChild(1);
        if (name == null) {
            return false;
        }
        Log.d(TAG, "chat name is =" + name.getText().toString());
        return true;
    }

    private boolean getDouYinChatView(AccessibilityNodeInfo root) {
        if (root == null) {
            return false;
        }
        if (root.getChildCount() < 2) {
            //表明不是在聊天界面
            return false;
        }
        AccessibilityNodeInfo child = root.getChild(1);
        if (child == null || child.getChildCount() < 10) {
            //表明当前页面不是聊天界面
            return false;
        }
        AccessibilityNodeInfo child1 = child.getChild(0);
        if (child1 == null || child1.getContentDescription() == null
                || !child1.getContentDescription().equals("返回")) {
            //表明当前页面不是聊天界面
            return false;
        }
        return true;
    }

    private boolean getDouYinLiveChatView(AccessibilityNodeInfo root){
        if (root == null) {
            return false;
        }
        AccessibilityNodeInfo child = root.getChild(0);
        if (child.getChildCount() < 5) {
            //表明不是在聊天界面
            return false;
        }

        //androidx.recyclerview.widget.RecyclerView
        AccessibilityNodeInfo chat = child.getChild(3);
        if (chat == null || !DY_CHAT_LIST_ITEM.equals(chat.getClassName().toString())) {
            return false;
        }
        return true;
    }

    private String getDouYinChatFriName(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }
        if (root.getChildCount() < 2) {
            //表明不是在聊天界面
            return null;
        }
        AccessibilityNodeInfo child = root.getChild(1);
        if (child == null || child.getChildCount() < 10) {
            //表明当前页面不是聊天界面
            return null;
        }
        AccessibilityNodeInfo child1 = child.getChild(0);
        if (child1 == null || child1.getContentDescription() == null
                || !child1.getContentDescription().equals("返回")) {
            //表明当前页面不是聊天界面
            return null;
        }
//        printNodeInfo(root);
        //com.bytedance.ies.dmt.ui.widget.DmtTextView 聊天好友姓名
        AccessibilityNodeInfo firNames = child.getChild(1);
        String content = firNames.getContentDescription().toString();
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        String[] split = content.split(",");
        if (split.length == 0) {
            return null;
        }
        return split[0];
    }

    private String getDouYinLiveChatName(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }
        AccessibilityNodeInfo child = root.getChild(0);
        if (child.getChildCount() < 5) {
            //表明不是在聊天界面
            return null;
        }

        //androidx.recyclerview.widget.RecyclerView
        AccessibilityNodeInfo chat = child.getChild(3);
        if (chat == null || !DY_CHAT_LIST_ITEM.equals(chat.getClassName().toString())) {
            //表明不是在聊天界面
            return null;
        }

        //android.widget.TextView
        AccessibilityNodeInfo name = child.getChild(1);
        if (name == null || TextUtils.isEmpty(name.getText())) {
            return null;
        }
        return name.getText().toString();
    }

    private String getEditTextStr(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }

        if(TextUtils.isEmpty(packName)) {
            return null;
        }

        String edit = null;
        switch (packName) {
            case APP_PACKAGE_NAME:
            case APP_PACKAGE_NAME1:
                edit = getDouYinEditText(root);
                break;
            case APP_PACKAGE_NAME2:
                edit = getDouYinLiveEditText(root);
                break;
        }


        return edit;
    }

    private String getDouYinEditText(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }

        if (root.getChildCount() < 2) {
            //表明不是在聊天界面
            return null;
        }

        AccessibilityNodeInfo child = root.getChild(1);
        if (child == null || child.getChildCount() < 10) {
            //表明当前页面不是聊天界面
            return null;
        }

        AccessibilityNodeInfo child1 = child.getChild(0);
        if (child1 == null || child1.getContentDescription() == null
                || !child1.getContentDescription().equals("返回")) {
            //表明当前页面不是聊天界面
            return null;
        }

        AccessibilityNodeInfo child2 = child.getChild(child.getChildCount() - 3);
        if (child2 == null || !DY_CHAT_EDIT_ITEM.equals(child2.getClassName().toString())) {
            return null;
        }

        return child2.getText().toString();
    }

    private String getDouYinLiveEditText(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }
        AccessibilityNodeInfo child = root.getChild(0);
        if (child.getChildCount() < 5) {
            //表明不是在聊天界面
            return null;
        }

        //androidx.recyclerview.widget.RecyclerView
        AccessibilityNodeInfo chat = child.getChild(3);
        if (chat == null || !DY_CHAT_LIST_ITEM.equals(chat.getClassName().toString())) {
            //表明不是在聊天界面
            return null;
        }

        //android.widget.LinearLayout
        AccessibilityNodeInfo input = child.getChild(child.getChildCount() - 1);
        //android.widget.EditText
        AccessibilityNodeInfo edit = input.getChild(0);
        if (edit == null || !DY_CHAT_EDIT_ITEM.equals(edit.getClassName().toString())){
            return null;
        }

        String str = edit.getText() != null ? edit.getText().toString() : null;
        if (str != null && str.equals("发消息")) {
            str = null;
        }
        return str;
    }

    private void printNodeInfo(AccessibilityNodeInfo root, int index) {
        Log.d(TAG, "rootView id is:" + root.getViewIdResourceName() + ";\n"
                + "rootView class is:" + root.getClassName() + ";\n"
                + "rootView content is:" + root.getContentDescription() + ";\n"
                + "rootView size is:" + root.getChildCount() + ";\n"
                + "rooView index is:" + index + ";\n"
                + "rootView text is:" + root.getText() + ";");
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
