package com.example.demoactivity.wanandroid.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demoactivity.R;
import com.example.demoactivity.wanandroid.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private LinearLayout llLogin;
    private EditText etLoginUsername;
    private EditText etLoginPwd;
    private TextView tvToRegister;
    private Button btnLogin;
    private LinearLayout llRegister;
    private EditText etRegisterUsername;
    private EditText etRegisterPwd;
    private EditText etRegisterConfirmPwd;
    private TextView tvToLogin;
    private Button btnRegister;

    @Override
    public void initView() {
        llLogin = findViewById(R.id.ll_login);
        etLoginUsername = findViewById(R.id.et_name);
        etLoginPwd = findViewById(R.id.et_pwd);
        tvToRegister = findViewById(R.id.tv_register);
        btnLogin = findViewById(R.id.btn_login);
        llRegister = findViewById(R.id.ll_register);
        etRegisterUsername = findViewById(R.id.et_register_name);
        etRegisterPwd = findViewById(R.id.et_register_pwd);
        etRegisterConfirmPwd = findViewById(R.id.et_register_confirm_pwd);
        tvToLogin = findViewById(R.id.tv_login);
        btnRegister = findViewById(R.id.btn_register);

        tvToRegister.setOnClickListener(v -> {
            llLogin.setVisibility(View.GONE);
            llRegister.setVisibility(View.VISIBLE);
        });

        tvToLogin.setOnClickListener(v -> {
            llLogin.setVisibility(View.VISIBLE);
            llRegister.setVisibility(View.GONE);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wan_login;
    }
}
