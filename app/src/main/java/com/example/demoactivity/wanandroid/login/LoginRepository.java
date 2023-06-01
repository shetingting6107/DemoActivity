package com.example.demoactivity.wanandroid.login;

import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.base.BaseBean;
import com.example.demoactivity.netWork.base.BaseRepository;

public class LoginRepository extends BaseRepository {

    /**
     * 登录wan android
     * @param username 用户名
     * @param pwd 密码
     */
    public void login(String username, String pwd, HttpCallback<BaseBean> callback) {
        apiServer.login(username, pwd).enqueue(callback);
    }

    /**
     * 注册wan android
     * @param username 用户名
     * @param pwd 密码
     * @param rePwd 确认密码
     */
    public void register(String username, String pwd, String rePwd, HttpCallback<BaseBean> callback) {
        apiServer.register(username, pwd, rePwd).enqueue(callback);
    }
}
