package com.example.demoactivity.wanandroid.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.demoactivity.R;

public class LoadingDialog extends Dialog {

    private ImageView iv_img;
    private AnimationSet animationSet;

    private static LoadingDialog mInstance;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public static LoadingDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LoadingDialog(context);
        }
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //透明背景处理
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setDimAmount(0f);

        this.setContentView(R.layout.dialog_loading);

        //设置dialog属性
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        iv_img = findViewById(R.id.iv_img);
        loading();
    }

    @Override
    protected void onStart() {
        super.onStart();
        iv_img.startAnimation(animationSet);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void loading() {
        animationSet = new AnimationSet(true);
        RotateAnimation animation = new RotateAnimation(0, +359,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setRepeatCount(-1);
        animation.setStartOffset(0);
        animation.setDuration(1000);
        LinearInterpolator lir = new LinearInterpolator();
        animationSet.setInterpolator(lir);
        animationSet.addAnimation(animation);
    }

    public static void showLoading(Context context) {
        if (mInstance == null) {
            mInstance = new LoadingDialog(context);
        }

        mInstance.show();
    }

    public static void dismissLoading(Context context) {
        if (mInstance == null) {
            mInstance = new LoadingDialog(context);
        }

        mInstance.dismiss();
    }
}
