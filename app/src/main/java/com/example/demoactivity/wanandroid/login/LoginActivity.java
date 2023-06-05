package com.example.demoactivity.wanandroid.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.base.BaseBean;
import com.example.demoactivity.wanandroid.WanAndroidMainActivity;
import com.example.demoactivity.wanandroid.base.BaseActivity;
import com.example.demoactivity.wanandroid.util.LoadingDialog;

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

    private LoginRepository loginRepository;

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
    }

    @Override
    public void initData() {
        loginRepository = new LoginRepository();

        tvToRegister.setOnClickListener(v -> {
            llLogin.setVisibility(View.GONE);
            llRegister.setVisibility(View.VISIBLE);
        });

        tvToLogin.setOnClickListener(v -> {
            llLogin.setVisibility(View.VISIBLE);
            llRegister.setVisibility(View.GONE);
        });

        btnRegister.setOnClickListener(v -> {
            String userName = etRegisterUsername.getText().toString();
            String pwd = etRegisterPwd.getText().toString();
            String rePwd = etRegisterConfirmPwd.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(rePwd)){
                Toast.makeText(LoginActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
            }else if (!pwd.equals(rePwd)) {
                Toast.makeText(LoginActivity.this, "两次输入的密码不一致！请检查！", Toast.LENGTH_SHORT).show();
            }else {
                register(userName, pwd, rePwd);
            }
        });

        btnLogin.setOnClickListener(v -> {
            String userName = etLoginUsername.getText().toString();
            String pwd = etLoginPwd.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            }else {
                login(userName, pwd);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wan_login;
    }

    /**
     * 登录
     * @param userName 用户名
     * @param pwd 密码
     */
    private void login(String userName, String pwd) {
        LoadingDialog.showLoading(LoginActivity.this);
        loginRepository.login(userName, pwd, new HttpCallback<BaseBean>() {
            @Override
            public void onSucceed(Object t) {
                LoadingDialog.dismissLoading(LoginActivity.this);
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                etLoginUsername.setText("");
                etLoginPwd.setText("");
                Intent intent = new Intent(LoginActivity.this, WanAndroidMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int code, String message) {
                LoadingDialog.dismissLoading(LoginActivity.this);
                Toast.makeText(LoginActivity.this, TextUtils.isEmpty(message) ? "登录失败！请检查！" : message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 注册
     * @param username 用户名
     * @param pwd 密码
     * @param repwd 确认密码
     */
    private void register(String username, String pwd, String repwd) {
        LoadingDialog.showLoading(LoginActivity.this);
        loginRepository.register(username, pwd, repwd, new HttpCallback<BaseBean>() {
            @Override
            public void onSucceed(Object t) {
                LoadingDialog.dismissLoading(LoginActivity.this);
                Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                etRegisterUsername.setText("");
                etRegisterPwd.setText("");
                etRegisterConfirmPwd.setText("");
                llRegister.setVisibility(View.GONE);
                llLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(int code, String message) {
                LoadingDialog.dismissLoading(LoginActivity.this);
                Toast.makeText(LoginActivity.this, TextUtils.isEmpty(message) ? "注册失败！请检查！" : message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
