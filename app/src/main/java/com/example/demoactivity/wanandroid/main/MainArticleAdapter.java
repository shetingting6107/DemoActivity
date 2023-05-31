package com.example.demoactivity.wanandroid.main;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.NetWorkActivity;
import com.example.demoactivity.netWork.bean.ArticleBean;

import java.util.List;

public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.MainArticleViewHolder> {

    private List<ArticleBean> articleList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public MainArticleAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setArticleList(List<ArticleBean> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public MainArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainArticleViewHolder(mLayoutInflater.inflate(R.layout.activity_wan_main_article_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainArticleViewHolder holder, int position) {
        ArticleBean articleBean = articleList.get(position);
        holder.tv_author.setText(!TextUtils.isEmpty(articleBean.getAuthor()) ? articleBean.getAuthor() : articleBean.getShareUser());
        holder.tv_article_title.setText(articleBean.getTitle());
        holder.tv_publish_time.setText(articleBean.getAuthor() != null ? articleBean.getNiceDate() : articleBean.getNiceDate());
        List<ArticleBean.TagsBean> tagsList = articleBean.getTags();
        StringBuilder tags = new StringBuilder();
        if (tagsList != null && tagsList.size() > 0) {
            for (ArticleBean.TagsBean tag : tagsList) {
                tags.append(tag.getName()).append(" + ");
            }
        }
        if (tags.length() > 2) {
            holder.tv_tags.setText(tags.toString().substring(0, tags.toString().length() - 2));
        }else {
            holder.ll_tag.setVisibility(View.GONE);
            holder.tv_tags.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            if (TextUtils.isEmpty(articleBean.getLink())) {
                return;
            }
            Intent intent = new Intent(mContext, NetWorkActivity.class);
            intent.putExtra("LINK_URL", articleBean.getLink());
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }

    static class MainArticleViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_author;
        private TextView tv_article_title;
        private TextView tv_publish_time;
        private LinearLayout ll_tag;
        private TextView tv_tags;


        public MainArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_article_title = itemView.findViewById(R.id.tv_article_title);
            tv_publish_time = itemView.findViewById(R.id.tv_publish_time);
            ll_tag = itemView.findViewById(R.id.ll_tag);
            tv_tags = itemView.findViewById(R.id.tv_tags);

        }
    }
}
