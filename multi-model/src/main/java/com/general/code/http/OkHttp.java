package com.general.code.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: zml
 * Date  : 2019/1/11 - 15:30
 **/
public final class OkHttp {
    private final String TAG = OkHttp.class.getSimpleName();
    OkHttpClient httpClient = new OkHttpClient();
    final Request request = new Request.Builder()
            .url("http://www.baidu.com")
            .get()
            .build();
    Call call = httpClient.newCall(request);

    public void get(){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"response:"+response.body().string());
            }
        });
    }
}
