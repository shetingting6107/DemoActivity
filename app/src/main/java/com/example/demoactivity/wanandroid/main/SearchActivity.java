package com.example.demoactivity.wanandroid.main;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.bean.SearchHotKeyBean;
import com.example.demoactivity.wanandroid.base.BaseActivity;

import java.util.List;

public class SearchActivity extends BaseActivity {

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvSearchKey;

    private SearchKeyAdapter adapter;

    private List<SearchHotKeyBean> keyBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wan_search;
    }

    @Override
    public void initView() {
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        rvSearchKey = findViewById(R.id.rv_search_key);

        etSearch.setFocusable(true);

        rvSearchKey.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchKeyAdapter(this);
        adapter.setListener(position -> {
            if (keyBeanList == null || keyBeanList.size() <= 0 || keyBeanList.size() < position) {
                return;
            }
            SearchHotKeyBean key = keyBeanList.get(position);
            String title = key.getName();
            if (TextUtils.isEmpty(title)) {
                return;
            }
            etSearch.setText(title);
        });
        rvSearchKey.setAdapter(adapter);


    }

    @Override
    public void initData() {

    }

}
