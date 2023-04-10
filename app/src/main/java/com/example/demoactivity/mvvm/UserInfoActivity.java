package com.example.demoactivity.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.demoactivity.R;
import com.example.demoactivity.databinding.ActivityUserInfoBinding;
import com.example.demoactivity.mvvm.model.UserInfo;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding userInfoBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
    }

    public void clickName(View v) {
        userInfoBinding.setUser(new UserInfo("Tom", "男"));
    }
}
