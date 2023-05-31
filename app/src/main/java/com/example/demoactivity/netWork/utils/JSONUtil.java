package com.example.demoactivity.netWork.utils;

import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.bean.BannerBean;
import com.google.gson.Gson;

public class JSONUtil {

    public static ArticleListBean parseToArticleListBean(Object response) {
        if (response == null) {
            return null;
        }

        Gson gson = new Gson();
        String date = gson.toJson(response);
        return gson.fromJson(date, ArticleListBean.class);
    }

    public static BannerBean parseToBannerListBean(Object response) {
        if (response == null) {
            return null;
        }

        Gson gson = new Gson();
        String date = gson.toJson(response);
        return gson.fromJson(date, BannerBean.class);
    }
}
