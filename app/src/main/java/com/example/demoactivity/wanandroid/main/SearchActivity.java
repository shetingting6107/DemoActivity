package com.example.demoactivity.wanandroid.main;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoactivity.R;
import com.example.demoactivity.netWork.Constants;
import com.example.demoactivity.netWork.HttpCallback;
import com.example.demoactivity.netWork.bean.SearchHotKeyBean;
import com.example.demoactivity.wanandroid.base.BaseActivity;

import java.util.List;

public class SearchActivity extends BaseActivity {

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvSearchKey;

    private MainRepository mainRepository;

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
            getSearchResult(title);
        });
        rvSearchKey.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> {
            String text = etSearch.getText().toString();
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(SearchActivity.this, "请检查输入文字！", Toast.LENGTH_SHORT).show();
                return;
            }
            getSearchResult(text);
        });
    }

    private void getSearchResult(String key) {
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra(Constants.Extra.EXTRA_SEARCH_KEY, key);
        startActivity(intent);
    }

    @Override
    public void initData() {
        mainRepository = new MainRepository();

        mainRepository.getSearchHotKey(new HttpCallback<List<SearchHotKeyBean>>() {
            @Override
            public void onSucceed(Object t) {
                List<SearchHotKeyBean> hotKeyBeans = (List<SearchHotKeyBean>) t;
                if (hotKeyBeans == null || hotKeyBeans.size() <= 0) {
                    return;
                }
                keyBeanList = hotKeyBeans;
                adapter.setKeyList(hotKeyBeans);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String message) {
                Toast.makeText(SearchActivity.this, TextUtils.isEmpty(message) ? "存在异常，请排查问题！" : message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
