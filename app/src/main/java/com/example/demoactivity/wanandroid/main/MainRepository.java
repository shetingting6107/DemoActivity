package com.example.demoactivity.wanandroid.main;

import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.base.BaseRepository;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.bean.BannerBean;

import java.util.List;

public class MainRepository extends BaseRepository {

    /**
     * 获取首页文章列表
     * @param page 页码
     */
    public void getMainArticleList(int page, HttpCallback<ArticleListBean> callback) {
        apiServer.getMainArticleList(page).enqueue(callback);
    }

    /**
     * 获取首页banner
     */
    public void getBanner(HttpCallback<List<BannerBean>> callback) {
        apiServer.getBanner().enqueue(callback);
    }
}
