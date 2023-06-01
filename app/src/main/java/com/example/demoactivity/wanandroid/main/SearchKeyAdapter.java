package com.example.demoactivity.wanandroid.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.bean.SearchHotKeyBean;
import com.example.demoactivity.wanandroid.inter.onRecyclerItemClickListener;

import java.util.List;

public class SearchKeyAdapter extends RecyclerView.Adapter<SearchKeyAdapter.SearchKeyViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private onRecyclerItemClickListener mListener;

    private List<SearchHotKeyBean> keyList;

    public SearchKeyAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setKeyList(List<SearchHotKeyBean> searchKeyList) {
        keyList = searchKeyList;
    }

    public void setListener(onRecyclerItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public SearchKeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchKeyViewHolder(mLayoutInflater.inflate(R.layout.activity_wan_search_key_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchKeyViewHolder holder, int position) {
        SearchHotKeyBean keyBean = keyList.get(position);
        String po = String.valueOf(position + 1);
        holder.tvPosition.setText(po);
        holder.tvKeyTitle.setText(keyBean.getName());
        if (keyBean.getOrder() <= 3) {
            holder.ivHot.setVisibility(View.VISIBLE);
        }else {
            holder.ivHot.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return keyList != null ? keyList.size() : 0;
    }

    static class SearchKeyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPosition;
        private TextView tvKeyTitle;
        private ImageView ivHot;

        public SearchKeyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvKeyTitle = itemView.findViewById(R.id.tv_key_title);
            ivHot = itemView.findViewById(R.id.iv_hot);
        }
    }
}
