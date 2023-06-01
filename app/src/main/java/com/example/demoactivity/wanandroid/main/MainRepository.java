package com.example.demoactivity.wanandroid.main;

import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.base.BaseRepository;
import com.example.demoactivity.netWork.bean.ArticleBean;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.bean.BannerBean;
import com.example.demoactivity.netWork.bean.SearchHotKeyBean;

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

    /**
     * 获取搜索热词
     */
    public void getSearchHotKey(HttpCallback<List<SearchHotKeyBean>> callback) {
        apiServer.getSearchHotKey().enqueue(callback);
    }

    /**
     * 根据关键字检索文章
     * @param page 页码
     * @param key 关键字
     */
    public void getArticleByKey(int page, String key, HttpCallback<ArticleListBean> callback) {
        apiServer.getArticleByKey(page, key).enqueue(callback);
    }

    /**
     * 获取置顶文章列表
     */
    public void getTopArticle(HttpCallback<List<ArticleBean>> callback) {
        apiServer.getTopArticle().enqueue(callback);
    }
}
