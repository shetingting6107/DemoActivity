package com.example.demoactivity.wanandroid.main;

import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.base.BaseRepository;

public class MainRepository extends BaseRepository {

    /**
     * 获取首页文章列表
     * @param page 页码
     */
    public void getMainArticleList(int page, HttpCallback callback) {
        apiServer.getMainArticleList(page).enqueue(callback);
    }
}
