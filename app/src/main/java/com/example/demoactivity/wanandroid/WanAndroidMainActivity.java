package com.example.demoactivity.wanandroid;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.base.BaseResponse;
import com.example.demoactivity.netWork.bean.ArticleBean;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.netWork.utils.JSONUtil;
import com.example.demoactivity.wanandroid.base.BaseActivity;
import com.example.demoactivity.wanandroid.main.MainArticleAdapter;
import com.example.demoactivity.wanandroid.main.MainRepository;

import java.util.ArrayList;
import java.util.List;

public class WanAndroidMainActivity extends BaseActivity {

    private MainRepository mainRepository;

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView articleListView;
    private MainArticleAdapter articleAdapter;

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
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainRepository.getMainArticleList(0, new HttpCallback() {
                    @Override
                    public void onSucceed(BaseResponse response) {
                        ArticleListBean articleListBean = JSONUtil.parseToArticleListBean(response.getData());
                        if (articleListBean == null) {
                            onFailed(0, null);
                            return;
                        }
                        List<ArticleBean> articleList = articleListBean.getDatas();
                        List<ArticleBean> list = removeNullTags(articleList);
                        articleAdapter.setArticleList(articleList);
                        articleAdapter.notifyDataSetChanged();
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        swipeLayout.setRefreshing(false);
                        Toast.makeText(WanAndroidMainActivity.this, TextUtils.isEmpty(message) ? "文章内容为空！" : message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        articleAdapter = new MainArticleAdapter(this);
        articleListView.setLayoutManager(new LinearLayoutManager(this));
        articleListView.setAdapter(articleAdapter);

        initList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wan_android_main;
    }

    private void initList() {
        mainRepository.getMainArticleList(0, new HttpCallback() {
            @Override
            public void onSucceed(BaseResponse response) {
                ArticleListBean articleListBean = JSONUtil.parseToArticleListBean(response.getData());
                if (articleListBean == null) {
                    onFailed(0, null);
                    return;
                }
                List<ArticleBean> articleList = articleListBean.getDatas();
                List<ArticleBean> list = removeNullTags(articleList);
                articleAdapter.setArticleList(articleList);
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String message) {
                Toast.makeText(WanAndroidMainActivity.this, TextUtils.isEmpty(message) ? "文章内容为空！" : message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<ArticleBean> removeNullTags(List<ArticleBean> beans) {
        List<ArticleBean> beanList = new ArrayList<>();
        if (beans == null || beans.size() <= 0) {
            return beanList;
        }

        for (ArticleBean bean : beans) {
            if (bean.getTags() != null && bean.getTags().size() > 0) {
                beanList.add(bean);
            }
        }

        return beanList;
    }
}
