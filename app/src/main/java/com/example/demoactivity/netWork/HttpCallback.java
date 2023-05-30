package com.example.demoactivity.netWork;

import com.example.demoactivity.netWork.base.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class HttpCallback implements Callback<BaseResponse> {
    @Override
    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
        if (!response.isSuccessful()) {
            onFailure(call, new Throwable("code = " + response.code() + " message = " + response.message()));
            return;
        }
        //空数据
        BaseResponse body = response.body();
        if (body == null) {
            onFailure(call, new Exception("value = empty"));
            return;
        }

        int code = body.getErrorCode();
        String msg = body.getErrorMsg();
        if (code != Constants.CODE_SUCCESS) {
            //失败的情况
            onFailed(code, msg);
        }else {
            onSucceed(body.getData());
        }
    }

    @Override
    public void onFailure(Call<BaseResponse> call, Throwable t) {
        onFailed(Constants.CODE_NET_WORK_ERROR, t.getMessage());
    }

    public abstract void onSucceed(Object t);

    public abstract void onFailed(int code, String message);
}
