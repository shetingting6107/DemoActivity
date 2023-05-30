package com.example.demoactivity.wanandroid;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.bean.ArticleBean;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.bean.BannerBean;
import com.example.demoactivity.netWork.utils.JSONUtil;
import com.example.demoactivity.wanandroid.base.BaseActivity;
import com.example.demoactivity.wanandroid.main.MainArticleAdapter;
import com.example.demoactivity.wanandroid.main.MainRepository;

import java.util.List;

public class WanAndroidMainActivity extends BaseActivity {

    private MainRepository mainRepository;

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView articleListView;
    private MainArticleAdapter articleAdapter;

    private LinearLayoutManager mLayoutManager;

    private List<ArticleBean> mArticleList;

    private int mPage = 0;
    private int mTotalPage = 0;
    private int mCurrentPage = 0;

    public static final String TAG = "WanAndroidMain";

    @Override
    public void initView() {
        swipeLayout = findViewById(R.id.swipe_layout);
        articleListView = findViewById(R.id.rv_article);
    }

    @Override
    public void initData() {
        mainRepository = new MainRepository();

        //设置下拉刷新loading圆圈的颜色
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //设置下拉刷新loading背景颜色
        swipeLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.color_dark_gray));
        //设置刷新监听事件
        swipeLayout.setOnRefreshListener(() -> mainRepository.getMainArticleList(0, new HttpCallback() {
            @Override
            public void onSucceed(Object t) {
                mPage = 0;
                ArticleListBean articleListBean = JSONUtil.parseToArticleListBean(t);
                if (articleListBean == null) {
                    onFailed(0, null);
                    return;
                }
                List<ArticleBean> articleList = articleListBean.getDatas();
                mTotalPage = articleListBean.getPageCount();
                mCurrentPage = articleListBean.getCurPage();
                if (mCurrentPage <= 1) {
                    mArticleList = articleList;
                }else {
                    mArticleList.addAll(articleList);
                }
//                List<ArticleBean> list = removeNullTags(articleList);
                articleAdapter.setArticleList(mArticleList);
                articleAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(int code, String message) {
                mPage = 0;
                swipeLayout.setRefreshing(false);
                Toast.makeText(WanAndroidMainActivity.this, TextUtils.isEmpty(message) ? "文章内容为空！" : message, Toast.LENGTH_SHORT).show();
            }
        }));

        articleAdapter = new MainArticleAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        articleListView.setLayoutManager(mLayoutManager);
        articleListView.setAdapter(articleAdapter);
        articleListView.addOnScrollListener(onScrollListener);

        initList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wan_android_main;
    }

    private void initList() {
        loadBanner();
        loadMore();
    }

//    private List<ArticleBean> removeNullTags(List<ArticleBean> beans) {
//        List<ArticleBean> beanList = new ArrayList<>();
//        if (beans == null || beans.size() <= 0) {
//            return beanList;
//        }
//
//        for (ArticleBean bean : beans) {
//            if (bean.getTags() != null && bean.getTags().size() > 0) {
//                beanList.add(bean);
//            }
//        }
//
//        return beanList;
//    }

    /**
     * 展示文章列表内容
     * 通过mPage值控制展示到第几页的列表
     */
    private void loadMore() {
        mainRepository.getMainArticleList(mPage, new HttpCallback() {
            @Override
            public void onSucceed(Object t) {
                ArticleListBean articleListBean = JSONUtil.parseToArticleListBean(t);
                if (articleListBean == null) {
                    onFailed(0, null);
                    return;
                }
                List<ArticleBean> articleList = articleListBean.getDatas();
//                List<ArticleBean> list = removeNullTags(articleList);
                mTotalPage = articleListBean.getPageCount();
                mCurrentPage = articleListBean.getCurPage();
                if (mCurrentPage <= 1) {
                    mArticleList = articleList;
                }else {
                    mArticleList.addAll(articleList);
                }
                articleAdapter.setArticleList(mArticleList);
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String message) {
                Toast.makeText(WanAndroidMainActivity.this, TextUtils.isEmpty(message) ? "文章内容为空！" : message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 加载banner信息
     */
    private void loadBanner() {
        mainRepository.getBanner(new HttpCallback() {
            @Override
            public void onSucceed(Object t) {
                List<BannerBean> bannerList = (List<BannerBean>) t;
                if (bannerList == null || bannerList.size() <= 0) {
                    onFailed(0, "list is empty!");
                    return;
                }
                Log.d(TAG, "banner list = " + bannerList);
            }

            @Override
            public void onFailed(int code, String message) {
                Toast.makeText(WanAndroidMainActivity.this, message != null ? message : "存在异常，请排查！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * RecyclerView 滑动监听器
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private int lastItem;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //当滑动到最后一个且停止滚动
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItem + 1 == articleAdapter.getItemCount()
                    && mCurrentPage < 10) {
                Log.d(TAG, "load more article");
                Toast.makeText(WanAndroidMainActivity.this, "show more articles!", Toast.LENGTH_SHORT).show();
                mPage += 1;
                loadMore();
            }else if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItem + 1 == articleAdapter.getItemCount()
                    && mCurrentPage >= 10){
                //由于数据量太大，所以将可浏览的文章量控制在200篇内（20*10）
                Toast.makeText(WanAndroidMainActivity.this, "All Article is show!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };
}
