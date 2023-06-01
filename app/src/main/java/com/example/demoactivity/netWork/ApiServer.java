package com.example.demoactivity.netWork;

import com.example.demoactivity.netWork.base.BaseResponse;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.bean.BannerBean;
import com.example.demoactivity.netWork.bean.SearchHotKeyBean;

import java.util.List;

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

    /**
     * 获取首页banner图片信息
     * @return banner信息
     */
    @GET("/banner/json")
    Call<BaseResponse<List<BannerBean>>> getBanner();

    /**
     * 获取搜索热词
     * @return 搜索热词信息
     */
    @GET("/hotkey/json")
    Call<BaseResponse<List<SearchHotKeyBean>>> getSearchHotKey();
}
