package com.example.demoactivity.netWork;

import com.example.demoactivity.netWork.base.BaseResponse;
import com.example.demoactivity.netWork.bean.ArticleListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServer {

    /**
     * 获取首页文章列表
     * @param page 页码
     * @return 返回首页文章列表
     */
    @GET("/article/list/{page}/json")
    Call<BaseResponse<ArticleListBean>> getMainArticleList(@Path("page") int page);
}
