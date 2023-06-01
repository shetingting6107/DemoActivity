package com.example.demoactivity.wanandroid.main;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.Constants;
import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.bean.ArticleBean;
import com.example.demoactivity.netWork.bean.ArticleListBean;
import com.example.demoactivity.wanandroid.base.BaseActivity;

import java.util.List;

public class SearchResultActivity extends BaseActivity {

    public static final String TAG = "WanAndroidSearch";

    private ImageView ivBack;
    private EditText etSearch;
    private Button btnSearch;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvArticle;

    private LinearLayoutManager mLayoutManager;

    private MainArticleAdapter articleAdapter;
    private int mPage = 0;
    private int mTotalPage = 0;
    private int mCurrentPage = 0;
    private String mKey;
    private Context mContext;

    private MainRepository mainRepository;

    private List<ArticleBean> mArticleList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wan_search_result;
    }

    @Override
    public void initView() {
        mContext = getApplicationContext();
        ivBack = findViewById(R.id.iv_back);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        swipeLayout = findViewById(R.id.swipe_layout);
        rvArticle = findViewById(R.id.rv_result_article);

        mLayoutManager = new LinearLayoutManager(this);
        rvArticle.setLayoutManager(mLayoutManager);
        articleAdapter = new MainArticleAdapter(this);
        rvArticle.setAdapter(articleAdapter);
        rvArticle.addOnScrollListener(onScrollListener);

        ivBack.setOnClickListener(v -> onBackPressed());

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etSearch.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(mContext, "搜索关键词为空，请检查！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPage = 0;
                mKey = text;
                getArticleByKey();
            }
        });
    }

    @Override
    public void initData() {
        mainRepository = new MainRepository();

        //设置下拉刷新loading圆圈的颜色
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //设置下拉刷新loading背景颜色
        swipeLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.color_dark_gray));
        //设置刷新监听事件
        swipeLayout.setOnRefreshListener(() -> mainRepository.getArticleByKey(0, mKey, new HttpCallback<ArticleListBean>() {
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

        Intent intent = getIntent();
        String key = intent.getStringExtra(Constants.Extra.EXTRA_SEARCH_KEY);
        etSearch.setText(key);
        mKey = key;
        getArticleByKey();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getArticleByKey() {
        mainRepository.getArticleByKey(mPage, mKey, new HttpCallback<ArticleListBean>() {
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
                Toast.makeText(mContext, TextUtils.isEmpty(message) ? "没有查询到相关文章！" : message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * RecyclerView 滑动监听器
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        private int lastItem;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int total = Math.min(mTotalPage, 10);
            //当滑动到最后一个且停止滚动
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItem + 1 == articleAdapter.getItemCount()
                    && mCurrentPage < total) {
                Log.d(TAG, "load more article");
                Toast.makeText(mContext, "show more articles!", Toast.LENGTH_SHORT).show();
                mPage += 1;
                getArticleByKey();
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
