package com.example.demoactivity.netWork.base;

import com.example.demoactivity.netWork.ApiServer;
import com.example.demoactivity.netWork.utils.RetrofitUtil;

public class BaseRepository {

    public static ApiServer apiServer = RetrofitUtil.getInstance().getApi(ApiServer.class);
}
