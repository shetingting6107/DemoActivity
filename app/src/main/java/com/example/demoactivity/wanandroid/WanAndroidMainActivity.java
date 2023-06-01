package com.example.demoactivity.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.demoactivity.R;
import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.NetWorkActivity;
import com.example.demoactivity.netWork.bean.ArticleBean;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.bean.BannerBean;
import com.example.demoactivity.wanandroid.base.BaseActivity;
import com.example.demoactivity.wanandroid.main.MainArticleAdapter;
import com.example.demoactivity.wanandroid.main.MainRepository;
import com.example.demoactivity.wanandroid.main.SearchActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class WanAndroidMainActivity extends BaseActivity {

    private MainRepository mainRepository;

    private EditText etMainSearch;
    private Banner banner;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView articleListView;

    private MainArticleAdapter articleAdapter;

    private LinearLayoutManager mLayoutManager;

    private List<ArticleBean> mArticleList;
    private List<BannerBean> mBannerList;

    private int mPage = 0;
    private int mTotalPage = 0;
    private int mCurrentPage = 0;
    private Context mContext;

    public static final String TAG = "WanAndroidMain";

    @Override
    public void initView() {
        mContext = getApplicationContext();
        etMainSearch = findViewById(R.id.et_main_search);
        banner = findViewById(R.id.banner);
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
        swipeLayout.setOnRefreshListener(() -> mainRepository.getMainArticleList(0, new HttpCallback<ArticleListBean>() {
            @Override
            public void onSucceed(Object t) {
                mPage = 0;
                ArticleListBean articleListBean = (ArticleListBean) t;
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
                Toast.makeText(mContext, TextUtils.isEmpty(message) ? "文章内容为空！" : message, Toast.LENGTH_SHORT).show();
            }
        }));

        articleAdapter = new MainArticleAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        articleListView.setLayoutManager(mLayoutManager);
        articleListView.setAdapter(articleAdapter);
        articleListView.addOnScrollListener(onScrollListener);

        etMainSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

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
        mainRepository.getMainArticleList(mPage, new HttpCallback<ArticleListBean>() {
            @Override
            public void onSucceed(Object t) {
                ArticleListBean articleListBean = (ArticleListBean) t;
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
                Toast.makeText(mContext, TextUtils.isEmpty(message) ? "文章内容为空！" : message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 加载banner信息
     */
    private void loadBanner() {
        mainRepository.getBanner(new HttpCallback<List<BannerBean>>() {
            @Override
            public void onSucceed(Object t) {
                List<BannerBean> bannerList = (List<BannerBean>) t;
                if (bannerList == null || bannerList.size() <= 0) {
                    onFailed(0, "list is empty!");
                    return;
                }
                Log.d(TAG, "banner list = " + bannerList);
                mBannerList = bannerList;
                showBanner();
            }

            @Override
            public void onFailed(int code, String message) {
                Toast.makeText(mContext, message != null ? message : "存在异常，请排查！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 将获取到的banner信息展示出来
     */
    private void showBanner() {
        if (mBannerList == null || mBannerList.size() == 0) {
            return;
        }
        List<String> imageList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (BannerBean bean : mBannerList) {
            String imageUrl = bean.getImagePath();
            imageList.add(imageUrl);
            String title = bean.getTitle();
            titleList.add(title);
        }

        //设置内置样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置加载图片工具
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        //设置图片网址或地址集合
        banner.setImages(imageList);
        //设置banner标题
        banner.setBannerTitles(titleList);
        //设置轮播时间
        banner.setDelayTime(3 * 1000);
        //设置是否为自动轮播
        banner.isAutoPlay(true);
        //设置显示器位置，小点点的位置
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置轮播图的监听
        banner.setOnBannerListener(position -> {
            if (mBannerList == null || mBannerList.size() == 0) {
                Toast.makeText(mContext, "banner list is empty！", Toast.LENGTH_SHORT).show();
                return;
            }
            BannerBean bean = mBannerList.get(position);
            if (bean == null || TextUtils.isEmpty(bean.getUrl())) {
                Toast.makeText(mContext, "banner bean url is empty！", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(mContext, NetWorkActivity.class);
            intent.putExtra("LINK_URL", bean.getUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        banner.start();
    }

    /**
     * RecyclerView 滑动监听器
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private int lastItem;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //当滑动到最后一个且停止滚动
            int total = Math.min(mTotalPage, 10);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItem + 1 == articleAdapter.getItemCount()
                    && mCurrentPage < total) {
                Log.d(TAG, "load more article");
                Toast.makeText(mContext, "show more articles!", Toast.LENGTH_SHORT).show();
                mPage += 1;
                loadMore();
            }else if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItem + 1 == articleAdapter.getItemCount()
                    && mCurrentPage >= total){
                //由于数据量太大，所以将可浏览的文章量控制在200篇内（20*10）
                Toast.makeText(mContext, "All Article is show!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };
}
