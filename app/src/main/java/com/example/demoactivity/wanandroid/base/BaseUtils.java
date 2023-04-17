package com.example.demoactivity.wanandroid.base;

import android.content.Context;
import android.content.Intent;

import com.example.demoactivity.wanandroid.login.LoginActivity;

public class BaseUtils {

    /**
     * 登录失效，返回登录页面
     * @param context 上下文
     */
    public static void backToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
