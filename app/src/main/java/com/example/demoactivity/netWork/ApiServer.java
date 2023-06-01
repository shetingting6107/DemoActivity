package com.example.demoactivity.netWork;

import com.example.demoactivity.netWork.base.BaseBean;
import com.example.demoactivity.netWork.base.BaseResponse;
import com.example.demoactivity.netWork.bean.ArticleBean;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.bean.BannerBean;
import com.example.demoactivity.netWork.bean.SearchHotKeyBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /**
     * 获取按关键字搜索文章列表
     * @param page 页码
     * @return 文章列表
     */
    @POST("/article/query/{page}/json")
    Call<BaseResponse<ArticleListBean>> getArticleByKey(@Path("page") int page, @Query("k") String k);

    /**
     * 获取置顶文章
     * @return 置顶文章列表
     */
    @GET("/article/top/json")
    Call<BaseResponse<List<ArticleBean>>> getTopArticle();

    /**
     * 登录wan android
     */
    @POST("/user/login")
    Call<BaseResponse<BaseBean>> login(@Query("username") String userName, @Query("password") String pwd);

    /**
     * 注册wan android
     */
    @POST("/user/register")
    Call<BaseResponse<BaseBean>> register(@Query("username") String userName, @Query("password") String pwd, @Query("repassword") String rePwd);

}
